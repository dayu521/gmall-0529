package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.manager.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    /**
     * 按照三级分类id查出平台属性的名和值
     * @param catalog3Id
     * @return
     */
    List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id);

    List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(@Param("valueIds") List<Integer> valueIds);
}
