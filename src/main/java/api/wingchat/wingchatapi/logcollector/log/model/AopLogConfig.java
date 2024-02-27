package api.wingchat.wingchatapi.logcollector.log.model;

import api.wingchat.wingchatapi.logcollector.log.LogCollector;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;


@Getter
@Setter
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
