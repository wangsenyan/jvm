package com.jvm.jvmlearn.jmm;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore 信号量
 * - 多个共享资源互斥使用,用于并发线程数的控制
 * - semaphore.acquire();
 * - semaphore.release();
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        SemaphoreTest st = new SemaphoreTest();
        st.test();
    }
    public void test(){
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " 爷抢到座位了");
                    TimeUnit.SECONDS.sleep(new Random().nextInt(7));
                    System.out.println(Thread.currentThread().getName() + " 爷用完了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
