package com.wjl.springbootdemo.aspect;

import com.google.common.collect.ImmutableList;
import com.wjl.springbootdemo.config.Limit;
import com.wjl.springbootdemo.enums.LimitType;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author 吴嘉烺
 * @description
 * @date 2023/3/15
 */
@Aspect
@Configuration
public class LimitInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LimitInterceptor.class);

    private static final String UNKNOWN = "unknown";

    private final RedisTemplate<String, Serializable> limitRedisTemplate;

    @Autowired
    public LimitInterceptor(RedisTemplate<String, Serializable> limitRedisTemplate) {
        this.limitRedisTemplate = limitRedisTemplate;
    }

    @Around("execution(public * *(..)) && @annotation(com.wjl.springbootdemo.config.Limit)")
    public Object interceptor(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();

        String key;
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();

        /**
         * 根据限流类型获取不同的key，不传则用方法名
         */
        switch (limitType) {
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            default:
                key = method.getName().toUpperCase();
        }

        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitAnnotation.prefix(), key));
        try {
            String luaScript = buildLuaScript();
            RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
            Number count = limitRedisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
            logger.info("Access try count is {} for name={} and key = {}", count, name, key);
            if (count != null && count.intValue() <= limitCount) {
                return proceedingJoinPoint.proceed();
            } else {
                throw new RuntimeException("You have been dragged into the blacklist");
            }
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw new RuntimeException(t.getLocalizedMessage());
            }
            throw new RuntimeException("server exception");
        }
    }

    /**
     * 获取请求IP地址
     * @return
     */
    private String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 编写 redis lua 脚本
     * -- 获取调用脚本时传入的第一个key值（用作限流的 key）
     * local key = KEYS[1]
     * -- 获取调用脚本时传入的第一个参数值（限流大小）
     * local limit = tonumber(ARGV[1])
     *
     * -- 获取当前流量大小
     * local curentLimit = tonumber(redis.call('get', key) or "0")
     *
     * -- 是否超出限流
     * if curentLimit + 1 > limit then
     *     -- 返回(拒绝)
     *     return 0
     * else
     *     -- 没有超出 value + 1
     *     redis.call("INCRBY", key, 1)
     *     -- 设置过期时间
     *     redis.call("EXPIRE", key, 2)
     *     -- 返回(放行)
     *     return 1
     * end
     * @return
     */
    private String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        //如果调用超过最大值，ARGV[1]为前面传入参数第一个值limitCount
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c;");
        lua.append("\nend");
        //执行计算器自增
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        //如果是第一次调用限流，设置对应键和过期时间，ARGV[2]为传入的limitPeriod
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        lua.append("\nreturn c;");
        return lua.toString();
    }
}
