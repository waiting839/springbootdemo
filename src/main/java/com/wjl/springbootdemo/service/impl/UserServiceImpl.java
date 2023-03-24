package com.wjl.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjl.springbootdemo.entity.User;
import com.wjl.springbootdemo.mapper.UserMapper;
import com.wjl.springbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/3/24
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByNickName(String nickName) {
        return userMapper.findByNickName(nickName);
    }
}
