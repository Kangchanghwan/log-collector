package api.wingchat.wingchatapi.logcollector.log.model;


import api.wingchat.wingchatapi.logcollector.log.util.MessageFormatter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 로그 데이터 객체 (외부 생성자 제공하지 않음 쓰레드당 이 객체는 하나만 존재함)
 * 사용자 정의 로그 데이터 객체 (외부에 생성자를 제공하지 않으며, 각 쓰레드에는 이 객체가 하나씩만 존재)
 */
@Getter
@Setter
public class LogData {

  private LogData() {
  }

  /**
   * 쓰레드 로그데이터 객체
   */
  private static final ThreadLocal<LogData> LOG_DATA = new ThreadLocal<>();
  /**
   * 쓰레드 LinkedHashMap 객체, 주로 최종 content 필드를 누적하는데 사용
   */
  private static final ThreadLocal<LinkedHashMap<String, String>> CONTENT_BUILDER = new ThreadLocal<>();
  /**
   * 애플리케이션 이름
   */
  private String appName;
  /**
   * 호스트
   */
  private String host;
  /**
   * 포트 번호
   */
  private Integer port;
  /**
   * 클라이언트 요청 IP
   */
  private String clientIp;
  /**
   * 요청 URL 주소
   */
  private String reqUrl;
  /**
   * HTTP 요청 방식(method)
   */
  private String httpMethod;
  /**
   * 요청 헤더 정보 (선택으로 기록 가능)
   */
  private Object headers;
  /**
   * 작업 태그
   */
  private String tag;
  /**
   * 메소드 내용
   */
  private LinkedHashMap<String, String> content;
  /**
   * 작업 메소드
   */
  private String method;
  /**
   * 매개변수
   */
  private Object args;
  /**
   * 응답 본문
   */
  private Object respBody;
  /**
   * 작업 날짜(호출 날짜)
   */
  private Date logDate;
  /**
   * 작업 처리 시간
   */
  private long costTime;
  /**
   * 쓰레드 이름
   */
  private String threadName = Thread.currentThread().getName();
  /**
   * 쓰레드 Id
   */
  private long threadId = Thread.currentThread().getId();
  /**
   * 처리 상태 - 성공(true)/실패(false)
   */
  private boolean success = false;


  public Date getLogDate() {
    return logDate == null ? null : (Date) logDate.clone();
  }

  public void setLogDate(Date logDate) {
    if (logDate != null) {
      this.logDate = (Date) logDate.clone();
    }
  }


  /**
   * 현재 쓰레드에서 작업 로그 객체를 가져옴
   *
   * @return 현재 쓰레드에서 사용하는 LogData를 가져옵니다.
   */
  public static LogData getCurrent() {
    if (LOG_DATA.get() == null) {
      LogData logData = new LogData();
      logData.setLogDate(new Date());
      LinkedHashMap<String, String> linkedHashMap = CONTENT_BUILDER.get();
      if (linkedHashMap == null) {
        CONTENT_BUILDER.set(new LinkedHashMap<>());
      }
      LOG_DATA.set(logData);
    }
    return LOG_DATA.get();
  }

  /**
   * 현재 작업 로그 객체를 설정
   *
   * @param logData 객체
   */
  public static void setCurrent(LogData logData) {
    if (CONTENT_BUILDER.get() != null) {
      logData.setContent(CONTENT_BUILDER.get());
    }
    LOG_DATA.set(logData);
  }

  /**
   * 현재 쓰레드에서 작업 로그 객체를 제거
   */
  public static void removeCurrent() {
    CONTENT_BUILDER.remove();
    LOG_DATA.remove();
  }

   /**
   * 내용 기록 단계. 예: step("ABC--{}--EFG ", "D") -> ABC--D--EFG
   *
   * @param message 단계 템플릿. 이 메서드를 사용하여 각 단계를 기록합니다.
   * @param args         템플릿 매개변수
   */
  public static void info(String message, Object... args) {
    StackTraceElement line = Thread.currentThread().getStackTrace()[2];

    var sb = CONTENT_BUILDER.get();
    if (sb != null) {
      String className = line.getClassName();
      sb.put(
          MessageFormatter.format(
              "[{}][{}:{}]",
              LocalDateTime.now(),
              className.substring(className.lastIndexOf(".") + 1),
              line.getLineNumber()),
          MessageFormatter.format(
              message,
              args
          )
      );
      CONTENT_BUILDER.set(sb);
    }
  }


}
