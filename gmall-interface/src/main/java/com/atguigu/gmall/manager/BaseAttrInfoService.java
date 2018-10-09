package com.atguigu.gmall.manager;

import java.util.List;

/**
 * 平台属性
 */
public interface BaseAttrInfoService {

    /**
     * 获取三级分类下的平台属性名
     * @param catalog3Id
     * @return
     */
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id);

    /**
     * 获取某个平台属性的所有属性值
     * @param baseAttrInfoId
     * @return
     */
    public List<BaseAttrValue> getBaseAttrValueByAttrId(Integer baseAttrInfoId);
}
