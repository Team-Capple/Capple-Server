package com.server.capple.global.utils.distributedLock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK-";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.server.capple.global.utils.distributedLock.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), annotation.value());
        RLock rLock = redissonClient.getLock(key);

        int tryCount = 0;
        try {
            boolean isLocked = false;
            while (!isLocked) {
                isLocked = rLock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit());
                if (!isLocked) {
                    Thread.sleep((long) (Math.random() * 100));
                }
                tryCount++;
                if (tryCount >= annotation.retryCount()) {
                    return false;
                }
            }
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    private Object getDynamicValue(String[] parameterNames, Object[] args, String value) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return (new SpelExpressionParser()).parseExpression(value).getValue(context);
    }
}
