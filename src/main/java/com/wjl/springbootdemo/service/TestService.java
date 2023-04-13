package com.wjl.springbootdemo.service;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/4/13
 */
public interface TestService {

    /**
     * 测试线程池监视器
     * @param n 执行次数
     */
    void testThreadPoolMonitor(int n);

    /**
     * 测试自定义线程池
     * @param n 执行次数
     */
    void testThreadPoolExecutor(int n);
}
