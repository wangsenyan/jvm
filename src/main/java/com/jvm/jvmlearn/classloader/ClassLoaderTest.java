package com.jvm.jvmlearn.classloader;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

/**
 * 一。类的生命周期
 * 加载(loading)- 链接（验证(verify)，准备(prepare)，解析(resolve)）- 初始化(initial) - 使用(using) - 卸载(unmount)
 * 二。loading加载阶段
 *  0. 基本数据类型由虚拟机预先定义,引用数据类型则需要进行类的加载
 *  1. 字节码 -> 内存中类模板对象    （反射）
 *  2. 加载阶段,查找并加载类的二进制数据,生成clas xss的实例
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
 *   - 字符串引用转为地址引用
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
 * 四。类的使用
 * 五。类的卸载
 *  1. 类、类的加载器、类的实例之间的引用关系
 *     loader引用变量 --> 类的加载器 <---> 类的Class实例
 *                                 --->类的二进制数据结构
 *                                 --->类的实例(堆) <----类的实例引用变量
 *  2. 类的生命周期
 *     - Class对象的生命周期
 *     - 类回收满足三个条件（方法区的垃圾回收）
 *       - 所有实例被回收(java堆中不存在该类及其任何派生类的实例)
 *       - 加载该类的类加载器已经被回收，OSGi,JSP的重载等
 *       - 对应的java.lang.Class对象没有在任何地方被引用,无法通过反射访问该类的方法
 *
 *
 * 六。类的加载器
 *  1. 类的加载分类
 *   - 显示加载
 *     - Class.forName(name)
 *     - this.getClass().getClassLoader().loadClass()
 *   - 隐式加载:虚拟机自动加载到内存中
 *     - class文件中引用了另一个类的对象
 *  2.了解的必要性
 *   - java.lang.ClassNotFoundException 或 java.lang.NoClassDefFoundError
 *   - 支持类的动态加载或对编译后的字节码进行加密解密操作
 *   - 自动以加载器重新定义类的加载规则,实现自己的逻辑
 *  3. 命名空间
 *   - 类由加载它的类加载器和这个类本身一同确认在java虚拟机中的唯一性
 *   - 每个类加载器都有一个独立的类名称空间,命名空间由该加载器及所有的父加载器加载的类组成
 *   - 同一命名空间中不会出现类的完整名字相同的两个类
 *   - 在不同的命名空间中,有可能出现类的完整名字相同的两个类
 *  4. 类加载器的基本特征
 *   - 双亲委派模型
 *   - 可见性:子类加载器可以访问父类加载器,反过来不允许
 *   - 单一性:父加载过的类型,不会被子加载器再次加载
 *  5. 类的加载器分类
 *   - 引导类加载器
 *     - 启动类加载器（java，javax,sun开头的）
 *   - 自定义类加载器(java实现)aa
 *     - 扩展类加载器(sun.misc.Launcher$ExtClassLoader)
 *      - 继承ClassLoader
 *      - 父类加载器为启动类加载器
 *      - 从java.ext.dirs,jre/lib/ext子类
 *     - 应用程序类加载器(sun.misc.Launcher$AppClassLoader)
 *      - AppClassLoader
 *      - classpath java.class.path
 *     - 用户自定义加载器
 *  6. 测试不同的类加载器
 *   - 获取ClassLoader的途径
 *     - 获取当前类的ClassLoader
 *       clazz.getClassLoader
 *     - 获取当前线程上下文的ClassLoader
 *       Thread.currentThread().getContextClassLoader()
 *     - 获取系统的ClassLoader
 *       ClassLoader.getSystemClassLoader()
 *   - 数组的加载器和数组元素类型的加载器相同
 *   - 基本数据类型不需要加载
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
//        String extDirs = System.getProperty("java.ext.dirs");
//        for (String path : extDirs.split(";")) {
//            System.out.println(path);
//        }
        String[] arrStr = new String[6];//数组中元素类型使用的加载器一样
        System.out.println(arrStr.getClass().getClassLoader());//null表示引导类加载器

        ClassLoader1[] arr1 = new ClassLoader1[6];
        System.out.println(arr1.getClass().getClassLoader());
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

class UserClassLoader extends ClassLoader{

}
class ClassLoader1 {
    public static void main(String[] args) throws ClassNotFoundException {

        System.out.println("**************启动类加载器**************");
//       System.out.println(classLoader);
//        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
//        for(URL element:urls)
//            System.out.println(element.toExternalForm());
        Class clazz3 = ClassLoader.getSystemClassLoader().loadClass("com.jvm.jvmlearn.classloader.ClassLoader1");
        System.out.println(clazz3.getClassLoader());
        System.out.println(clazz3.getClassLoader().getParent());
    }
}