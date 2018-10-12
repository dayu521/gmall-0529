package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.manager.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id);
}
