package com.atguigu.gmall.manager.spu;

import com.atguigu.gmall.SuperBean;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 基本销售属性
 */


@TableName("base_sale_attr")  //按照命名规则一致的话可以省略
@Data
public class BaseSaleAttr extends SuperBean {

    private String name;//基本销售属性名
}
