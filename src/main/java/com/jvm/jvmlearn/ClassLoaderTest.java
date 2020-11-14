package com.jvm.jvmlearn;

public class ClassLoaderTest {

    public static void main(String[] args) {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);//jdk.internal.loader.ClassLoaders$AppClassLoader@3fee733d

        //扩展类加载器
        ClassLoader extClassLoader = systemClassLoader.getParent();
        System.out.println(extClassLoader);//jdk.internal.loader.ClassLoaders$PlatformClassLoader@6f539caf

        //获取不到引导类加载器bootstrap
        //bootstrap使用c/c++语言实现,嵌套在JVM内部
        //加载Java核心库
        //加载扩展类和应用程序加载器,并指定为他们的父类加载器
        //只加载包名为java/javax/sum等开头的类
        ClassLoader bootstrapClassLoader = extClassLoader.getParent();
        System.out.println(bootstrapClassLoader);//null

        //应用程序加载器 classpath/java.class.path
        //程序中默认的类加载器
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println(classLoader);//jdk.internal.loader.ClassLoaders$AppClassLoader@3fee733d

        //String类使用引导类加载器进行加载--->Java的核心类库
        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println(classLoader1);//null

        //System.out.println("**********启动类加载器**************");
        //URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        //for(URL element : urls){
        //    System.out.println(element.toExternalForm());
        //}
        System.out.println("**********扩展类加载器**************");
        //jdk14失效
        String extDirs = System.getProperty("java.ext.dirs");
        for (String path : extDirs.split(";")) {
            System.out.println(path);
        }
    }
}
