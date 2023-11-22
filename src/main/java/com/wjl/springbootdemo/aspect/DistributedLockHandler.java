package com.wjl.springbootdemo.aspect;

import com.wjl.springbootdemo.config.DistributedLock;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/11/22
 */
@Aspect
@Configuration
public class DistributedLockHandler {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockHandler.class);

    @Autowired
    private RedissonClient redissonClient;

    public DistributedLockHandler() {

    }

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockName = this.getRedisKey(joinPoint, distributedLock);
        int leaseTime = distributedLock.leaseTime();
        String errorDesc = distributedLock.errorDesc();
        int waitTime = distributedLock.waitTime();

        Object obj;
        try {
            boolean lock = this.redissonClient.getLock(lockName).tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (!lock) {
                throw new RuntimeException(errorDesc);
            }

            obj = joinPoint.proceed();
        } catch (Throwable t) {
            logger.error("执行业务方法异常", t);
            throw t;
        } finally {
            if (this.redissonClient.getLock(lockName).isHeldByCurrentThread()) {
                this.redissonClient.getLock(lockName).unlock();
            }
        }
        return obj;
    }

    private String getRedisKey(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        String key = distributedLock.key();
        Object[] parameterValues = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        if (StringUtils.isEmpty(key)) {
            if (parameterNames != null && parameterNames.length > 0) {
                StringBuffer sb = new StringBuffer();
                int i = 0;

                for (int len = parameterNames.length; i < len; ++i) {
                    sb.append(parameterNames[i]).append(" = ").append(parameterValues[i]);
                }

                key = sb.toString();
            } else {
                key = "redissionLock";
            }

            return key;
        } else {
            return key;
        }
    }
}
