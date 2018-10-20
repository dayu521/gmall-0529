package com.atguigu.gmall.user;

public interface UserInfoService {

    /**
     * 登陆
     * @param userInfo  按照带来的账号密码查询用户的详情
     * @return 返回用户在数据库的详细信息
     */
    public UserInfo login(UserInfo userInfo);
}
