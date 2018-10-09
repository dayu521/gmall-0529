package com.atguigu.gmall.manager;

import com.atguigu.gmall.SuperBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class BaseAttrValue extends SuperBean {


    private String valueName;

    private String attrId;

    private String isEnabled;

}
