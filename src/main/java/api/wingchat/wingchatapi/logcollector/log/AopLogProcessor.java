package api.wingchat.wingchatapi.logcollector.log;

import api.wingchat.wingchatapi.logcollector.log.model.AopLogConfig;
import api.wingchat.wingchatapi.logcollector.log.model.LogData;
import api.wingchat.wingchatapi.logcollector.log.model.NothingCollector;
import api.wingchat.wingchatapi.logcollector.log.util.DataExtractor;
import api.wingchat.wingchatapi.logcollector.log.util.SpringElSupporter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


@Component
public class AopLogProcessor {

    private final ApplicationContext applicationContext;
    private final CollectorExecutor collectorExecutor;
    private final LogCollector logCollector;
    private final SpringElSupporter elSupporter = new SpringElSupporter();
    private final Map<Class<? extends LogCollector>, LogCollector> collectors = new HashMap<>();
    private final String appName;

    public AopLogProcessor(@Autowired ApplicationContext applicationContext,
                           @Autowired CollectorExecutor collectorExecutor,
                           @Autowired LogCollector logCollector) {
        this.applicationContext = applicationContext;
        this.collectorExecutor = collectorExecutor;
        this.logCollector = logCollector;
        this.appName = getAppName(applicationContext);
    }

    public String getAppName(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String name = environment.getProperty("spring.application.name");
        if (name != null) {
            return name;
        }
        if (applicationContext.getId() != null) {
            return applicationContext.getId();
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if ("main".equals(stackTraceElement.getMethodName())) {
                return stackTraceElement.getFileName();
            }
        }
        return applicationContext.getApplicationName();
    }

    public String getAppName() {
        return appName;
    }

    /**
     * 로그 데이터 시점을 처리합니다.
     *
     * @param config AopLog 설정
     * @param point  절단점 객체
     * @return 실행 결과 반환
     * @throws Throwable AOP 내부의 예외는 외부에서 처리하기 위해 그대로 던짐
     */
    public Object proceed(AopLogConfig config, ProceedingJoinPoint point) throws Throwable {
        try {
            LogData.removeCurrent();
            LogData data = LogData.getCurrent();
            return proceed(config, data, point);
        } finally {
            LogData.removeCurrent();
        }
    }

    /**
     * 실행할 수집기를 선택합니다.
     */
    private LogCollector selectLogCollector(Class<? extends LogCollector> clz) {
        if (clz == NothingCollector.class || clz == null) {
            return logCollector;
        } else {
            LogCollector collector;
            try {
                collector = applicationContext.getBean(clz);
            } catch (Exception e) {
                collector = collectors.get(clz);
                if (collector == null) {
                    collector = BeanUtils.instantiateClass(clz);
                    collectors.put(clz, collector);
                }
            }
            return collector;
        }
    }


    /**
     * 메서드 실행 처리 기록
     *
     * @param aopLog 어노테이션 객체
     * @param data   로그 데이터
     * @param point  절단점 객체
     * @return 실행 결과 반환
     * @throws Throwable AOP 내부의 예외는 외부에서 처리하기 위해 그대로 던질 것이다
     */
    private Object proceed(AopLogConfig aopLog, LogData data, ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        boolean success = false;
        try {
            result = point.proceed();
            success = true;
            return result;
        } catch (Throwable throwable) {
            if (aopLog.isStackTraceOnErr()) {
                try (StringWriter sw = new StringWriter(); PrintWriter writer = new PrintWriter(sw, true)) {
                    String output = getExceptionStackTraceInfo(throwable);
                    writer.println(output);
                    LogData.info("Fail : {}" , sw);
                }
            }
            throw throwable;
        } finally {
            if (!aopLog.isLogOnErr() || !data.isSuccess()) {
                data.setAppName(appName);
                data.setCostTime(System.currentTimeMillis() - data.getLogDate().getTime());
                MethodSignature signature = (MethodSignature) point.getSignature();
                data.setTag(elSupporter.getByExpression(signature.getMethod(), point.getTarget(), point.getArgs(), aopLog.getTag()).toString());
                data.setMethod(signature.getDeclaringTypeName() + "#" + signature.getName());
                DataExtractor.logHttpRequest(data, aopLog.getHeaders());
                if (aopLog.isArgs()) {
                    data.setArgs(DataExtractor.getArgs(signature.getParameterNames(), point.getArgs()));
                }
                if (aopLog.isRespBody()) {
                    data.setRespBody(DataExtractor.getResult(result));
                }
                data.setSuccess(success);
                LogData.setCurrent(data);
                if (aopLog.isAsyncMode()) {
                    collectorExecutor.asyncExecute(selectLogCollector(aopLog.getCollector()), LogData.getCurrent());
                } else {
                    collectorExecutor.execute(selectLogCollector(aopLog.getCollector()), LogData.getCurrent());
                }
            }
        }
    }

    private static String getExceptionStackTraceInfo(Throwable throwable) {
        StackTraceElement element = throwable.getStackTrace()[0];
        String output = String.format("'%s' occurred at %s.%s(%s:%d)",
            throwable.getClass().getName(),
            element.getClassName(),
            element.getMethodName(),
            element.getFileName(),
            element.getLineNumber());
        if(throwable.getMessage() != null) {
            output += " message is " + throwable.getMessage();
        }
        return output;
    }

}
