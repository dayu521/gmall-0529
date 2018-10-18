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

    /**
     * 按照平台属性值集合找到这些值所在的所有平台属性名字和值
     * @param valueIds
     * @return
     */
    List<BaseAttrInfo> getBaseAttrInfoByAttrValueIdIn(@Param("ids") List<Integer> valueIds);
}
