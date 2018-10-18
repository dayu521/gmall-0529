package com.atguigu.gmall.manager.es;

import lombok.Data;

import java.io.Serializable;

/**
 * sku搜索从页面传过来的数据
 */
@Data
public class SkuSearchParamEsVo  implements Serializable{

    private Integer catalog3Id;
    private String keyword;
    private Integer[] valueId;
    private String sortName = "hotScore";
    private String sortOrder = "desc";
    private Integer pageNo = 1;//页码
    private Integer pageSize = 12;//每页多少数据
}
