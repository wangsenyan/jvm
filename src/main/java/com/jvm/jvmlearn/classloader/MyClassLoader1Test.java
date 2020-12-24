package com.jvm.jvmlearn.classloader;

public class MyClassLoader1Test {
    public static void main(String[] args) throws Exception {
        MyClassLoader1 myClassLoader1 = new MyClassLoader1("d:/");
        Class clazz = myClassLoader1.loadClass("MyTest");
        System.out.println("加载此类的类的加载器为:" + clazz.getClassLoader().getClass().getName() );
        System.out.println("当前类的父类加载器为:" +clazz.getClassLoader().getParent().getClass().getName());
        System.out.println("当前类的父类加载器为:" +clazz.getClassLoader().getParent().getParent().getClass().getName());
        System.out.println(ClassLoader.getPlatformClassLoader());
        //System.out.println("当前类的父类加载器为:" +clazz.getClassLoader().getParent().getParent().getParent().getClass().getName());
    }
}
