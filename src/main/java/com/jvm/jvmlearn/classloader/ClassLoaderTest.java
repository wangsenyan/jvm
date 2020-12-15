package com.jvm.jvmlearn.classloader;

/**
 * 一。类的生命周期
 * 加载(loading)- 链接（验证(verify)，准备(prepare)，解析(resolve)）- 初始化(initial) - 使用(using) - 卸载(unmount)
 * 二。loading加载阶段
 *  1. 字节码 -> 内存中类模板对象    （反射）
 *  2. 加载阶段,查找并加载类的二进制数据,生成class的实例
 *  3. 过程
 *    - 通过类的全名,获取类的二进制数据流
 *    - 解析类的二进制数据流为方法区的数据结构(Java类模板)
 *    - 创建java.lang.Class类的实例,表示该类型,
 *      作为方法区这个类的各种数据的访问入库
 *  4. 二进制流的获取方法（只要所读取的字节码符合jvm规范即可）
 *    - class后缀的文件
 *    - jar,zip等归档的数据包,提取类文件
 *    - http协议通过网络进行加载
 *    - 在运行时生成一段class的二进制信息
 *  5. 不是ClassFile格式，classFormatError
 */
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
