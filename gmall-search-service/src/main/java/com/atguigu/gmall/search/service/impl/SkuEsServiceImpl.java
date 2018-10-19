package com.atguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.es.SkuBaseAttrEsVo;
import com.atguigu.gmall.manager.es.SkuInfoEsVo;
import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVo;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.search.constant.EsConstant;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class SkuEsServiceImpl implements SkuEsService {


    @Reference
    SkuService skuService;

    @Autowired
    JestClient jestClient;


    @Async  //异步。表示这是一个异步调用
    @Override
    public void onSale(Integer skuId) {
        System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"+skuId);
        try {
            //1、查出这个sku对应的详细信息
            SkuInfo info = skuService.getSkuInfoBySkuId(skuId);
            SkuInfoEsVo skuInfoEsVo = new SkuInfoEsVo();


            //将查出的数据拷贝过来
            BeanUtils.copyProperties(info,skuInfoEsVo);

            //查出当前sku的所有平台属性的值
            List<SkuBaseAttrEsVo> vos = skuService.getSkuBaseAttrValueIds(skuId);
            skuInfoEsVo.setBaseAttrEsVos(vos);


            //保存sku信息到es
            Index index = new Index.Builder(skuInfoEsVo).index(EsConstant.GMALL_INDEX).type(EsConstant.GMALL_SKU_TYPE)
                    .id(skuInfoEsVo.getId() + "").build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                log.error("Es保存数据出问题了：{}",e);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    /**
     * 按照查询参数查出页面需要的数据
     * @param paramEsVo
     * @return
     */
    @Override
    public SkuSearchResultEsVo searchSkuFromES(SkuSearchParamEsVo paramEsVo) {
        SkuSearchResultEsVo resultEsVo = null;
       // jestClient.execute(xxx)
        //0、DSL的大拼串
        String queryDsl = buildSkuSearchQueryDSL(paramEsVo);

        //1、传入dsl语句
        Search search = new Search.Builder(queryDsl)
                .addIndex(EsConstant.GMALL_INDEX)
                .addType(EsConstant.GMALL_SKU_TYPE)
                .build();

        //2、执行查询
        try {
            SearchResult result = jestClient.execute(search);

            //===3、把查出出来的result处理成能给页面返回的SkuSearchResultEsVo对象
            resultEsVo = buildSkuSearchResult(result);
            resultEsVo.setPageNo(paramEsVo.getPageNo());
            return  resultEsVo;
        } catch (IOException e) {
           log.error("ES查询出故障：{}",e);
        }
        return resultEsVo;
    }


    @Async
    @Override
    public void updateHotScore(Integer skuId, Long hincrBy) {
        String updateHotScore = "{\"doc\": {\"hotScore\":"+hincrBy+"}}";
        Update update = new Update.Builder(updateHotScore)
                .index(EsConstant.GMALL_INDEX)
                .type(EsConstant.GMALL_SKU_TYPE)
                .id(skuId+"")
                .build();
        try {
            jestClient.execute(update);
        } catch (IOException e) {
           log.error("ES更新热度出问题了：{}",e);
        }
    }

    //构造QueryDSL字符串
    private    String buildSkuSearchQueryDSL(SkuSearchParamEsVo paramEsVo){
        //1、创建一个搜索数据的构建器。帮我们能构造出DSL
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //过滤三级分类信息
        if(paramEsVo.getCatalog3Id()!=null){
            //过滤三级分类信息
            TermQueryBuilder termCatalog3 = new TermQueryBuilder("catalog3Id",paramEsVo.getCatalog3Id());
            boolQuery.filter(termCatalog3);
        }

        //过滤valueId信息
        if(paramEsVo.getValueId()!=null && paramEsVo.getValueId().length>0){
            for (Integer vid : paramEsVo.getValueId()) {
                //过滤 页面提交来的所有valueId
                TermQueryBuilder termValueId = new TermQueryBuilder("baseAttrEsVos.valueId", vid);
                boolQuery.filter(termValueId);
            }
            ;
        }


        //搜索
        if(!StringUtils.isEmpty(paramEsVo.getKeyword())){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", paramEsVo.getKeyword());
            boolQuery.must(matchQueryBuilder);

            //高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuName");
            highlightBuilder.preTags("<span style='color:red'>");
            highlightBuilder.postTags("</span>");
            sourceBuilder.highlight(highlightBuilder);
        }



        //以上查询与过滤完成
        sourceBuilder.query(boolQuery);




        //排序
        if(!StringUtils.isEmpty(paramEsVo.getSortField())){
            SortOrder sortOrder = null;
            switch (paramEsVo.getSortOrder()){
                case "desc":sortOrder = SortOrder.DESC;break;
                case "asc":sortOrder = SortOrder.ASC;break;
                default: sortOrder = SortOrder.DESC;
            }
            sourceBuilder.sort(paramEsVo.getSortField(), sortOrder);
        }


        //分页
        //页面传入的是页码，我们计算一下从第几个开始查;  (pageNo - 1)*pageSize   0  12
        sourceBuilder.from( (paramEsVo.getPageNo()-1)*paramEsVo.getPageSize());
        sourceBuilder.size(paramEsVo.getPageSize());



        //聚合
        TermsBuilder termsBuilder = new TermsBuilder("valueIdAggs");
        termsBuilder.field("baseAttrEsVos.valueId");
        sourceBuilder.aggregation(termsBuilder);



        //他的string方法就是获取到我们的dsl语句
        String dsl = sourceBuilder.toString();
        return dsl;
    }

    //将查出的结果构建为页面能用的vo对象数据
    private SkuSearchResultEsVo buildSkuSearchResult( SearchResult result){
        SkuSearchResultEsVo resultEsVo = new SkuSearchResultEsVo();

        //所有skuInfo的集合
        List<SkuInfoEsVo> skuInfoEsVoList =  null;
        //1、从es搜索的结果中找到所有的SkuInfo信息

        //拿到命中的所有记录
        List<SearchResult.Hit<SkuInfoEsVo, Void>> hits = result.getHits(SkuInfoEsVo.class);
        if(hits == null || hits.size() == 0){
            return  null;
        }else{
            //查到了数据
            skuInfoEsVoList = new ArrayList<>(hits.size());
            //遍历所有命中的记录，取出每一个SKuInfo放在list中，并且设置好高亮
            for (SearchResult.Hit<SkuInfoEsVo, Void> hit : hits) {
                SkuInfoEsVo source = hit.source;
                
                //有可能有高亮的
                Map<String, List<String>> highlight = hit.highlight;
                //普通非全文模糊【匹配的是没有高亮的
                if(highlight!=null){
                   String higtText = highlight.get("skuName").get(0);
                    //替换高亮
                    source.setSkuName(higtText);
                }

                skuInfoEsVoList.add(source);
            }
        }



        //保存了skuInfo信息
        resultEsVo.setSkuInfoEsVos(skuInfoEsVoList);
        //总计录数
        resultEsVo.setTotal(result.getTotal().intValue());



        //从聚合的数据中取出所有平台属性以及他的值
        List<BaseAttrInfo> baseAttrInfos = getBaseAttrInfoGroupByValueId(result);
        resultEsVo.setBaseAttrInfos(baseAttrInfos);
        return  resultEsVo;
    }

    /**
     * 根据es中查询到的聚合的结果找到所有涉及到的平台属性对应的值
     * @param result
     * @return
     */
    private List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(SearchResult result){

        MetricAggregation aggregations = result.getAggregations();
        //1、获取term聚合出来的数据
        TermsAggregation valueIdAggs = aggregations.getTermsAggregation("valueIdAggs");
        List<TermsAggregation.Entry> buckets = valueIdAggs.getBuckets();


        List<Integer> valueIds = new ArrayList<>();
        //2、遍历buckets
        for (TermsAggregation.Entry bucket : buckets) {
            String key = bucket.getKey();
            valueIds.add(Integer.parseInt(key));
        }

        //3、查询所有涉及到的平台属性以及值
        return  skuService.getBaseAttrInfoGroupByValueId(valueIds);
    }


}
