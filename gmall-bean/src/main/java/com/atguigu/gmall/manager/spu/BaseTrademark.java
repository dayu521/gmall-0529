package com.atguigu.gmall.manager.spu;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * 品牌表
 */
@Data
public class BaseTrademark extends SuperBean {

    //tm_name  logo_url  is_enable
    private String  tmName; //品牌名字
    private String logoUrl;//品牌url地址
    private String isEnable;//是否启用

}
