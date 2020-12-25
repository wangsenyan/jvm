package com.jvm.jvmlearn;

/**
 * 栈
 * 1. 一个方法对应一个栈帧
 * 2. 栈不存在垃圾回收
 * <p>
 * 栈异常
 * 1. StackOverflowError
 * 2. OutOfMemoryError
 * 异常逐级向上抛出,如果没有被捕获,虚拟机异常退出(捕获相当于正常返回)
 * 设置栈大小(1M) -XX:ThreadStackSize
 * -Xss1m
 * -Xss1024k
 * -Xss1048576
 * 工具链接 https://docs.oracle.com/en/java/javase/15/docs/specs/man/java.html
 */
public class StackTest {
    private static int count = 1;

    public static void main(String[] args) {
        //递归出错
        System.out.println(count);
        //f(count==5)
        //    return;
        count++;
        //main(args);
    }
}
