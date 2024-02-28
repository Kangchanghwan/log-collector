package com.itech.log.logcollector;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;


@Component
@EnableAsync(proxyTargetClass = true)
public class CollectorExecutor {

    /**
     * 비동기 방식으로 수집기를 실행합니다.
     *
     * @param collector 수집기
     * @param data      데이터 객체
     */
    @Async("collectorAsyncExecutor")
    public <D> void asyncExecute(Collector<D> collector, D data) {
        execute(collector, data);
    }

    /**
     * 동기 방식으로 수집기를 실행합니다.
     *
     * @param collector 수집기
     * @param data      데이터 객체
     */
    public <D> void execute(Collector<D> collector, D data) {
        collector.collect(data);
    }
}
