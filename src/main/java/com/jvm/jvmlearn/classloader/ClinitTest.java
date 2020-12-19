package com.jvm.jvmlearn.classloader;

/**
 * 1. 基本数据类型 static final 显示赋值在链接阶段准备阶段完成
 * 2. string 字面量 显示赋值在链接阶段准备阶段完成
 */
public class ClinitTest {
    public static int a =1;
    public static final int INT_CONSTANT=10;
    //准备阶段不会执行代码,clinit阶段会
    //对引用类型,clinit阶段显示初始化
    public static final Integer INTEGER_CONSTANT1 = Integer.valueOf(100);
    public static Integer INTEGER_CONSTANT2 = Integer.valueOf(1000);

    public static final String s0 ="hello world"; //准备阶段确定
    public static final String s = new String("hello world");
}
class StaticA {
    static {
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
        }

        try {
            Class.forName("com.jvm.jvmlearn.classloader.StaticB");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("StaticA init OK");
    }
}
class StaticB {
    static {
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
        }

        try {
            Class.forName("com.jvm.jvmlearn.classloader.StaticA");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("StaticB init OK");
    }
}

/**
 * 因为clinit方法引用不当导致死锁
 * 类必须初始化才能被引用吗?
 */
class StaticDeadLockMain extends Thread {
    private char flag;
    public StaticDeadLockMain(char flag){
        this.flag = flag;
        this.setName("Thread" + flag);
    }
    @Override
    public void run(){
        try {
            Class.forName("com.jvm.jvmlearn.classloader.Static" + flag);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(getName() + "over");
    }

    public static void main(String[] args) {
        StaticDeadLockMain loadA = new StaticDeadLockMain('A');
        loadA.start();
        StaticDeadLockMain loadB = new StaticDeadLockMain('B');
        loadB.start();
    }
}