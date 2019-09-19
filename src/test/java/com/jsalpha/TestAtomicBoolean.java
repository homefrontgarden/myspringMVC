package com.jsalpha;
import	java.util.concurrent.atomic.AtomicInteger;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestAtomicBoolean {
    int i = 0;
    public static void main(String [] args) {
        Count count = new Count(0);
        Runnable runnable = new Runnable(){
            AtomicBoolean atomic = new AtomicBoolean(true);
            @Override
            public void run() {
                for(int index = 50 ; count.getIndex() < index ;){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(atomic.compareAndSet(true,false ) && count.getIndex() < index) {
                        count.add();
                        atomic.set(true);
                    }
                }
            }
        };
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
class Count{
    private AtomicInteger count = new AtomicInteger();
    private int index;
    public void add(){
        System.out.println(count.incrementAndGet());
        System.out.println(++index);
    }

    public Count(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
class MyThread extends Thread {
    private int i;

    public MyThread(int i) {
        this.i = i;
    }

    AtomicBoolean b = new AtomicBoolean(true);
    public void run(){
        if(b.compareAndSet(true,false)){
            System.out.println(++i);
        }
    }
}
