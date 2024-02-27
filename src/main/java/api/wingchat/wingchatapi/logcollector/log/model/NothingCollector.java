package api.wingchat.wingchatapi.logcollector.log.model;


import api.wingchat.wingchatapi.logcollector.log.LogCollector;

public class NothingCollector implements LogCollector {
    @Override
    public void collect(LogData data) {
        // This is an empty collector will do nothing
    }
}
