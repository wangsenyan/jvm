package com.jvm.jvmlearn.jmm;

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
 */
public class AQSTest {
    public static void main(String[] args) {
        new ReentrantLock();
    }
}
