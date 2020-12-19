package com.jvm.jvmlearn.jmm;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁:
 *   - 缺点: CPU浪费
 *   - 优点: 短时间任务避免切换
 */
public class SpinLockTest {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();
    public void lock(){
        Thread thread = Thread.currentThread();
        while (!atomicReference.compareAndSet(null,thread)){
        }
        System.out.println(Thread.currentThread().getName() + " \t coming");
    }
    public void unlock(){
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread,null);
        System.out.println(Thread.currentThread().getName() + "\t invoked unlock");
    }
    public static void main(String[] args) {
        SpinLockTest spinLockTest = new SpinLockTest();
        new Thread(()->{
            spinLockTest.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
                //Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockTest.unlock();
        },"AA").start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            spinLockTest.lock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockTest.unlock();
        },"BB").start();
    }
}
