package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SkuEsService;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.constant.RedisCacheKeyConst;
import com.atguigu.gmall.manager.es.SkuBaseAttrEsVo;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SkuServiceImpl implements SkuService {

    @Reference
    SkuEsService skuEsService;

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

    /**
     * 1、查缓存===分布式锁的使用
     *      1.1）、缓存中有
     *          直接返回结果，return
     *      1.2）、缓存中有但是是"null"串
     *          说明之前数据库是null串，也是给其他调用者返回null
     *      1.3）、缓存中没有
     *          1.3.1）、获取锁 jedis.set(k,v,"NX","EX",1000(超时));保证占位和超时是原子操作
     *              1.3.1.1）、获取到了
     *                  1.3.1.1.1）、执行业务逻辑
     *                  1.3.1.1.2）、缓存执行结果
     *                  1.3.1.1.3）、释放锁
     *              1.3.1.2）、没获取到
     *                  自旋即可
     *
     * @param skuId
     * @return
     * @throws InterruptedException
     */
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
            return result;
        }
        if("null".equals(s)){
            //防止缓存穿透的
            //缓存中存了，只不过这是你数据库给我的
           //之前数据库查过，但是没有，所以给缓存中放了一个null串
            return null;
        }
        if(s==null){
            //当这个数据等于null的时候
            //缓存中没有必须从数据库先查出来，在放到缓存
            //我们需要加锁
            // 拿到锁再去查数据库；
            String token = UUID.randomUUID().toString();
            String lock = jedis.set(RedisCacheKeyConst.LOCK_SKU_INFO+":"+skuId, token, "NX", "EX", RedisCacheKeyConst.LOCK_TIMEOUT);

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
                //判断是否还是我的锁，如果是才删
                //NB之处....释放锁；解锁有问题吗？删锁的错误姿势
//                String redisToken = jedis.get(RedisCacheKeyConst.LOCK_SKU_INFO);
//                if(token.equals(redisToken)){
//                    jedis.del(RedisCacheKeyConst.LOCK_SKU_INFO);
//                }else{
//                    //业务逻辑已经超出锁的时间了，别人已经持有锁了，我们不要把别人锁删了
//                }

                //脚本；正确的解锁；一定要是原子操作
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                jedis.eval(script,
                        Collections.singletonList(RedisCacheKeyConst.LOCK_SKU_INFO+":"+skuId),
                        Collections.singletonList(token));
            }
            jedis.close();
            return result;
        }
        jedis.close();
        return null;

    }

    @Override
    public List<SkuAttrValueMappingTo> getSkuAttrValueMapping(Integer spuId) {
        return skuInfoMapper.getSkuAttrValueMapping(spuId);
    }

    @Override
    public List<SkuBaseAttrEsVo> getSkuBaseAttrValueIds(Integer skuId) {
        List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.selectList(new QueryWrapper<SkuAttrValue>().eq("sku_id", skuId));

        List<SkuBaseAttrEsVo> results = new ArrayList<>();
        for (SkuAttrValue skuAttrValue : skuAttrValues) {
            Integer valueId = skuAttrValue.getValueId();
            SkuBaseAttrEsVo vo = new SkuBaseAttrEsVo();
            vo.setValueId(valueId);
            results.add(vo);
        }

        return results;
    }

    @Override
    public List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(List<Integer> valueIds) {


        return baseAttrInfoMapper.getBaseAttrInfoGroupByValueId(valueIds);
    }

    //增加商品热度信息
    @Async
    @Override
    public void incrSkuHotScore(Integer skuId) {
        Jedis jedis = jedisPool.getResource();
        Long hincrBy = jedis.hincrBy(RedisCacheKeyConst.SKU_HOT_SCORE, skuId + "", 1);
        if(hincrBy % 3 == 0){
            //更新ES的热度
            skuEsService.updateHotScore(skuId,hincrBy);
        }
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
