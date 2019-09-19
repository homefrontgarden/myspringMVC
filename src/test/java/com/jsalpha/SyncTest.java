package com.jsalpha;

public class SyncTest implements Runnable {
    //共享资源变量
    static int count = 0;

    @Override
    public void run() {
        increaseCount();
    }

    private synchronized static void increaseCount() {

        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + count++);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SyncTest syncTest1 = new SyncTest();
        SyncTest syncTest2 = new SyncTest();
        Thread thread1 = new Thread(syncTest1, "thread1");
        Thread thread2 = new Thread(syncTest2, "thread2");
        thread1.start();
        thread2.start();
    }
    /**
     * 输出结果
     thread1:0
     thread1:1
     thread1:2
     thread1:3
     thread1:4
     thread2:5
     thread2:6
     thread2:7
     thread2:8
     thread2:9
     */
}
