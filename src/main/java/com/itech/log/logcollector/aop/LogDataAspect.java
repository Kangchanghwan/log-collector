package com.itech.log.logcollector.aop;

import com.itech.log.logcollector.annontation.AopLog;
import com.itech.log.logcollector.model.AopLogConfig;
import com.itech.log.logcollector.AopLogProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;


@ComponentScan
@Component
@Aspect
@EnableAspectJAutoProxy(exposeProxy = true)
public final class LogDataAspect {

    private final AopLogProcessor aopLogProcessor;

    public LogDataAspect(@Autowired AopLogProcessor aopLogProcessor) {
        this.aopLogProcessor = aopLogProcessor;
    }

    /**
     * AopLog 어노테이션이 붙은 메소드에 대해 적용됩니다.
     */
    @Pointcut("@annotation(com.itech.log.logcollector.annontation.AopLog) || @within(com.itech.log.logcollector.annontation.AopLog)")
    public void aopLogPointCut() {
        //ig
    }

    @Around("aopLogPointCut()")
    public Object note(ProceedingJoinPoint point) throws Throwable {
        AopLogConfig config = new AopLogConfig();
        MethodSignature signature = (MethodSignature) point.getSignature();
        AopLog aopLog = signature.getMethod().getAnnotation(AopLog.class);
        if (aopLog == null) {
            aopLog = point.getTarget().getClass().getAnnotation(AopLog.class);
        }
        if (aopLog != null) {
            config.setLogOnErr(aopLog.logOnErr());
            config.setTag(aopLog.tag());
            config.setHeaders(aopLog.headers());
            config.setArgs(aopLog.args());
            config.setRespBody(aopLog.respBody());
            config.setStackTraceOnErr(aopLog.stackTraceOnErr());
            config.setAsyncMode(aopLog.asyncMode());
            config.setCollector(aopLog.collector());
        }
        return aopLogProcessor.proceed(config, point);
    }

}
