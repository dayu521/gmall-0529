package com.atguigu.gmall.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String id;
    private String userName;
}
