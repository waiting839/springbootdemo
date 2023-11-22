package com.wjl.springbootdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjl.springbootdemo.entity.User;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/3/24
 */
public interface UserService extends IService<User> {

    /**
     * 通过nickName查询
     * @param nickName
     * @return
     */
    User findByNickName(String nickName);

    /**
     * 更新
     * @param user
     * @return
     */
    User update(User user);
}
