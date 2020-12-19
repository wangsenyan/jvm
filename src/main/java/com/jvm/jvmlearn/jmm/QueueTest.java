package com.jvm.jvmlearn.jmm;

import org.junit.Test;

import java.beans.Transient;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * o. 队列
 *  - Collection
 *    - List
 *    - Queue
 *      - BlockingQueue
 *        - ArrayBlockingQueue 数组,有界,阻塞
 *         - add(e)              remove()         element() 错误抛异常
 *         - offer(e)            poll()           peek()    错误返false
 *         - put(e)              take()           无        错误阻塞
 *         - offer(e,time,unit)  poll(time,unit)  无        过时返回false
 *
 *        - DelayedWorkQueue   链表,有界,阻塞 默认 Integer.MAX_VALUE
 *        - SynchronousQueue   不存储元素,阻塞
 *        - BlockingDeque
 *          - LinkedBlockingDeque 链表,双向,阻塞
 *        - DelayQueue         优先级,延迟,无界
 *        - TransferQueue
 *          - LinkedTransferQueue 链表,无界,阻塞
 *        - LinkedBlockingQueue
 *        - PriorityBlockingQueue  优先级,无界,阻塞
 * 一。队列
 * 二。阻塞队列
 *  - 如果阻塞队列为空,从队列获取元素的操作会被阻塞
 *  - 如果阻塞队列为满,往队列里添加元素的操作将会被阻塞
 *  - 用途
 *   - 生产者消费者模式
 *   - 线程池
 *   - 消息中间件
 *  ？？ 信号量,生产者释放,消费者生产
 * 三。synchronized与lock的区别
 * 1. 原始构成
 *   - synchronized是关键字属于JVM层面
 *     monitorenter(底层是通过monitor对象来完成,其实wait/notify等方法也依赖于monitor对象只有在同步块或方法中才能调用wait/notify生方法
 *     monitorexit
 *   - lock是具体类(java.util.concurrent.locks.lock)是api层面的锁
 * 2. 使用方法
 *   - synchronized 不需要用户去手动释放锁,当synchronized代码执行完成后系统自动让线程释放对锁的占用
 *   - ReentrantLock 需要用户去手动释放锁,若没有释放锁,就可能导致死锁现在
 *     需要lock和unlock方法配合try/finally语句块来完成
 * 3. 等待是否可中断
 *   - synchronized不可中断
 *   - ReentrantLock 可中断
 *    - 设置超时方法 tryLock(long timeout,TimeUnit unit)
 *    - lockInterruptibly()放代码块中,调用interrupt方法可中断
 * 4. 加锁是否公平
 *   - synchronized 非公平锁
 *   - ReentrantLock 可公平可不公平
 * 5. 锁绑定多个条件Condition
 *   - synchronized 没有,随机唤醒一个或全部唤醒
 *   - ReentrantLock用来实现分组唤醒需要唤醒的线程们,可以精确唤醒,而不是像synchronized
 */
public class QueueTest {

    public static void main(String[] args) {
      QueueTest queueTest = new QueueTest();
      //queueTest.syncBQ();
        //queueTest.test();
        queueTest.test1();
    }
    public  void arrayBQ(){
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.element());
        System.out.println(blockingQueue.remove());
    }

    public void syncBQ(){
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();//必须Put配合才能阻塞
        new Thread(()->{
            try {
                blockingQueue.put("1");
                System.out.println(Thread.currentThread().getName() + "\t" + "加入1");
                blockingQueue.put("2");
                System.out.println(Thread.currentThread().getName() + "\t" + "加入2");
                blockingQueue.put("3");
                System.out.println(Thread.currentThread().getName() + "\t" + "加入3");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AAA").start();
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(blockingQueue.poll());
                TimeUnit.SECONDS.sleep(5);
                System.out.println(blockingQueue.poll());
                TimeUnit.SECONDS.sleep(5);
                System.out.println(blockingQueue.poll());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BBB").start();
    }

    /**
     * 交替0101010101 失败
     * 需要在SynchronousQueue.put 和 take 输出
     */
    public void test(){
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();
        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    blockingQueue.put("1");
                    System.out.println("0");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    blockingQueue.take();
                    System.out.println(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();

    }
    class Box{
        private volatile int number = 0;
        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();
        public void inc(){
            lock.lock();
            try {
                while (number!=0){ //if 虚假唤醒
                    condition.await();
                    //这个位置被强占了？
                }
                number++;
                System.out.println(Thread.currentThread().getName() + "\t" + number );
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
        public void dec(){
            lock.lock();

            try {
                while (number==0){
                    condition.await();
                }
                number--;
                System.out.println(Thread.currentThread().getName() + "\t" + number );
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
        public int getNumber() {
            return number;
        }
    }

    /**
     * 交替0101010101 成功
     * 虚假唤醒,if 条件进去后,判断条件改变,主要不是原子性
     * await结束,但是又被其他线程抢占资源了
     */
    public void test1(){
        Box box = new Box();
        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                box.inc();
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                box.dec();
            }
        },"BB").start();
        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                box.inc();
            }
        },"CC").start();

        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                box.dec();
            }
        },"DD").start();
    }
}
