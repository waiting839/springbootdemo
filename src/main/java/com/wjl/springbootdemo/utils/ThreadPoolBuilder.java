package com.wjl.springbootdemo.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/4/13
 */
@Slf4j
public class ThreadPoolBuilder {
    private static ThreadPoolExecutor threadPoolMonitor = new ThreadPoolMonitor(
            3, 10,
            5, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder()
                    .setNameFormat("wjl-threadPoolMonitor-%d")
                    .setUncaughtExceptionHandler((thread, throwable) -> log.error("ThreadPool {} got exception", thread, throwable))
                    .build());

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            3, 10,
            5, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder()
                    .setNameFormat("wjl-threadPoolExecutor-%d")
                    .build());

    public static ThreadPoolExecutor getThreadPoolMonitor() {
        return threadPoolMonitor;
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
