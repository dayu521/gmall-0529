package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.mapper.UserAddressMapper;
import com.atguigu.gmall.user.User;
import com.atguigu.gmall.user.UserAddress;
import com.atguigu.gmall.user.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserAddressMapper userAddressMapper;


    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public void buyMovie(String uid, String mid) {

    }

    @Override
    public List<UserAddress> getUserAddressesByUserId(int id) {
        List<UserAddress> userAddresses = userAddressMapper.selectList(new QueryWrapper<UserAddress>().eq("user_id", id));

        return userAddresses;
    }
}
