package com.atguigu.gmall.search;

import com.atguigu.gmall.manager.es.SkuSearchParamEsVo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.util.StringUtils;


public class SimpleTest {

    @Test
    public  void  testDsl(){
        SkuSearchParamEsVo vo = new SkuSearchParamEsVo();
        vo.setCatalog3Id(61);
        vo.setKeyword("银色");
        vo.setPageNo(1);
        vo.setSortName("price");
        vo.setValueId(new Integer[]{14});
        vo.setSortOrder("asc");
        buildSkuQueryDSL( vo);
    }


    public void buildSkuQueryDSL(SkuSearchParamEsVo vo) {
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

        String toString = sourceBuilder.toString();
        System.out.println(toString);
    }
}
