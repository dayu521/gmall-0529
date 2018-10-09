package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.manager.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 在mapper文件中映射这个方法
     * @param user
     * @return
     */
    public User getUserByHaha(User user);

}
