package com.jvm.jvmlearn;

//https://blog.csdn.net/justloveyou_/article/details/72466416
public class WhaTest {
    private static WhaTest ws;

    //ws = new WhaTest();
    //构造代码块,优先于类构造函数
    {
        System.out.println("class A");
    }

    static {
        System.out.println("Class B");
    }

    public static void main(String[] args) {
        WhaTest t = new WhaTest();
    }
}
