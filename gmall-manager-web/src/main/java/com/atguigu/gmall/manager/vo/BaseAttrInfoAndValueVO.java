package com.atguigu.gmall.manager.vo;

import lombok.Data;

import java.util.List;


/**
 *   * {
 *  "id":"1",
 *  "attrName":"励志读物",
 *  "attrValues":[
 *      {"id":2,"valueName":"111","attrId":"1"},
 *      {"id":"","valueName":"对萨达撒","attrId":1}
 *      ]
 * }
 */
@Data
public class BaseAttrInfoAndValueVO {

    private Integer id;
    private String attrName;
    private Integer catalog3Id;//收集页面提交的三级分类的id
    private List<BaseAttrValueVo> attrValues;


}

