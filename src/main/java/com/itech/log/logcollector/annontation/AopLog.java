package com.itech.log.logcollector.annontation;


import com.itech.log.logcollector.model.NothingCollector;
import com.itech.log.logcollector.LogCollector;
import org.springframework.http.HttpHeaders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AopLog {

    /**
     * 예외가 발생한 경우에만 기록합니다.
     *
     * @return 예외가 발생한 경우에만 기록
     */
    boolean logOnErr() default false;

    /**
     * 작업 태그(작업 분류)
     */
    String tag() default "undefined";

    /**
     * 기록된 헤더들, 기본적으로는 content-type과 user-agent를 기록합니다.
     */
    String[] headers() default {HttpHeaders.USER_AGENT, HttpHeaders.CONTENT_TYPE};

    /**
     * AOP가 요청 매개변수를 기록할지 여부
     *
     * @return AOP가 요청 매개변수를 기록하는지 여부
     */
    boolean args() default true;

    /**
     * AOP가 응답 매개변수를 기록할지 여부
     *
     * @return AOP가 응답 매개변수를 기록하는지 여부
     */
    boolean respBody() default true;

    /**
     * 예외가 발생했을 때, AOP가 예외 스택 정보를 content에 추가할지 여부
     *
     * @return 예외가 발생했을 때, AOP가 예외 스택 정보를 content에 추가하는지 여부
     */
    boolean stackTraceOnErr() default false;

    /**
     * 비동기 모드로 로그를 수집합니다.
     *
     * @return 비동기 모드로 로그를 수집하는지 여부
     */
    boolean asyncMode() default true;

    /**
     * 특수한 수집기를 지정합니다.
     *
     * @return 특정 수집기 반환
     */
    Class<? extends LogCollector> collector() default NothingCollector.class;
}
