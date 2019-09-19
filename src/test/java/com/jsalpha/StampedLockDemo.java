package com.jsalpha;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo {
    Map<String,String> map = new HashMap<>();
    private StampedLock lock = new StampedLock();
    public static void main(String[] args) throws InterruptedException {
        StampedLockDemo s = new StampedLockDemo();
        System.out.println(s.put("dengjingsi","dengjingsi"));
        System.out.println(s.put("dengjing","dengjing"));
        new Thread(()->{
                s.readWithOptimisticLock("dengjingsi");
        }).start();
        new Thread(()->{
                s.readWithOptimisticLock("dengjingsi");
        }).start();
        Thread.sleep(1000);
        System.out.println(s.put("dengjingsi","dengjing"));
    }
    public long put(String key, String value){
        long stamp = lock.writeLock();
        try {
            map.put(key, value);
        } finally {
            lock.unlockWrite(stamp);
        }
        return stamp;
    }
    public String readWithOptimisticLock(String key) {
        long stamp = lock.tryOptimisticRead();
        String value = map.get(key);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!lock.validate(stamp)) {
            System.out.println("进来了！");
            stamp = lock.readLock();
            try {
                return map.get(key);
            } finally {
                lock.unlock(stamp);
            }
        }
        return value;
    }
    public String get(String key) throws InterruptedException {
        long stamp = lock.readLock();
//        Thread.sleep(1000);
        System.out.println(stamp);
        try {
            return map.get(key);
        } finally {
            lock.unlockRead(stamp);
        }
    }

}
