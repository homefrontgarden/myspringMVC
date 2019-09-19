package com.jsalpha;

/**
 * 线程池
 * Created by gameloft9 on 2019/4/24.
 */
public interface ThreadPool {

    int getPoolSize();

    void init();

    void shutdown();

    void execute(Runnable runnable);
}
