package com.atguigu.gmall.user;

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
}
