package com.jvm.jvmlearn.jmm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一。AQS:AbstractQueuedSynchronizer
 * 1. 是什么
 *  -  抽象的队列同步器
 *  - 构建锁或者其他同步器组件的重量级基础框架及整个JUC体系的基石
 *    通过FIFO队列来完成资源获取线程的排队工作,并通过一个int类型
 *    变量标识持有锁的状态
 *  - 锁和同步器
 *   - 锁: 面向锁的使用者
 *   - 同步器: 面向锁的实现者
 * 2. 用关
 *  - CountDownLatch
 *  - ThreadPoolExecutor
 *  - ReentrantLock
 *  - ReentrantReadWriteLock
 *  - Semaphore
 * 3. AQS组成
 *  - volatile int state 表示同步状态
 *  - FIFO队列 资源获取的排队工作
 *  - Node 每条抢占资源的线程
 *   - volatile int  waitState 每个node的状态
 *     - WAITING 1
 *     - CANCELLED 0x80000000
 *     - COND 2
 *  - CAS 对State值的修改
 * 4. ReentrantLock
 *  - 公平锁
 *    - FairSync
 *    - hasQueuedPredecessors 判断是否有有效节点
 *  - 非公平锁
 *    - NonfairSync
 */
public class AQSTest {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
    }
}
