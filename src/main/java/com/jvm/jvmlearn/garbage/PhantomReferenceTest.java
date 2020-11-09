package com.jvm.jvmlearn.garbage;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用,
 * 可以跟踪对象的回收时间,因此,也可以将一些资源释放操作放置在虚引用中执行和记录
 */
public class PhantomReferenceTest {
    public static PhantomReferenceTest obj;
    static ReferenceQueue<PhantomReferenceTest> phantomQueue = null;

    public static class CheckRefQueue extends Thread {

        @Override
        public void run() {
            while (true){
                if(phantomQueue!=null){
                    PhantomReference<PhantomReferenceTest> objt = null;
                    try{
                      objt = (PhantomReference<PhantomReferenceTest>) phantomQueue.remove();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("正在回收");
                }
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("调用当前类的finalize()方法");
        obj = this;
    }

    public static void main(String[] args) {
        Thread t = new CheckRefQueue();
        //守护线程:当程序中没有非守护线程时,守护线程结束
        t.setDaemon(true);
        t.start();

        phantomQueue = new ReferenceQueue<PhantomReferenceTest>();
        obj = new PhantomReferenceTest();
        PhantomReference<PhantomReferenceTest> phantomRef = new PhantomReference<PhantomReferenceTest>(obj,phantomQueue);

        try{
            System.out.println(phantomRef.get());
            obj = null;

            System.gc();
            Thread.sleep(1000);
            if(obj == null){
                System.out.println("obj 是 null");
            }else{
                System.out.println("obj 可用");
            }
            System.out.println("第二次 gc");
            obj = null;
            System.gc();//一旦对象回收,会放到队列中
            Thread.sleep(1000);
            if(obj == null){
                System.out.println("obj 是 null");
            }else{
                System.out.println("obj 可用");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
