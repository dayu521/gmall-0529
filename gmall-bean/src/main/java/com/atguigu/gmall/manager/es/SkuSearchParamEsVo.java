package com.atguigu.gmall.manager.es;

import lombok.Data;

import java.io.Serializable;

/**
 * 页面传入的所有 查询参数
 */
@Data
public class SkuSearchParamEsVo implements Serializable {


    String keyword; //关键字搜索
    Integer catalog3Id; //三级目录id  catalog3Id
    Integer[] valueId; //这个值会有多个
    Integer pageNo = 1;  //页码
    Integer pageSize = 12;
    String sortField = "hotScore";
    String sortOrder = "desc";
}
