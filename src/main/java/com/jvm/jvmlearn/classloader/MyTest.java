package com.jvm.jvmlearn.classloader;

public class MyTest {
    private static int j =11;
    private String name;
    private static MyTest myTest = new MyTest("hang");
    public MyTest(String name) {
        this.name = name;
        System.out.println(j);
    }

    public static void main(String[] args) {

    }
}
