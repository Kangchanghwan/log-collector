package com.itech.log.logcollector.model;


import com.itech.log.logcollector.LogCollector;

public class NothingCollector implements LogCollector {
    @Override
    public void collect(LogData data) {
        // This is an empty collector will do nothing
    }
}
