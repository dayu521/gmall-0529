package com.atguigu.gmall.manager.spu;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * Spu海报
 */
@Data
public class SpuPoster extends SuperBean {

    //spu_id  img_name  img_url
    private Integer spuId;//关联的商品id
    private String imgName;//海报图片的名字
    private String imgUrl;//海报图片的url地址
}
