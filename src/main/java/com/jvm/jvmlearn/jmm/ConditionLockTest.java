package com.jvm.jvmlearn.jmm;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock,条件变量
 * AA(5)-> BB(10) -> CC(15)
 * 资源:负责打印(唤醒对象)
 * 线程:执行
 */
class ShareResource{
   private String name;
   private Lock lock = new ReentrantLock();
   private Condition condition = lock.newCondition();

    public ShareResource(String next) {
        this.name = next;
    }

    public void printn(int number, String  next){
       lock.lock();
       try{
           if(name == null) return;
           String myName = Thread.currentThread().getName();
           while (name!= myName)
               condition.await();
           for (int i = 0; i < number ; i++) {
               System.out.println(Thread.currentThread().getName() + "\t" + i);
           }
           name = next;
           condition.signalAll();
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           lock.unlock();
       }
   }
}
public class ConditionLockTest {
    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource("AA");

        new Thread(()->{
            shareResource.printn(10,"CC");
        },"BB").start();
        new Thread(()->{
            shareResource.printn(15,null);
        },"CC").start();
        new Thread(()->{
            shareResource.printn(5,"BB");
        },"AA").start();
    }
}
