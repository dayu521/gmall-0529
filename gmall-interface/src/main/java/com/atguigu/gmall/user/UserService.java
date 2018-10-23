package com.atguigu.gmall.user;

import java.util.List;

public interface UserService {

    /**
     * 获取用户
     * @param id
     * @return
     */
    public User getUser(String id);

    /**
     * 购买电影
     * @param uid  用户id
     * @param mid  电影id
     */
    public void buyMovie(String uid,String mid);

    /**
     * 获取用户的收货地址列表
     * @param id  用户id
     * @return
     */
    List<UserAddress> getUserAddressesByUserId(int id);
}
