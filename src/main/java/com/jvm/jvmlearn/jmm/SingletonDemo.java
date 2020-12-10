package com.jvm.jvmlearn.jmm;

public class SingletonDemo {
    private volatile static SingletonDemo instance;

    public SingletonDemo() {
        System.out.println("hello");
    }

    /**
     * 多线程导致多个实例,非原子性
     * @return
     */
    public static SingletonDemo getInstance0(){
        if(instance == null) {
            instance = new SingletonDemo();
        }
        return instance;
    }

    /**
     * DCL双端检索
     * 指令重排导致: 看似
     */
    public static SingletonDemo getInstance(){
        if(instance == null) {
            synchronized (SingletonDemo.class){
                if(instance == null)
                    //分配内存 1
                    //内存初始化 2
                    //内存地址赋值给instance 3 instance!=null
                    //1,2,3 可能重排为1,3,2
                    instance = new SingletonDemo();
            }
        }
        return instance;
    }

    /**
     * 用volatile修饰instance
     */
    public static SingletonDemo getInstance1(){
        if(instance == null) {
            synchronized (SingletonDemo.class){
                if(instance == null)
                    //分配内存 1
                    //内存初始化 2
                    //内存地址赋值给instance 3 instance!=null
                    //1,2,3 可能重排为1,3,2
                    instance = new SingletonDemo();
            }
        }
        return instance;
    }
    public static void main(String[] args) {
        for(int i=0;i<20;i++){
            new Thread(()->{
                //SingletonDemo.getInstance0();
                SingletonDemo.getInstance();
            },String.valueOf(i)).start();
        }
    }
}
