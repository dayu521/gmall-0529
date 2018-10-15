package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuAttrValueMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuImageMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuSaleAttrValueMapper;
import com.atguigu.gmall.manager.mapper.spu.SpuSaleAttrMapper;
import com.atguigu.gmall.manager.sku.SkuAttrValue;
import com.atguigu.gmall.manager.sku.SkuImage;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.sku.SkuSaleAttrValue;
import com.atguigu.gmall.manager.spu.SpuSaleAttr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    
    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    SkuImageMapper skuImageMapper;

    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id) {
        return baseAttrInfoMapper.getBaseAttrInfoByCatalog3Id(catalog3Id);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(Integer spuId) {
        return spuSaleAttrMapper.getSpuSaleAttrBySpuId(spuId);
    }

    @Transactional
    @Override
    public void saveBigSkuInfo(SkuInfo skuInfo) {

        //1、先保存基本的skuInfo信息
        skuInfoMapper.insert(skuInfo);
        
        //2、再保存提交的图片、平台属性、销售属性等
        List<SkuImage> skuImages = skuInfo.getSkuImages();
        for (SkuImage skuImage : skuImages) {
            //skuid保存才知道
            skuImage.setSkuId(skuInfo.getId());
            skuImageMapper.insert(skuImage);
        }


        List<SkuAttrValue> skuAttrValues = skuInfo.getSkuAttrValues();
        for (SkuAttrValue skuAttrValue : skuAttrValues) {
            skuAttrValue.setSkuId(skuInfo.getId());
            skuAttrValueMapper.insert(skuAttrValue);
        }


        List<SkuSaleAttrValue> skuSaleAttrValues = skuInfo.getSkuSaleAttrValues();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValues) {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValueMapper.insert(skuSaleAttrValue);
        }


    }

    @Override
    public List<SkuInfo> getSkuInfoBySpuId(Integer spuId) {
        return skuInfoMapper.selectList(new QueryWrapper<SkuInfo>().eq("spu_id",spuId));
    }
}
