package com.jvm.jvmlearn;

import org.springframework.asm.ClassWriter;
import org.springframework.asm.Opcodes;

/**
 * MetaSpace 本地物理内存不是虚拟机内存???
 * 1. jdk7之前 PermSize=size
 * 2. Jdk8及之后
 * -XX:MaxMetaspaceSize=size
 * -XX:MetaspaceSize=size   -1 没有限制
 * <p>
 * 错误分析OOM
 * 1. 通过内存映像分析工具对dump出来的堆内存转储快照进行分析 --内存泄漏/溢出
 * 2. 内存泄漏 GC Roots引用分析
 * 3. 检查虚拟机的堆参数，检查是否存在对象生命周期过长
 * <p>
 * 方法区(Method Area)
 * 1. 存储类型信息、常量、静态变量、即时编译器编译后的代码缓存
 * 1）类型信息
 * a. 完整有效名称 (包名.类名)
 * b. 直接父类的完整有效名(interface或java.lang.Object都没父类)
 * c. 类型修饰符(public,abstract,final的某个子集)
 * d. 直接接口的一个有序列表
 * 2) 域(Field)信息 javap -v -p MetaSpaceTest.class > text.txt  // -p 显示private信息
 * a. 保存所有域的相关信息以及域的声明顺序
 * b. 域的相关信息包括:域名称,域类型,域修饰符(public,private,protected,static,final,volatile,transient的某个子集)
 * c. 静态方法 a.b() -> new a() =null, a.b()正常访问
 * d. 全局常量: static final 编译时刻已经被分配
 * 3) 常量池  Constant pool  #num 引用
 * a. 类名，方法名，参数类型，字面量等类型
 * b. 运行时常量池:常量池表加载到内存称为运行时常量池
 * 符号引用转换为真实地址
 * 每个加载的类都有
 * 4) 静态变量，字符串常量放在堆(永久代,GC)，其他放在本地内存中
 */
public class MetaSpaceTest extends ClassLoader {
    public static void main(String[] args) throws InterruptedException {
        //java.lang.OutOfMemoryError: Compressed class space
        int j = 0;
        MetaSpaceTest test = new MetaSpaceTest();
        try {
            for (int i = 0; i < 10000; i++) {
                //创建classWriter对象,用于生成类的二进制字节码
                ClassWriter classWriter = new ClassWriter(0);
                //指明版本号，修饰符，类名，包名，父类，接口
                classWriter.visit(Opcodes.V14, Opcodes.ACC_PUBLIC, "Class" + i, null, "java/lang/Object", null);
                //返回byte
                byte[] code = classWriter.toByteArray();
                //类的加载
                test.defineClass("Class" + i, code, 0, code.length);
                j++;
            }
        } finally {
            System.out.println(j);
        }
        //Thread.sleep(100000);
    }
}

/**
 * public static int count;
 * descriptor: I
 * flags: (0x0009) ACC_PUBLIC, ACC_STATIC //关键
 * <p>
 * public static final int number;
 * descriptor: I
 * flags: (0x0019) ACC_PUBLIC, ACC_STATIC, ACC_FINAL
 * ConstantValue: int 2  //关键
 * <p>
 * 。。。
 * //<clinit>
 * static {};
 * descriptor: ()V
 * flags: (0x0008) ACC_STATIC
 * Code:
 * stack=1, locals=0, args_size=0
 * 0: iconst_1
 * 1: putstatic     #21                 // Field count:I
 * 4: return
 * LineNumberTable:
 * line 55: 0
 */
class Order {
    public static int count = 1;
    public static final int number = 2;

    public static void hello() {
        System.out.println("hello");
    }

    ;
}