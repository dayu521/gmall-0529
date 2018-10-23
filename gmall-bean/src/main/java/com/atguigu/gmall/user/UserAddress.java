package com.atguigu.gmall.user;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

/**
 * 用户收货地址
 */
@Data
public class UserAddress extends SuperBean {

    //id  user_address        user_id  consignee  phone_num  is_default
    private String  userAddress;
    private Integer userId;
    private String consignee;//收货人
    private String phoneNum;
    private String isDefault;//是否默认  1默认  0非默认

}
