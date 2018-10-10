package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.BaseAttrValue;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id) {
        return baseAttrInfoMapper.selectList(new QueryWrapper<BaseAttrInfo>().eq("catalog3_id",catalog3Id));
    }

    @Override
    public List<BaseAttrValue> getBaseAttrValueByAttrId(Integer baseAttrInfoId) {
        return baseAttrValueMapper.selectList(new QueryWrapper<BaseAttrValue>().eq("attr_id",baseAttrInfoId));
    }

    //

    /**
     * 大型的保存删除修改方法
     * 1、必须开启基于注解的事务功能 @EnableTransactionManagement
     * 2、在方法上  @Transactional
     * @param baseAttrInfo
     */
    @Transactional
    @Override
    public void saveOrUpdateBaseInfo(BaseAttrInfo baseAttrInfo) {
        log.info("准备修改的BaseAttrInfo信息是：{},没有id？{}",baseAttrInfo,baseAttrInfo.getId());
        //判断是修改还是保存还是里面的删除....
        if(baseAttrInfo.getId()!=null){
            //1、修改基本属性名
            baseAttrInfoMapper.updateById(baseAttrInfo);
            //2、属性的属性值操作
            List<BaseAttrValue> attrValues = baseAttrInfo.getAttrValues();
            List<Integer> ids = new ArrayList<>();
            //2.1）、删除没有提交过来的数据  1
            for (BaseAttrValue attrValue : attrValues) {
                Integer id = attrValue.getId();
                if(id!=null){
                    ids.add(id);
                }
            }
            //delete * from baseattrvalue where id not in(1) and attr_id = baseAttrInfo.getId()
            baseAttrValueMapper.delete(new QueryWrapper<BaseAttrValue>()
                    .notIn("id",ids).eq("attr_id",baseAttrInfo.getId()));

            for (BaseAttrValue attrValue : attrValues) {
                //2.2）、提交过来的数据，如果有id就是修改
                if(attrValue.getId()!=null){
                    baseAttrValueMapper.updateById(attrValue);
                }else {
                    //2.3）、提交过来的数据，如果没有id就是新增
                    attrValue.setAttrId(baseAttrInfo.getId());
                    baseAttrValueMapper.insert(attrValue);
                }

            }
        }else{
            //基本属性没有id，那么就是新增
            //1、先将新的基本属性插入到数据库，我们要使用到它的自增id来设置他里面的baseAttrValues的attrId值
            //特别注意：由于新增的平台属性baseAttrInfo，页面并没有提交他所在的三级分类id，我们需要在ajax提交的时候加上三级分类信息
            //   1）修改ajax请求给json对象加上三级分类属性的值   2）、给vo字段加上三级分类的值（catalog3Id）
            baseAttrInfoMapper.insert(baseAttrInfo);
            //2、获取到刚才插入的baseAttrInfo的id
            Integer baseAttrInfoId = baseAttrInfo.getId();
            //3、把每一个baseAttrValue的attrId设置好，然后插入即可
            for (BaseAttrValue baseAttrValue : baseAttrInfo.getAttrValues()) {
                baseAttrValue.setAttrId(baseAttrInfoId);
                baseAttrValueMapper.insert(baseAttrValue);
            }

        }



    }
}
