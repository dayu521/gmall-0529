package com.atguigu.gmall.manager.sku;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

@Data
public class SkuImage extends SuperBean {

    //sku_id  img_name       img_url  spu_img_id  is_default
    private Integer skuId;//当前图片对应的skuId
    private String imgName;//图片的名字
    private String imgUrl;//图片的url
    private Integer spuImgId;//图片对应的spu_image表中的id
    private String isDefault;//是否默认图片


}
