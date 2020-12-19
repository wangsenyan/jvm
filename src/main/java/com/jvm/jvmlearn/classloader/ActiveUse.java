package com.jvm.jvmlearn.classloader;

import org.junit.Test;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.util.Random;

class Order implements Serializable {
    public static int o = 1;
    public static final int oo = 1;
    static {
        System.out.println("Order 执行初始化");
    }
    public static void method(){
        System.out.println("static method");
    }

    /**
     * 7.  main 导致当前类加载 clinit
     */
    public static void main(String[] args) {
        System.out.println("7. main 导致当前类加载 clinit ");
    }
}
class MyOrder extends Order implements CompareA {
    static {
        System.out.println("MyOrder 执行初始化");
    }
    public static int num =1;
}
interface CompareA{
    public static final Thread t = new Thread(){
        {
            System.out.println("CompareA的初始化");
        }
    };
    public static int a = 10;
    public static final int b = new Random().nextInt(10);
    public default void method1(){};
}
class YourOrder implements CompareA {
    static {
        System.out.println("YourOrder 执行初始化");
    }
    public static int num =1;
}
interface CompareB extends  CompareA {
    public static final Thread t = new Thread(){
        {
            System.out.println("CompareB的初始化");
        }
    };
    public static int a = 10;
    public static final int b = new Random().nextInt(10);
}

/**
 * 反序列化
 */
public class ActiveUse {
    @Test
    public void test1(){
        ObjectOutputStream oos = null;
        try{
            oos = new ObjectOutputStream(new FileOutputStream("order.dat"));
            oos.writeObject(new Order());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(oos != null)
                   oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void test2(){
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("order.dat"));
            Order order =  (Order) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            try {
                if(ois!=null)
                  ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     *   2. 静态代码块
     */
    @Test
    public void test3(){
        Order.method();
    }

    /**
     * 3. 静态字段, static final 不会导致初始化
     */
    @Test
    public void test4(){
        System.out.println(Order.o);
    }
    @Test
    public void test5(){
        System.out.println(Order.oo);
    }

    /**
     * 3. 静态字段, 接口的静态字段
     */
    @Test
    public void test6(){
        //System.out.println(CompareA.a); //为什么不初始化
        System.out.println(CompareA.b);
    }

    /**
     * 4. Class.forName
     */
    @Test
    public void test7(){
        try {
            Class.forName("com.jvm.jvmlearn.classloader.Order");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * -XX:+TraceClassLoading   ---> template ->junit 中添加,打印load参数
     * 5. 子类clinit,父类也执行clinit
     *    implements 接口,但是接口不执行clinit
     */
    @Test
    public void test8(){
        System.out.println(MyOrder.num);
        System.out.println(CompareB.b); //父接口CompareA不会执行clinit
    }

    /**
     * 6. default方法,实现的interface也初始化
     *    public default void method1(){};
     */
    @Test
    public void test9(){
        System.out.println(YourOrder.num);
    }

    /**
     * 8. MethodHandle
     */
    public void test10(){
       // MethodHandle md = new MethodHandle();
    }
}
