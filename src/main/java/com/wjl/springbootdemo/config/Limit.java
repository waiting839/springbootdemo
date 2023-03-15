package com.wjl.springbootdemo.config;

import com.wjl.springbootdemo.enums.LimitType;

import java.lang.annotation.*;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/3/15
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

    /**
     * 名字
     * @return
     */
    String name() default "";

    /**
     * key
     * @return
     */
    String key() default "";

    /**
     * key的前缀
     * @return
     */
    String prefix() default "";

    /**
     * 给定的时间范围（单位秒）
     * @return
     */
    int period();

    /**
     * 一定时间内最多访问次数
     * @return
     */
    int count();

    /**
     * 限流的类型（用户自定义key 或者 请求IP）
     * @return
     */
    LimitType limitType() default LimitType.CUSTOMER;
}
