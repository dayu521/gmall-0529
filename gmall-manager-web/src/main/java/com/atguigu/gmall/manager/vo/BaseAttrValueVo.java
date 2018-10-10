package com.atguigu.gmall.manager.vo;


import lombok.Data;

/**
 * {"id":2,"valueName":"111","attrId":"1"},
 *      {"id":"","valueName":"对萨达撒","attrId":1}
 */
@Data
public class BaseAttrValueVo {

    private Integer id;
    private String valueName;
    private Integer  attrId;


}
