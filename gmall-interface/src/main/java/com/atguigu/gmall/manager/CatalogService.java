package com.atguigu.gmall.manager;

import java.util.List;

/**
 * 操作分类的接口
 */
public interface CatalogService {

    /**
     * 获取所有的一级分类
     * @return
     */
    public List<BaseCatalog1> getAllBaseCatalog1();

    /**
     * 获取二级分类
     * @param catalog1Id   一级分类的id
     * @return
     */
    public List<BaseCatalog2> getBaseCatalog2ByC1id(Integer catalog1Id);

    /**
     * 获取三级分类
     * @param catalog2Id   二级分类的id
     * @return
     */
    public List<BaseCatalog3> getBaseCatalog3ByC2id(Integer catalog2Id);
}
