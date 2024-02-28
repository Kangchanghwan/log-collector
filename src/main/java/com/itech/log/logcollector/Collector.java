package com.itech.log.logcollector;



@FunctionalInterface
public interface Collector<T> {

    void collect(T data);
}
