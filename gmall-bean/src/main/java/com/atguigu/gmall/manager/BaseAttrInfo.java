package com.atguigu.gmall.manager;

import com.atguigu.gmall.SuperBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 平台属性名信息
 */
public class BaseAttrInfo extends SuperBean {


    private String attrName;

    private Integer catalog3Id;

    private String isEnabled;

}
