package com.wjl.springbootdemo.controller;

import com.wjl.springbootdemo.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吴嘉烺
 * @description controller
 * @date 2023/4/13
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @RequestMapping("test/testThreadPoolMonitor")
    public void testThreadPoolMonitor(int n) {
        testService.testThreadPoolMonitor(n);
    }

    @RequestMapping("test/testThreadPoolExecutor")
    public void testThreadPoolExecutor(int n) {
        testService.testThreadPoolExecutor(n);
    }
}
