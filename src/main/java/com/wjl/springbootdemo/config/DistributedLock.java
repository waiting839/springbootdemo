package com.wjl.springbootdemo.config;

import java.lang.annotation.*;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/11/22
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DistributedLock {

    /**
     * 分布式锁key
     * @return
     */
    String key() default "";

    /**
     * 释放时间
     * @return
     */
    int leaseTime() default 10;

    /**
     * 是否自动释放
     * @return
     */
    boolean autoRelease() default true;

    /**
     * 错误信息
     * @return
     */
    String errorDesc() default "系统正在处理，请稍后提交";

    /**
     * 等待时间
     * @return
     */
    int waitTime() default 1;
}
