package com.jvm.jvmlearn.jmm;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 做加法 CyclicBarrier
 * 可循环使用的屏障,让一组线程到达一个屏障(同步点)时被阻塞,
 * 直到最后一个线程到达屏障才会开门
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrierTest cbt = new CyclicBarrierTest();
        cbt.test();
    }
    public void test(){
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{
            System.out.println("日你妈的");
        });
        for (int i = 0; i <7; i++) {
            final int itemp = i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + " is ok");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
