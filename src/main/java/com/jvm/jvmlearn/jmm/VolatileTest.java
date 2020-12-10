package com.jvm.jvmlearn.jmm;
/**
 * volatile 修改可见性
 */

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class  B {
    volatile   int number = 0;
    public void addTo(){
        this.number = 60;
    }
    public void addUnTo(){
        number++;
    }
    public synchronized void addPlus(){ number++;}
    AtomicInteger atomicInteger = new AtomicInteger();
    public void addAtomic(){
        atomicInteger.getAndIncrement();
    }
}

/**
 * java保证单线程数据依赖,然后指令重排
 */
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

/**
 * 不保证原子性,可用synchronized
 * 原因在于指令本身不是原子性,内存屏障会插入指令中,在volatile同步值之后,继续执行导致线程不安全
 * 加载->自增(寄存器)  [volatile 同步主内存值到工作内存中、更新到寄存器]  ->保存 导致当前线程的自增无效
 */
class VolatileAtomic{
    public static void main(String[] args) {
        B b = new B();
        for(int i=0;i<20;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++) {
                    b.addUnTo();
                    b.addAtomic();
                }
            },String.valueOf(i)).start();
        }
        while (Thread.activeCount()>2){

        }
        System.out.println(Thread.currentThread().getName()+ " number is " +b.number);
        System.out.println(Thread.currentThread().getName()+ " number is " +b.atomicInteger);
    }
}
