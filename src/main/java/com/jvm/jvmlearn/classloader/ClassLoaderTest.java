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
 * 3. 解析阶段
 *
 * 三。初始化阶段
 * 0. 为类的静态变量赋予正确的初始值
 * 1. 到了初始化阶段,才真正开始执行类中定义的Java程序代码
 * 2. <clinit>()方法
 *   - Java编译器生成并由JVM调用
 *   - 由静态成员的赋值语句及static语句块合并产生的
 *   - 父类的<clinit>()在子类<clinit>()之前调用 由父及子,静态先行
 * 3. 不会生成<clinit>()的场景 ClinitTest
 *   - 没静态字段
 *   - 静态字段没有显示赋值
 *   - final static 基本数据类型字段 --在准备期就已经确定
 * 4. <clinit>()的线程安全性
 *   - 带锁线程安全
 * 5. 类主动使用和被动使用
 *   - 主动使用才调用clinit,被动不会
 *   - 主动使用
 *     -1 创建一个类的实例,new关键字,或者通过反射,克隆,反序列化
 *     -2 调用静态方法,即使用字节码的invokestatic
 *     -3 当使用类、接口的静态字段时(final修饰符特殊考虑),比如,使用getstatic或 putstatic指令
 *     -4 当使用java.lang.reflect包中的方法反射类的方法时,Class.forName()
 *     -5 当初始化子类时,如果发现其父类还没有进行初始化,则先触发父类初始化
 *        - 不适用于接口,初始化一个接口,不会初始化父接口
 *     -6 如果一个接口定义了default方法,那么直接实现或间接实现该接口的类的初始化,该接口要在其之前被初始化
 *     -7 当虚拟机启动时,用户要指定一个要执行的主类(main),虚拟机先初始化这个主类
 *     -8 当初次使用MethodHandle实例时,初始化改MethodHandle指向的方法所在的类(涉及解析REF_getStatic,REF_putStatic)
 *   - 被动使用
 *    - 当访问一个静态字段,只有真正声明这个字段的类才会被初始化
 *      - 当通过子类引用父类的静态变量,不会导致子类初始化
 *    - 通过数组定义类引用,不会触发
 *    - 引用常量不会触发类或接口的初始化,常量在链接阶段已经被显示初始化
 *    - 调用ClassLoader类的loadClass()方法加载一个类,并不会导致类的初始化
 */
public class ClassLoaderTest {
    private static long id;
    private static final  int num=0;
    public static final String constStr="CONST";
    //private String name;

//    private static ClassLoaderTest clt = new ClassLoaderTest("wang");
//    public ClassLoaderTest(String name) {
//        this.name = name;
//        System.out.println(name);
//    }

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

class A {
    private static final A a = new A();
}