package com.jvm.jvmlearn.bytecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 一。抛出异常指令
 *  - throw(主动、被动)          --->athrow
 *  - JVM运行时异常会在其他Java虚拟机指令监测到异常状况时自动抛出
 * 二。异常及异常的处理
 *  - 抓抛模型 try-catch-finally --->使用异常表
 *  - 在抛出异常时,Java虚拟机会清除操作数栈上的所有内容,
 *    而后将异常实例压入调用者操作数栈上
 * 三。异常表
 * 1. 如果定义了try-catch或try-finally的异常处理,就会创建一个异常表
 *    保存了每个异常处理信息,比如
 *    - 起始位置
 *    - 结束位置
 *    - 程序计数器记录的代码处理的偏移地址
 *    - 被捕获的异常类在常量池中的索引
 * 2. 依次向上寻找匹配处理,没有则弹出当前栈帧,最后没有找到方法,线程终止,
 *    最后一个守护线程抛出,jvm终止
 */
public class ExceptionTest {
    public void throwZero(int i){
        if(i==0)
            throw new RuntimeException("参数值为0");
    }

    /**
     * 异常表
     * 0	0	22	25	cp_info #26
     * 1	0	22	33	cp_info #7
     *
     *  0 new #14 <java/io/File>
     *  3 dup
     *  4 ldc #16 <d:/hello.txt>
     *  6 invokespecial #18 <java/io/File.<init>>
     *  9 astore_1
     * 10 new #19 <java/io/FileInputStream>
     * 13 dup
     * 14 aload_1
     * 15 invokespecial #21 <java/io/FileInputStream.<init>>
     * 18 astore_2
     * 19 ldc #24 <hello!>
     * 21 astore_3
     * 22 goto 38 (+16)
     * 25 astore_1
     * 26 aload_1
     * 27 invokevirtual #28 <java/io/FileNotFoundException.printStackTrace>
     * 30 goto 38 (+8)
     * 33 astore_1
     * 34 aload_1
     * 35 invokevirtual #31 <java/lang/RuntimeException.printStackTrace>
     * 38 return
     */
    public void tryCatch(){
        try {
            File file = new File("d:/hello.txt");
            FileInputStream fis =  new FileInputStream(file);
            String info = "hello!";
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }
    public static String func(){
        String str = "hello";
        try{
            return str;
        }finally {
            str = "world";
        }
    }
    static class Name{
        private int id;
        private String name;
    }
    public static Name func1(){
        Name name = new Name();
        try{
            name.name = "hello";
            return name;
        }finally {
            name.name = "world";
        }
    }

    /**
     * 字符串常量地址不同
     */
    public static void main(String[] args) {
        System.out.println(func());
        System.out.println(func1().name);
    }
}
