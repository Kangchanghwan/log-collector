package com.itech.log.logcollector.config;

import com.itech.log.logcollector.LogCollector;
import com.itech.log.logcollector.model.NothingCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceAsyncTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@ComponentScan
@Configuration
public class AopLogAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AopLogAutoConfiguration.class);
    private final BeanFactory beanFactory;
    public AopLogAutoConfiguration(@Autowired BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 기본적으로 비어있는 수집기를 구성합니다.
     *
     * @return 기본적으로 비어있는 수집기가 구성됩니다.
     */
    @Bean
    @ConditionalOnMissingBean(value = LogCollector.class)
    public LogCollector nothingCollector() {
        return new NothingCollector();
    }

    @Bean(name = "collectorAsyncExecutor")
    public Executor collectorAsyncExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(processors / 2);
        executor.setMaxPoolSize(processors);
        executor.setQueueCapacity(256);
        executor.setThreadNamePrefix("collectorAsyncExecutor-");
        executor.setKeepAliveSeconds(60); // maxpoolsize로 인해 덤으로 더 돌아다니는 튜브는 60초 후에 수거해서 정리
        executor.setRejectedExecutionHandler((r, exec) -> log.error("collectorAsyncExecutor thread queue is full,activeCount:{},Subsequent collection tasks will be rejected,please check your LogCollector or config your Executor", exec.getActiveCount()));
        executor.initialize();
        return new LazyTraceAsyncTaskExecutor(beanFactory, executor, "collectorAsyncExecutor");
    }



}
