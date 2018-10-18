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
import com.atguigu.gmall.manager.es.SkuSearchResponseEsVo;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.search.constant.EsConstant;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.Aggregation;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.StringUtils;

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
            Index index = new Index.Builder(skuInfoEsVo)
                    .index(EsConstant.GMALL_INDEX)
                    .type(EsConstant.GMALL_SKU_TYPE)
                    .id(skuInfoEsVo.getId() + "")
                    .build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                log.error("Es保存数据出问题了：{}",e);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public SkuSearchResponseEsVo searchSkuInfoFromEs(SkuSearchParamEsVo vo) {
        //1、构建DSL
        String queryDSL = buildSkuQueryDSL(vo);

        Search search = new Search.Builder(queryDSL)
                .addIndex(EsConstant.GMALL_INDEX)
                .addType(EsConstant.GMALL_SKU_TYPE)
                .build();
        try {
            //ES查询
            SearchResult searchResult = jestClient.execute(search);

            //2、构建查询结果
            return  buildSkuQueryResponse(searchResult);
        } catch (IOException e) {
            log.error("jestClient检索异常{}",e);
        }
        return null;
    }

    /**
     * 构建查询结果
     * @param searchResult
     * @return
     */
    private SkuSearchResponseEsVo buildSkuQueryResponse(SearchResult searchResult) {

        SkuSearchResponseEsVo responseEsVos = new SkuSearchResponseEsVo();
        List<SkuInfoEsVo> skuInfoEsVoList = new ArrayList<>();
        List<SearchResult.Hit<SkuInfoEsVo, Void>> hits = searchResult.getHits(SkuInfoEsVo.class);

        //整理skuInfo返回数据
        for (SearchResult.Hit<SkuInfoEsVo, Void> hit : hits) {
            SkuSearchResponseEsVo responseEsVo = new SkuSearchResponseEsVo();
            SkuInfoEsVo source = hit.source;//获取查询数据
            Map<String, List<String>> highlight = hit.highlight;
            //修改高亮

            if(hit.highlight!=null){
                source.setSkuName( highlight.get("skuName").get(0));
            }

            skuInfoEsVoList.add(source);
        }

        //回传数据
        responseEsVos.setSkuInfo(skuInfoEsVoList);

        //整理聚合数据
        MetricAggregation aggregations = searchResult.getAggregations();
        TermsAggregation valueIdGroup = aggregations.getTermsAggregation("valueIdGroup");
        //获取聚合的所有值
        List<Integer> valueIds = new ArrayList<>();
        for (TermsAggregation.Entry entry : valueIdGroup.getBuckets()) {
            String key = entry.getKey();
            valueIds.add(Integer.parseInt(key));
        };
        //查出这些属性值所在的平台属性，以及可以选择的其他值
        List<BaseAttrInfo> baseAttrInfos = skuService.getBaseAttrInfoByAttrValueIdIn(valueIds);
        responseEsVos.setBaseAttrInfos(baseAttrInfos);

        responseEsVos.setTotal(searchResult.getTotal().intValue());

        return responseEsVos;
    }

    /**
     * 构建 QueryDSL语句
     * @param vo
     */

    private String buildSkuQueryDSL(SkuSearchParamEsVo vo) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //query查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        sourceBuilder.query(boolQuery);

        //过滤
        //1、三级分类过滤
        if(vo.getCatalog3Id()!=null){
            TermQueryBuilder termCatalog3Id = new TermQueryBuilder("catalog3Id",vo.getCatalog3Id());
            boolQuery.filter(termCatalog3Id);
        }


        //2、平台属性过滤
        if(vo.getValueId()!=null&&vo.getValueId().length>0){
            for (Integer valueId : vo.getValueId()) {
                TermQueryBuilder termValueId = new TermQueryBuilder("baseAttrEsVos.valueId", valueId);
                boolQuery.filter(termValueId);
            }
        }

        //3、按照名字查询
        if(!StringUtils.isEmpty(vo.getKeyword())){
            MatchQueryBuilder matchSkuName = new MatchQueryBuilder("skuName",vo.getKeyword());
            boolQuery.must(matchSkuName);

            //高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<span style='color:red'>");
            highlightBuilder.postTags("</span>");
            highlightBuilder.field("skuName");
            sourceBuilder.highlight(highlightBuilder);
        }


        //分页
        sourceBuilder.from( (vo.getPageNo()-1)*vo.getPageSize() );
        sourceBuilder.size(vo.getPageSize());

        //排序
        SortOrder sortOrder = SortOrder.DESC;
        if(!StringUtils.isEmpty(vo.getSortOrder())&&StringUtils.isEmpty(vo.getSortName())){
            switch (vo.getSortOrder()){
                case "desc":
                    sortOrder = SortOrder.DESC;
                    break;
                case "asc":
                    sortOrder = SortOrder.ASC;
                    break;
            }
            sourceBuilder.sort(vo.getSortName(),sortOrder);
        }

        //聚合
        TermsBuilder termsBuilder = AggregationBuilders.terms("valueIdGroup").field("baseAttrEsVos.valueId");
        sourceBuilder.aggregation(termsBuilder);

        String queryDsl = sourceBuilder.toString();
        return queryDsl;
    }
}
