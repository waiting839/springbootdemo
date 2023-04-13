package com.wjl.springbootdemo.service.impl;

import com.wjl.springbootdemo.service.TestService;
import com.wjl.springbootdemo.utils.ThreadPoolBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/4/13
 */
@Service("testService")
public class TestServiceImpl implements TestService {

    @Override
    public void testThreadPoolMonitor(int n) {
        CompletableFuture[] completableFutures = IntStream.range(1, n).mapToObj(i -> CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000 + i * 1000);
                System.out.println("current value is:" + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i == 3) {
                System.out.println("准备报错");
                throw new RuntimeException("报错");
            }
        }, ThreadPoolBuilder.getThreadPoolMonitor())).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(completableFutures).join();
    }

    @Override
    public void testThreadPoolExecutor(int n) {

    }
}
