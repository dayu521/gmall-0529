package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.BaseAttrValue;
import com.atguigu.gmall.manager.vo.BaseAttrInfoAndValueVO;
import com.atguigu.gmall.manager.vo.BaseAttrValueVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/attr")
@Controller
public class AttrManagerController {


    @Reference
    BaseAttrInfoService baseAttrInfoService;


    /**
     *
     * vo：收集页面的值封装为指定对象的
     * {
     *  "id":"1",
     *  "attrName":"励志读物",
     *  "attrValues":[
     *      {"id":2,"valueName":"111","attrId":"1"},
     *      {"id":"","valueName":"对萨达撒","attrId":1}
     *      ]
     * }
     *
     * @RequestBody 将请求体中的数据封装成指定BaseAttrInfoAndValueVO的对象
     * 1、如果请求体中是json字符串，直接可以将json转为对象
     * BaseAttrInfoAndValueVO vo = json.parse(jsonStr)
     * 2、 k=v&k=v&k=v  URL编码的
     * 3、页面如果直接提交一个原生的json
     * 这是一个综合的方法
     */
    @ResponseBody
    @RequestMapping("/updates")
    public String saveOrUpdateOrDeleteAttrInfoAndValue(
            @RequestBody  BaseAttrInfoAndValueVO baseAttrInfoAndValueVO){

        log.info("页面提交来的数据：{}",baseAttrInfoAndValueVO);

        //1、修改还是添加
//        if(baseAttrInfoAndValueVO.getId()!=null){  不用在controller中判断是修改还是添加，我们直接在service里面全部处理
        //controller这里负责整理好数据传递过去即可
            //修改,检查新提交的属性名不能是空串.....
            //1、修改基本属性名
            //2、修改这个属性对应的所有的值
            BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
            //将vo中的所有属性复制到bean中
            BeanUtils.copyProperties(baseAttrInfoAndValueVO,baseAttrInfo);

            List<BaseAttrValue> values = new ArrayList<>();
            //遍历页面上的数据vo
            for (BaseAttrValueVo baseAttrValueVo : baseAttrInfoAndValueVO.getAttrValues()) {
                //将这个vo里面的数据封装到BaseAttrValue这个对象
                BaseAttrValue baseAttrValue = new BaseAttrValue();
                BeanUtils.copyProperties(baseAttrValueVo,baseAttrValue);
                values.add(baseAttrValue);
            };
            //将复制好的list设置在attrInfo中
            baseAttrInfo.setAttrValues(values);
            log.info("复制属性完成：{}",baseAttrInfo);

            //以上数据整理完成，调用远程service进行处理
            baseAttrInfoService.saveOrUpdateBaseInfo(baseAttrInfo);
//        }else{
            //2、基本属性没有id是添加  我们希望无论是修改还是添加都是用baseAttrInfoService.saveOrUpdateBaseInfo(baseAttrInfo);一个方法
//        }
        return "ok";
    }



    /**
     *获取某个平台属性下的所有属性值
     * @param id  属性名的id
     * @return
     *
     * Error resolving template "attr/value/1", t  要找的页面模板不存在
     */
    @ResponseBody
    @RequestMapping("/value/{id}")
    public List<BaseAttrValue> getBaseAttrValueByAttrId(@PathVariable("id") Integer id){


        return baseAttrInfoService.getBaseAttrValueByAttrId(id);
    }

    /**
     * 去平台属性列表页面
     * 所有的去页面的请求，都加上html后缀
     * @return
     */
    @RequestMapping("/listPage.html")
    public String toAttrListPage(){

        return "attr/attrListPage";
    }


}
