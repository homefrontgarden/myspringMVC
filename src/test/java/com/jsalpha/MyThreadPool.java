package com.jsalpha;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 线程池实现
 * Created by gameloft9 on 2019/4/24.
 */
public class MyThreadPool implements ThreadPool {

    // 线程名称前缀
    private static final String THREAD_NAME_PREFIX = "my_thread_";

    // 线程池线程个数
    private int count = -1;

    // 线程优先级
    private int prior = Thread.NORM_PRIORITY;

    // 是否守护线程
    private boolean daemon = false;

    // 临界资源锁
    private final Object poolLock = new Object();

    // 是否线程池关闭
    private boolean shutdown = false;

    // 是否初始化完成(避免还未初始化完成就调用execute方法)
    private boolean initialized = false;

    private HashSet<WorkerThread> workers = new HashSet<WorkerThread>();

    private LinkedList<WorkerThread> idleWorkers = new LinkedList<WorkerThread>();

    private LinkedList<WorkerThread> busyWorkers = new LinkedList<WorkerThread>();
    public MyThreadPool() {
    }

    public MyThreadPool(int count, int prior) {
        this.count = count;
        this.prior = prior;
    }

    public int getPoolSize() {
        return count;
    }
    private HashSet<WorkerThread> createWorkers(int count, int prior){
        ThreadGroup group = new ThreadGroup(THREAD_NAME_PREFIX+"_group");
        for(int index = 0 ; index < count ; index++){
            workers.add(new WorkerThread(this,group,THREAD_NAME_PREFIX+index,prior,true));
        }
        return workers;
    }
    public void init() {
        synchronized (poolLock){
            if (!initialized) {
                Iterator<WorkerThread> it = createWorkers(count, prior).iterator();
                while (it.hasNext()) {
                    WorkerThread worker = it.next();
                    worker.start();

                    if(!idleWorkers.contains(worker)){
                        idleWorkers.add(worker);
                    }
                }

                initialized = true;
            }

            poolLock.notifyAll();
        }
    }

    public void shutdown() {
        synchronized (poolLock) {
            shutdown = true;

            if (workers == null){
                return;
            }

            Iterator<WorkerThread> it = workers.iterator();
            while (it.hasNext()) {
                WorkerThread worker = it.next();
                worker.shutDown();

                idleWorkers.remove(worker);
                busyWorkers.remove(worker);
            }

            poolLock.notifyAll();
        }
    }
    public void execute(Runnable runnable) {
        if (null == runnable) {
            return;
        }

        synchronized (poolLock) {
            while ((idleWorkers.size() < 1) && initialized && !shutdown) {
                try {
                    System.out.println("暂无可用线程，等待500ms...");
                    poolLock.wait(500);
                } catch (InterruptedException e) {
                    System.out.println("异常");
                }
            }

            if (initialized && !shutdown) {
                WorkerThread worker = idleWorkers.removeFirst();
                busyWorkers.add(worker);
                worker.run(runnable);
            }

            poolLock.notifyAll();
        }
    }
    public void makeIdle(WorkerThread worker){
        synchronized (busyWorkers){
            int index = busyWorkers.indexOf(worker);
            if(-1 != index){
                idleWorkers.add(busyWorkers.get(index));
            }
        }

    }
}
