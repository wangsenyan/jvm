package com.jvm.jvmlearn.classloader;

import org.junit.Test;

import java.util.Random;

/**
 * 类的被动使用
 *
 */
public class PassiveUse {

    public static void main(String[] args) {

    }

    /**
     * - 当访问一个静态字段,只有真正声明这个字段的类才会被初始化
     *     - 当通过子类引用父类的静态变量,不会导致子类初始化,但会加载
     *   -XX:+TraceClassLoading 只上溯初始化
     */
    @Test
    public void test1(){
        System.out.println(Child.num);
    }

    /**
     * 数组声明不会初始化
     */
    @Test
    public void test2(){
        Parent[] parents = new Parent[10];
        System.out.println(parents.getClass());
        System.out.println(parents.getClass().getSuperclass());
        parents[0] = new Parent(); //此处才初始化
    }

    /**
     * 3. 类的常量不会初始化
     * 如果有常量在clinit中,则都会调用
     */
    @Test
    public void test3(){
        System.out.println(Parent.num);
    }

    /**
     * 3. 类变量
     */
    @Test
    public void test4(){
        System.out.println(SerialA.ID);
    }

    /**
     * 4. 调用ClassLoader类的loadClass()方法加载一个类,并不会导致类的初始化
     */
    @Test
    public void test5(){
        try {
            Class clazz = ClassLoader.getSystemClassLoader().loadClass("com.jvm.jvmlearn.classloader.Parent");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class Parent{
    static {
        System.out.println("parent的初始化");
    }
    public static int num = 1;
    public static final int nums = 1;
    //public static final int nums = new Random().nextInt();//导致输出num也会初始化
}
class Child extends Parent{
    static {
        System.out.println("child的初始化过程");
    }
    public static int n = 0;
}

interface SerialA{
    public static final Thread t = new Thread(){
        {
            System.out.println("SerialA初始化");
        }
    };
    int ID = 1;
    int ID1 = new Random().nextInt(10);
}