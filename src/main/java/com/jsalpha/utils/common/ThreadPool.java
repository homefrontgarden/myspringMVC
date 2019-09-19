package com.jsalpha.utils.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 * 1.存在多个线程
 */
public class ThreadPool {

    private List<Thread> threads;
    private LinkedList<Thread> busyThreads;
    private LinkedList<Thread> freeThreads;
    public Thread getThread(Runnable runnable){
        Thread thread = freeThreads.poll();
        if(thread == null){
            thread = new Thread(runnable);
        }
        return thread;
    }

    public ThreadPool(int threadCount) {
        threads = new ArrayList();
        busyThreads = new LinkedList<>();
        freeThreads = new LinkedList<>();
        Thread thread;
        for(int index = 0;index < threadCount; index++){
            thread = new Thread();
            thread.setDaemon(true);
            threads.add(thread);
        }
        freeThreads.addAll(threads);
        new Thread(){
            @Override
            public void run() {
                Thread thread;
                while(null != freeThreads.peek()){
                    thread = freeThreads.poll();
                    if(thread.isAlive()){
                        busyThreads.offer(thread);
                    }
                    freeThreads.offer(thread);
                }

            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                Thread thread;
                while(null != busyThreads.peek()){
                    thread = busyThreads.poll();
                    if(!thread.isAlive()){
                        freeThreads.offer(thread);
                    }
                    busyThreads.offer(thread);
                }

            }
        }.start();
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

    public LinkedList<Thread> getBusyThreads() {
        return busyThreads;
    }

    public void setBusyThreads(LinkedList<Thread> busyThreads) {
        this.busyThreads = busyThreads;
    }

    public LinkedList<Thread> getFreeThreads() {
        return freeThreads;
    }

    public void setFreeThreads(LinkedList<Thread> freeThreads) {
        this.freeThreads = freeThreads;
    }

    public static void main(String[] args){
        ThreadPool threadPool = new ThreadPool(15);
//        Thread t = threadPool.getThread(()->{
//            for(int i = 0; i < 10; i++){
//                System.out.println(i);
//            }
//        });
        Thread t = new Thread(()->{
            for(int i = 0; i < 10; i++){
                System.out.println(i);
            }
        });
        t.start();
        System.out.println(threadPool.getFreeThreads().size());
        System.out.println(threadPool.getBusyThreads().size());
    }
}
class MyThread extends Thread{


//    public void setRunnable(Runnable target){
//        super.target = target;
//    }
    public MyThread() {
    }

    public MyThread(Runnable target) {
        super(target);
    }

    public MyThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public MyThread(String name) {
        super(name);
    }

    public MyThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public MyThread(Runnable target, String name) {
        super(target, name);
    }

    public MyThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public MyThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }
}