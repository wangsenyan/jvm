package com.jvm.jvmlearn.jmm;

import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一。为啥学lockSupport
 *  1.1 java --JVM
 *  1.2 JUC ---AQS --->前置 可重入锁,lockSupport
 * 二。学习方法
 *  1.1 是什么
 *   1.1.1 线程等待唤醒机制(wait/notify)的加强版
 *   1.1.2 park 阻塞线程
 *   1.1.3 unpark 取消阻塞线程
 *   1.1.4 三种等待和唤醒线程方法
 *       - Object中的wait()线程等待 notify()唤醒线程 必须在synchronized内,先wait后notify
 *       - JUC包中Condition的await()让线程等待,signal()唤醒线程 必须在lock内,先await后signal
 *       - LockSupport可以park()阻塞线程及unpark()唤醒指定被阻塞的线程
 *        - permit(0,1)默认为0,调用park,当前线程阻塞,知道其他线程将当前线程的permit设置为1,
 *          park方法会被唤醒,然后permit再次被设置为0并返回
 *        - park和unpark顺序可变
 *        - park 消耗permit
 *        - unpark 增加permit 最多1
 *  1.2 能干嘛
 *  1.3 去哪下
 *  1.4 怎么玩
 * 三。AB ---》after | before
 */
public class LockSupportTest {
    public static void main(String[] args) {
         LockSupportTest lockSupportTest = new LockSupportTest();
         lockSupportTest.locks();
    }
    public void locks(){
       Thread a =  new Thread(()->{
           System.out.println(Thread.currentThread().getName() + "\t"+"----come in");
           LockSupport.park();
           //LockSupport.park();
           System.out.println(Thread.currentThread().getName() + "\t" +"----被唤醒");
        },"a");
       a.start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread b = new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "\t"+"----come in");
            //LockSupport.unpark(a);
            LockSupport.unpark(a);
       },"b");
       b.start();
    }
}
