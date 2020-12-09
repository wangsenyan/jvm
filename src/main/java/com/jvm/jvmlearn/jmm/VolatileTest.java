package com.jvm.jvmlearn.jmm;
/**
 * volatile 修改可见性
 */

import java.util.concurrent.TimeUnit;

 class B {
    volatile int number = 0;
    public void addTo(){
        this.number = 60;
    }
}

public class VolatileTest {
    public static void main(String[] args) {
        B b = new B();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t come in");

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            b.addTo();
            System.out.println(Thread.currentThread().getName()+"\t update in" + b.number);
        },"AAA").start();
        while (b.number==0){

        }
        System.out.println(Thread.currentThread().getName()+"\t mission is over");
    }
}
