package com.atguigu.gmall.manager;

import com.atguigu.gmall.SuperBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BaseAttrValue extends SuperBean {


    private String valueName;

    private String attrId;

    private String isEnabled;

}
