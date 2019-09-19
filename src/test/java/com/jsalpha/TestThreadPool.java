package com.jsalpha;

public class TestThreadPool {
    public static void main(String[] args) throws Exception{
        ThreadPool tp = new MyThreadPool(1,5);
        System.out.println("线程池大小:{}"+tp.getPoolSize());
        tp.init();

        // 给一点时间让其初始化完成，这一个不要也可以，只是为了让结果更直观
        Thread.sleep(2000);

        // 2s后我们关闭线程池
        Thread term = new Thread(new Runnable() {
            public void run() {
                System.out.println("关闭线程池");
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException e){
                    System.out.println(e);
                }

                tp.shutdown();
            }
        });
        term.start();

        // 循环添加10个任务
        for(int i = 1;i<=10;i++){
            final int index = i;
            tp.execute(new Runnable() {
                public void run() {
                    try{
                        System.out.println("任务_{}开始执行..");
                        Thread.sleep(1000);
                        System.out.println("任务_{}执行成功。");
                    }catch(Exception e){
                        System.out.println("异常");
                    }

                }
            });
        }
    }
}
