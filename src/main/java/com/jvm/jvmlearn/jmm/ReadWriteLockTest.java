package com.jvm.jvmlearn.jmm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 */
public class ReadWriteLockTest {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    public void inc(){
        atomicInteger.getAndIncrement();
    }
   private volatile Map<String,Object> map= new HashMap<>();
   private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
   public void put(String key,Object value){
       rwlock.writeLock().lock();
       try{
           System.out.println(Thread.currentThread().getName() + " 正在写入 " + value) ;
           map.put(key,value);
           TimeUnit.SECONDS.sleep(1);
           System.out.println(Thread.currentThread().getName() + " 写入完成 " );
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           rwlock.writeLock().unlock();
       }
   }
   public void get(String key){
       rwlock.readLock().lock();
       try{
           System.out.println(Thread.currentThread().getName() + " 正在读取");
           Object r = map.get(key);
           System.out.println(Thread.currentThread().getName() + " 读取完成" + r);
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           rwlock.readLock().unlock();
       }
   }

    public static void main(String[] args) {
       ReadWriteLockTest rwl = new ReadWriteLockTest();
        for(int i=0;i<10;i++){
            final String t = String.valueOf(i);
            new Thread(()->{
                rwl.put(t,t);
            },String.valueOf(i)).start();
        }
        for(int i=0;i<10;i++){
            final String t = String.valueOf(i);
            new Thread(()->{
                rwl.get(String.valueOf(t));
            },String.valueOf(i)).start();
        }
    }
}
