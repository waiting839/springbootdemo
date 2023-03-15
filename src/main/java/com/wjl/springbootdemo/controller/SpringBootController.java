package com.wjl.springbootdemo.controller;

import com.wjl.springbootdemo.config.Limit;
import com.wjl.springbootdemo.enums.LimitType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/3/14
 */
@RestController()
public class SpringBootController {

    private static final AtomicInteger ATOMIC_INTEGER_NULL = new AtomicInteger();
    private static final AtomicInteger ATOMIC_INTEGER_CUSTOMER = new AtomicInteger();
    private static final AtomicInteger ATOMIC_INTEGER_IP = new AtomicInteger();

    @RequestMapping("springBoot/say")
    public String say() {
        return "Hello SpringBoot!";
    }

    @Limit(key = "testLimiterByNull", period = 10, count = 3)
    @RequestMapping("springBoot/testLimiterByNull")
    public int testLimiterByNull() {
        return ATOMIC_INTEGER_NULL.incrementAndGet();
    }

    @Limit(key = "testLimiterByCustomer", period = 10, count = 3, limitType = LimitType.CUSTOMER)
    @RequestMapping("springBoot/testLimiterByCustomer")
    public int testLimiterByCustomer() {
        return ATOMIC_INTEGER_CUSTOMER.incrementAndGet();
    }

    @Limit(key = "testLimiterByIP", period = 10, count = 3, limitType = LimitType.IP)
    @RequestMapping("springBoot/testLimiterByIP")
    public int testLimiterByIP() {
        return ATOMIC_INTEGER_IP.incrementAndGet();
    }
}
