package com.jvm.jvmlearn.jmm;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class SourceOne{
    private ReentrantLock reentrantLock = new ReentrantLock();
    public void lock(){
        reentrantLock.lock();
        System.out.println(Thread.currentThread().getName() +  " 获得资源A的锁");
    }
    public void unlock(){
        System.out.println(Thread.currentThread().getName() +  " 释放资源A的锁");
        reentrantLock.unlock();
    }
}
class SourceTwo{
    private ReentrantLock reentrantLock = new ReentrantLock();
    public void lock(){
        reentrantLock.lock();
        System.out.println(Thread.currentThread().getName() +  " 获得资源B的锁");
    }
    public void unlock(){
        System.out.println(Thread.currentThread().getName() +  " 释放资源B的锁");
        reentrantLock.unlock();
    }
}

/**
 * 死锁案例
 * jps 获取Tomcat进程
 * jstack <pid>
 *    Found one Java-level deadlock:
 */
public class DeadLockTest {
    public static void main(String[] args) {
        SourceOne sourceOne = new SourceOne();
        SourceTwo sourceTwo = new SourceTwo();
        new Thread(()->{
            sourceOne.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sourceTwo.lock();
            sourceTwo.unlock();
            sourceOne.unlock();
        },"AA").start();
        new Thread(()->{
            sourceTwo.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sourceOne.lock();
            sourceOne.unlock();
            sourceTwo.unlock();
        },"BB").start();
    }
}
