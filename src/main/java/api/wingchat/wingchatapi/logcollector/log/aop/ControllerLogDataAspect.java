package api.wingchat.wingchatapi.logcollector.log.aop;

import api.wingchat.wingchatapi.logcollector.log.AopLogProcessor;
import api.wingchat.wingchatapi.logcollector.log.model.AopLogConfig;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLogDataAspect {

    @Resource
    private AopLogProcessor aopLogProcessor;
    private static final AopLogConfig CONFIG;

    static {
        CONFIG = new AopLogConfig();
        CONFIG.setStackTraceOnErr(true);
        CONFIG.setTag("controller");
        CONFIG.setRespBody(true);
        CONFIG.setHeaders(new String[]{"content-type", "Cookie"});
    }
    /**
     * 해당 패키지 내에 있는 모든 컨트롤러에 대해 적용됩니다.
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.web.bind.annotation.RestController)")
    public void allController() {
        //ig
    }

    // 请使用环绕通知 @Around()
    @Around("allController()")
    public Object note(ProceedingJoinPoint point) throws Throwable {
        return aopLogProcessor.proceed(CONFIG, point);
    }
}