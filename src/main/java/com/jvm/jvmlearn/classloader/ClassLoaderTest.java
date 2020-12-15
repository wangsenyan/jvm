package com.jvm.jvmlearn.classloader;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 一。类的生命周期
 * 加载(loading)- 链接（验证(verify)，准备(prepare)，解析(resolve)）- 初始化(initial) - 使用(using) - 卸载(unmount)
 * 二。loading加载阶段
 *  0. 基本数据类型由虚拟机预先定义,引用数据类型则需要进行类的加载
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
 *
 *  6. 类型与class实例的位置
 *    - java.lang.Class 在堆中
 *    - 类模型的位置:方法区
 *    - Class类的构造方法是私有的,只有JVM能够创建
 *  7. 数组类的加载
 *    - 数组本身不是由类加载器负责创建的
 *    - 如果数组的元素类型死引用类型,那么就遵循定义的加载过程递归加载和创建数组A的元素类型
 *    - JVM使用指定的元素类型和数组维度来创建新的数组类
 *    - 元素类型是引用类型,访问性由元素类型决定,基本类型为public
 * 二。链接阶段
 * 1. 验证阶段(verification)
 *   - 保证加载的字节码是合法、合理并符合规范的
 *   - 验证项目
 *     - 格式检查
 *     - 语义检查（是否继承final,是否有父类,抽象类是否实现,是否不兼容
 *     - 字节码验证(跳转不存在的指令,函数调用是否传递正确的参数,变量的赋值不正确的数据类型,栈映射帧(StackMapTable)
 *     - 符号引用验证
 *       - 通过类或方法的字符串名称检查这些类或方法是否存在
 *       - NoClassDefFoundError
 *       - NoSuchMethodError
 * 2. 准备阶段
 *   - 为静态变量分配内存,并初始化为默认值
 *   - static final 在编译的时候就会分配,准备阶段显示赋值
 *   - 非final修饰的变量,在准备环节进行默认初始化赋值
 */
public class ClassLoaderTest {
    private static long id;
    private static final  int num=0;
    public static final String constStr="CONST";
    //初始化阶段
    public static final String constStr1= new String("CONST");
    public static void main(String[] args) {
        Loading();
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
    public static void Loading(){
        try {
           Class clazz = Class.forName("java.lang.String");
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
class LinkingTest{
    private static long id;
    private static final  int num=0;
    public static final String constStr="CONST";
}