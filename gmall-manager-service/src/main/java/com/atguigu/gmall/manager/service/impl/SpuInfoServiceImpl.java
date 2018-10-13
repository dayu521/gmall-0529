package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.SpuInfoService;
import com.atguigu.gmall.manager.mapper.spu.*;
import com.atguigu.gmall.manager.spu.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Service
public class SpuInfoServiceImpl implements SpuInfoService {


    @Autowired
    SpuInfoMapper spuInfoMapper;
    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    SpuImageMapper spuImageMapper;

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public List<SpuInfo> getSpuInfoByC3Id(Integer catalog3Id) {


        return spuInfoMapper.selectList(new QueryWrapper<SpuInfo>().eq("catalog3_id",catalog3Id));
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttr() {
        return baseSaleAttrMapper.selectList(null);
    }


    @Override
    public void saveBigSpuInfo(SpuInfo spuInfo) {

        //1、保存spu的基本信息
        spuInfoMapper.insert(spuInfo);

        //获取到刚才保存的spu的id
        Integer spuId = spuInfo.getId();


        //2、保存spu的所有的图片信息
        List<SpuImage> spuImages = spuInfo.getSpuImages();
        for (SpuImage spuImage : spuImages) {
            //设置好商品id
            spuImage.setSpuId(spuId);
            spuImageMapper.insert(spuImage);
        }

        //3、保存spu的所有的销售属性的信息
        List<SpuSaleAttr> spuSaleAttrs = spuInfo.getSpuSaleAttrs();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrs) {
            spuSaleAttr.setSpuId(spuId);
            spuSaleAttrMapper.insert(spuSaleAttr);

            //4、保存销售属性值的信息
            List<SpuSaleAttrValue> saleAttrValues = spuSaleAttr.getSaleAttrValues();
            for (SpuSaleAttrValue saleAttrValue : saleAttrValues) {
                //设置spu的id
                saleAttrValue.setSpuId(spuId);
                //获取到销售属性的id
                saleAttrValue.setSaleAttrId(spuSaleAttr.getSaleAttrId());
                //保存销售属性值的对应关系
                spuSaleAttrValueMapper.insert(saleAttrValue);
            }

        }




    }
}
