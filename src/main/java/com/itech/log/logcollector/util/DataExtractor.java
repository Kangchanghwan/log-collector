package com.itech.log.logcollector.util;

import com.itech.log.logcollector.model.LogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class DataExtractor {

  private static final Logger log = LoggerFactory.getLogger(DataExtractor.class);
  private static final String AND_REG = "&";
  private static final String EQUALS_REG = "=";

  private DataExtractor() {

  }

  /**
   * HttpServletRequest 객체를 가져옵니다.
   *
   * @return HttpServletRequest
   */
  public static HttpServletRequest getRequest() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attributes != null ? attributes.getRequest() : null;
  }

  /**
   * HttpServletResponse 객체를 가져옵니다.
   *
   * @return HttpServletResponse
   */
  public static HttpServletResponse getResponse() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attributes != null ? attributes.getResponse() : null;
  }

  /**
   * 요청 매개변수 내용을 가져옵니다.
   *
   * @param parameterNames 매개변수 이름 목록
   * @param args           매개변수 목록
   * @return 요청 매개변수 내용을 가져옵니다.
   */
  public static Object getArgs(String[] parameterNames, Object[] args) {
    Object target = null;
    if (args.length == 1) {
      target = args[0];
    } else {
      target = args;
    }
    if (target == null) {
      return null;
    }
    HttpServletRequest request = getRequest();
    if (request != null && request.getContentType() != null) {
      String contentType = request.getContentType();
      if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
        return target;
      }
    }
    return appletArgs(parameterNames, args);
  }


  /**
   * 프로그램 실행 결과 내용을 가져옵니다.
   *
   * @param resp 응답 객체
   * @return 프로그램 실행 결과 내용을 가져옵니다.
   */
  public static Object getResult(Object resp) {
    if (resp == null) {
      return null;
    }
    HttpServletResponse response = getResponse();
    if (response != null) {
      return resp;
    }
    return null;
  }

  /**
   * 프로그램 매개변수를 가져옵니다.
   *
   * @param parameterNames 매개변수 이름
   * @param args           매개변수 값
   * @return 프로그램 매개변수를 가져옵니다.
   */
  public static Object appletArgs(String[] parameterNames, Object[] args) {
    if (parameterNames == null || parameterNames.length == 0 || args == null || args.length == 0) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < parameterNames.length; i++) {
      String parameterValue = "";
      if (args[i] != null) {
        parameterValue = args[i].toString();
      }
      sb.append(parameterNames[i]).append(EQUALS_REG).append(parameterValue).append(AND_REG);
    }
    if (sb.lastIndexOf(AND_REG) != -1) {
      sb.deleteCharAt(sb.lastIndexOf(AND_REG));
    }
    return sb.toString();
  }


  /**
   * HttpServletRequest 정보를 추출합니다.
   *
   * @param data    logData 객체
   * @param headers 헤더
   */
  public static void logHttpRequest(LogData data, String[] headers) {
    HttpServletRequest request = getRequest();
    if (request != null) {
      data.setHost(request.getLocalAddr());
      data.setPort(request.getLocalPort());
      data.setClientIp(request.getRemoteAddr());
      data.setReqUrl(request.getRequestURL().toString());
      data.setHttpMethod(request.getMethod());
      Map<String, String> headersMap = new HashMap<>(8);
      for (String header : headers) {
        if (header != null && !header.trim().isEmpty()) {
          headersMap.put(header, request.getHeader(header));
        }
      }
      data.setHeaders(headersMap);
    }
  }

}
