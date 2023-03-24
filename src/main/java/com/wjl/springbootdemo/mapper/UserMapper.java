package com.wjl.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjl.springbootdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/3/24
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过nickName查询
     * @param nickName
     * @return
     */
    @Select("select * from user where nickName = #{nickName}")
    User findByNickName(@Param("nickName") String nickName);
}
