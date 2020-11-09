package com.jvm.jvmlearn;

public class JvmLearnApplication {
    private static int num = 11;

    static {
        num = 22;
        number = 44;
        //System.out.println(number);//非法的前向调用
        //clinit 只对类变量赋值和静态代码块语句合并 比clinit先执行
        //init 类构造器，父类构造器->本身构造器
        //<clinit>()方法在多线程下呗同步加锁
    }

    private static int number = 33;//link  prepare =0 链接阶段已经初始化为0,在initial阶段,根据clint执行

    public static void main(String[] args) throws InterruptedException {
        int i = 2;
        int j = 3;
        int k = i + j;

        Thread.sleep(6000);
        System.out.println("hello");
        // SpringApplication.run(JvmLearnApplication.class, args);
    }

    public JvmLearnApplication() {
        num = 1;
    }

    public void test() {
        System.out.println("test this");
    }
}
