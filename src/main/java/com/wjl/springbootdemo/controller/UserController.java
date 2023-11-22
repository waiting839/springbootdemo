package com.wjl.springbootdemo.controller;

import com.wjl.springbootdemo.entity.User;
import com.wjl.springbootdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/3/24
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping("user/save")
    public User save() {
        User user = new User();
        user.setNickName("nick");
        user.setRealName("jack");
        userService.save(user);
        return user;
    }

    @RequestMapping("user/delete")
    public void delete() {
        userService.removeById("1");
    }

    @RequestMapping("user/findByNickName")
    public User findByNickName(String nickName) {
        return userService.findByNickName(nickName);
    }

    @RequestMapping("user/update")
    public User update() {
        return userService.update(new User());
    }
}
