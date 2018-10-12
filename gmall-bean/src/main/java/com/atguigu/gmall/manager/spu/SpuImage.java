package com.atguigu.gmall.manager.spu;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * Spu图片信息
 */
@Data
public class SpuImage extends SuperBean {

    //spu_id  img_name  img_url
    private Integer spuId;//图片对应的商品id
    private String imgName;//图片的名字
    private String imgUrl;//图片的url地址

}
