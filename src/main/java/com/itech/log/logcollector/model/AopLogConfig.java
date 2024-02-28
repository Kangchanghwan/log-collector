package com.itech.log.logcollector.model;

import com.itech.log.logcollector.LogCollector;
import org.springframework.http.HttpHeaders;


public class AopLogConfig {
    /**
     * 오류가 발생했을 때만 로그를 기록합니다.
     */
    private boolean logOnErr;
    /**
     * 작업 태그(작업 분류)
     */
    private String tag;
    /**
     * 기록된 헤더들, 기본적으로는 content-type과 user-agent를 기록합니다.
     */
    private String[] headers;
    /**
     * AOP가 요청 매개 변수를 기록할지 결정합니다.
     */
    private boolean args;
    /**
     * AOP가 응답 본문을 기록할지 결정합니다.
     */
    private boolean respBody;
    /**
     * 예외가 발생했을 경우, AOP가 content에 예외 스택 정보를 추가할지 결정합니다.
     */
    private boolean stackTraceOnErr;
    /**
     * 비동기 모드로 로그를 수집합니다.
     */
    private boolean asyncMode;
    /**
     * 특정 수집기를 지정합니다.
     */
    private Class<? extends LogCollector> collector;

    public boolean isLogOnErr() {
        return logOnErr;
    }

    public void setLogOnErr(boolean logOnErr) {
        this.logOnErr = logOnErr;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public boolean isArgs() {
        return args;
    }

    public void setArgs(boolean args) {
        this.args = args;
    }

    public boolean isRespBody() {
        return respBody;
    }

    public void setRespBody(boolean respBody) {
        this.respBody = respBody;
    }

    public boolean isStackTraceOnErr() {
        return stackTraceOnErr;
    }

    public void setStackTraceOnErr(boolean stackTraceOnErr) {
        this.stackTraceOnErr = stackTraceOnErr;
    }

    public boolean isAsyncMode() {
        return asyncMode;
    }

    public void setAsyncMode(boolean asyncMode) {
        this.asyncMode = asyncMode;
    }

    public Class<? extends LogCollector> getCollector() {
        return collector;
    }

    public void setCollector(Class<? extends LogCollector> collector) {
        this.collector = collector;
    }

    /**
     * 기본값 설정
     * */
    public AopLogConfig() {
        this.logOnErr = false;
        this.tag = "undefined";
        this.headers = new String[]{HttpHeaders.USER_AGENT, HttpHeaders.CONTENT_TYPE};
        this.args = true;
        this.respBody = true;
        this.stackTraceOnErr = true;
        this.asyncMode = true;
        this.collector = NothingCollector.class;
    }

}
