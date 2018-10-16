package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.constant.RedisCacheKeyConst;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuAttrValueMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuImageMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuSaleAttrValueMapper;
import com.atguigu.gmall.manager.mapper.spu.SpuSaleAttrMapper;
import com.atguigu.gmall.manager.sku.*;
import com.atguigu.gmall.manager.spu.SpuSaleAttr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Slf4j
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

    @Autowired
    JedisPool jedisPool;

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

    @Override
    public SkuInfo getSkuInfoBySkuId(Integer skuId) throws InterruptedException {
        Jedis jedis = jedisPool.getResource();
        //sku:30:info  = {xx:xx}
        //信息肯定不能一直在缓存，我们以后缓存的所有key都加上过期时间
        String key = RedisCacheKeyConst.SKU_INFO_PREFIX+skuId+RedisCacheKeyConst.SKU_INFO_SUFFIX;
        SkuInfo result = null;

        //先去缓存中看看
        String s = jedis.get(key);
        if(s!=null){
            //如果缓存中有，转成我们想要的对象
            log.debug("缓存中找到数据了：{}",skuId);
            result = JSON.parseObject(s, SkuInfo.class);
            jedis.close();
        }else if("null".equals(s)){
            //防止缓存穿透的
            //缓存中存了，只不过这是你数据库给我的
           //之前数据库查过，但是没有，所以给缓存中放了一个null串
            return null;
        }else{
            //当这个数据等于null的时候
            //缓存中没有必须从数据库先查出来，在放到缓存
            //我们需要加锁
            // 拿到锁再去查数据库；
            String lock = jedis.set(RedisCacheKeyConst.LOCK_SKU_INFO, "ABC", "NX", "EX", RedisCacheKeyConst.LOCK_TIMEOUT);

            if(lock == null){
                //没有拿到锁
                log.debug("没有获取到锁等待重试");
                Thread.sleep(1000);//等待一秒重试
                //自旋锁
                getSkuInfoBySkuId(skuId);
            }else if("OK".equals(lock)) {
                log.debug("获取到锁，查数据库了：");
                result = getFromDb(skuId);
                //sku:3:info --xxxxx
                //sku:4:info --yyyy
                //将对象转为json存到redis中
                String skuInfoJson = JSON.toJSONString(result);
                //json.null  "null"
                log.debug("从数据库查到的数据：{}",skuInfoJson);
                //存到缓存中,第二天以后就会有人新查数据
                if("null".equals(skuInfoJson)){
                    //空数据缓存时间短
                    jedis.setex(key,RedisCacheKeyConst.SKU_INFO_NULL_TIMEOUT,skuInfoJson);
                }else{
                    //正常数据缓存时间长
                    jedis.setex(key,RedisCacheKeyConst.SKU_INFO_TIMEOUT,skuInfoJson);
                }

                //手动释放，即使释放失败，也会自动过期删除
                jedis.del(RedisCacheKeyConst.LOCK_SKU_INFO);
            }
            jedis.close();
        }



        return result;
    }

    @Override
    public List<SkuAttrValueMappingTo> getSkuAttrValueMapping(Integer spuId) {
        return skuInfoMapper.getSkuAttrValueMapping(spuId);
    }

    private SkuInfo getFromDb(Integer skuId){
        log.debug("缓存中没找到。从数据准备查询skuId是{}的商品信息",skuId);
        //1、先查出skuInfo基本信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if(skuInfo == null){
            //即使没有数据也返回出去放在缓存
            return null;
        }
        //2、查出这个skuInfo的所有图片信息
        List<SkuImage> skuImages = skuImageMapper.selectList(new QueryWrapper<SkuImage>().eq("sku_id", skuInfo.getId()));
        skuInfo.setSkuImages(skuImages);
        //3、查出整个skuAttrValue信息
        List<SkuAllSaveAttrAndValueTo> skuAllSaveAttrAndValueTos = skuImageMapper.getSkuAllSaveAttrAndValue(skuInfo.getId(),skuInfo.getSpuId());
        skuInfo.setSkuAllSaveAttrAndValueTos(skuAllSaveAttrAndValueTos);

        //加缓存：redis作为缓存中间件；内存数据库
        //

        return skuInfo;
    }
}
