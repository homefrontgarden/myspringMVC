package com.jsalpha;

import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerThread extends Thread {

    // 锁，用于加入任务
    private final Object runLock = new Object();

    // 待跑的任务
    private Runnable runnable = null;

    // 指示线程是否shutdown
    private AtomicBoolean run = new AtomicBoolean(true);

    // 线程池
    MyThreadPool tp;

    public WorkerThread(MyThreadPool tp, ThreadGroup group, String name, int prior, boolean daemon) {
        super(group, name);
        this.tp = tp;

        setPriority(prior);
        setDaemon(daemon);
    }

    public void shutDown() {
        this.run.set(false);
    }

    public void run(Runnable newRunnable) {
        synchronized (runLock) {
            if (null != runnable) {
                throw new IllegalStateException("Already running a Runnable!");
            }

            if (!run.get()) {// 线程已经关闭，无法接受任务
                return;
            }

            this.runnable = newRunnable;

            runLock.notifyAll();
        }
    }

    public void run() {
        while (run.get()) {
            try {
                synchronized (runLock) {
                    if (runnable == null && run.get()) {
                        runLock.wait(500);
                    }

                    if (null != runnable) {
                        runnable.run();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("线程执行异常");
            } finally {
                synchronized (runLock) {
                    runnable = null;
                }

                tp.makeIdle(this);
            }
        }
    }
}
