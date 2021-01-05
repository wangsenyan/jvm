package com.jvm.jvmlearn.classloader;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MyClassLoader1Test {
    public static void main(String[] args) throws Exception {
//        MyClassLoader1 myClassLoader1 = new MyClassLoader1("d:/");
//        Class clazz = myClassLoader1.loadClass("MyTest");
//        System.out.println("加载此类的类的加载器为:" + clazz.getClassLoader().getClass().getName() );
//        System.out.println("当前类的父类加载器为:" +clazz.getClassLoader().getParent().getClass().getName());
//        System.out.println("当前类的父类加载器为:" +clazz.getClassLoader().getParent().getParent().getClass().getName());
//        System.out.println(ClassLoader.getPlatformClassLoader());
          Loading("MyTest");

        //System.out.println("当前类的父类加载器为:" +clazz.getClassLoader().getParent().getParent().getParent().getClass().getName());
    }
    public static void Loading(String name){
        try {
            MyClassLoader1 myClassLoader1 = new MyClassLoader1("d:/");
            Class clazz = myClassLoader1.loadClass(name);
            //Class claxx = Class.forName("Myt");
            //Class clazz = Class.forName("java.lang.String");
            //获取当前运行时类声明的所有方法
            Method[] ms = clazz.getDeclaredMethods();
            for(Method m : ms){
                //获取方法的修饰符
                String mod = Modifier.toString(m.getModifiers());
                System.out.print(mod+" ");
                //获取方法的返回值类型
                String returnType = m.getReturnType().getSimpleName();
                System.out.print(returnType + " ");
                //获取方法名
                System.out.print(m.getName() + "(");
                //获取方法的参数列表
                Class<?>[] ps = m.getParameterTypes();
                if(ps.length == 0) System.out.print(')');
                for(int i =0;i<ps.length;i++){
                    char end = (i== ps.length -1)? ')' :',';
                    //获取参数的类型
                    System.out.print(ps[i].getSimpleName() +end);
                }
                System.out.println();
            }
        }catch (Exception e){
            //在命令行打印异常信息在程序中出错的位置及原因
            e.printStackTrace();
        }
    }
}
