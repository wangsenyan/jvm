package com.jvm.jvmlearn.error;

/**
 * 并发线程,native thread 与操作系统有关
 *  导致原因
 *    - 一个应用进程创建多个线程,超过系统承载极限
 *    - 服务器不允许创建那么多线程,linux允许1024个线程
 *  解决方法
 *    - 想办法降低应用程序创建线程的数量
 *    - 修改服务器线程限制
 *      - ulimit -u
 *      - /etc/security/limits.conf
 */
public class UnableCreateNewNativeThread {
    public static void main(String[] args) {
        for (int i = 0;  ; i++) {
            System.out.println(i);
            new Thread(()->{
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"" + i).start();
        }
    }
}
