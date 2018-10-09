package com.atguigu.gmall.manager;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    @TableLogic  //表示这个字段是一个逻辑删除字段
    private Integer delFlag; // del_flag  我们写属性要么和数据库字段一样，要么符合驼峰命名规则，就能一切自动



}
