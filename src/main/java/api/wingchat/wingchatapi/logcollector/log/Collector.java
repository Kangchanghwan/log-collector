package api.wingchat.wingchatapi.logcollector.log;



@FunctionalInterface
public interface Collector<T> {

    void collect(T data);
}
