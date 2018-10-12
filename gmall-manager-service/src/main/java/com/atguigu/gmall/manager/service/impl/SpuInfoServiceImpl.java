package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.SpuInfoService;
import com.atguigu.gmall.manager.mapper.spu.SpuInfoMapper;
import com.atguigu.gmall.manager.spu.SpuInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Service
public class SpuInfoServiceImpl implements SpuInfoService {


    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Override
    public List<SpuInfo> getSpuInfoByC3Id(Integer catalog3Id) {


        return spuInfoMapper.selectList(new QueryWrapper<SpuInfo>().eq("catalog3_id",catalog3Id));
    }
}
