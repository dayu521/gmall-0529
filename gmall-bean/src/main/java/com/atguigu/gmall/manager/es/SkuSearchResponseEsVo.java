package com.atguigu.gmall.manager.es;

import com.atguigu.gmall.manager.BaseAttrInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SkuSearchResponseEsVo implements Serializable{

    private List<SkuInfoEsVo> skuInfo;//所有页面显示的数据

    private List<BaseAttrInfo> baseAttrInfos;//平台属性筛选项

    private Integer total;//总记录数


}
