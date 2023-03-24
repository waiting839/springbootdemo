package com.wjl.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/3/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("nickName")
    private String nickName;

    @TableField("realName")
    private String realName;

    @TableField(value = "createTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
