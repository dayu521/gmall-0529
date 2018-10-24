package com.atguigu.gmall.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pay.PaymentInfo;
import com.atguigu.gmall.pay.PaymentService;
import com.atguigu.gmall.payment.mapper.PaymentInfoMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentInfoMapper paymentInfoMapper;


    /**
     * 保存支付流水的初始化信息
     * @param paymentInfo
     */
    @Async
    @Override
    public void createAliTrade(PaymentInfo paymentInfo) {

        paymentInfoMapper.insert(paymentInfo);
    }

    @Override
    public void updatePayement(PaymentInfo paymentInfo) {
        PaymentInfo update = new PaymentInfo();
        BeanUtils.copyProperties(paymentInfo,update);
        update.setOutTradeNo(null);

        PaymentInfo where = new PaymentInfo();
        where.setOutTradeNo(paymentInfo.getOutTradeNo());
        paymentInfoMapper.update(update,new UpdateWrapper<PaymentInfo>(where));
    }
}
