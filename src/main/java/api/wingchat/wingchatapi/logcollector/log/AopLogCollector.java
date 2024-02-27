package api.wingchat.wingchatapi.logcollector.log;

import api.wingchat.wingchatapi.logcollector.log.model.LogData;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class AopLogCollector implements LogCollector {

  private final ObjectMapper objectMapper;

  @Override
  public void collect(LogData logData) {
    try {
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        if(logData.isSuccess()){
          log.info(objectMapper.writeValueAsString(logData));
        }else {
          log.error(objectMapper.writeValueAsString(logData));
        }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}