package com.atguigu.gmall.user;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

@Data
public class UserInfo extends SuperBean {

    private String loginName;
    private String nickName;
    private String passwd;
    private String name;
    private String phoneNum;
    private String email;
    private String headImg;
    private String userLevel;
}
