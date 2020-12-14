package com.jvm.jvmlearn.bytecode;

/**
 * 同步--通过monitor实现
 * 1. 方法级同步
 *  - 检查方法的ACC_SYNCHRONIZED访问标志是否设置 管程同步
 *  - 同步过程
 *   - 同步锁 - 执行完后释放
 *   - 异常,方法内部不能处理,异常抛出到方法外自动释放同步锁
 * 2. 方法内指定指令序列的同步 - java 虚拟机中,任何对象都有一个监视器与之相关联
 *   - monitorenter
 *   - monitorexit
 */
public class SyncTest {
    private int i=0;
    public synchronized void add(){
        i++;
    }

    private Object obj = new Object();

    /** 默认两个异常处理函数 1. 正常代码块的异常 2，处理异常时候的异常
     *  0 aload_0
     *  1 getfield #13 <com/jvm/jvmlearn/bytecode/SyncTest.obj>
     *  4 dup
     *  5 astore_1
     *  6 monitorenter
     *  7 aload_0
     *  8 dup
     *  9 getfield #7 <com/jvm/jvmlearn/bytecode/SyncTest.i>
     * 12 iconst_1
     * 13 isub
     * 14 putfield #7 <com/jvm/jvmlearn/bytecode/SyncTest.i>
     * 17 aload_1
     * 18 monitorexit
     *
     * 19 goto 27 (+8)
     * 22 astore_2  //任何异常
     * 23 aload_1
     * 24 monitorexit //异常退出后,取消锁
     * 25 aload_2
     * 26 athrow //异常抛出
     * 27 return
     */
    public void subtract(){
        synchronized (obj){
            i--;
        }
    }
}
