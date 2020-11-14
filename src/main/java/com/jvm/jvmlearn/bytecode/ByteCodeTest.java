package com.jvm.jvmlearn.bytecode;

/**
 * 1. 继承 + 多态
 *    类成员变量无多态?
 *    调用方法：由对象的实际类型决定调谁的。
 *    访问属性：由引用的定义类型决定访问谁的。
 * 一。基本概念
 * 1. class文件是一组以8位字节为基础单位的二进制流，没有任何分隔符号
 *    类c语言结构体的方式进行数据存储,只有两种书数据类型:无符号数和表
 *    - 无符号数：u1,u2,u4,u8 分别表示1、2、4、8个字节的无符号数
 *      描述数字，索引引用，数量值或按照utf-8编码构成的字符串值
 *    - 表是由多个无符号数或其他表示作为数据结构项构成的复合数据类型，
 *      所有表都习惯地以"_info"结尾
 * 二。class文件的基本结构
 * ```
 * ClassFile {
 *     u4             magic;
 *     u2             minor_version;
 *     u2             major_version;
 *     u2             constant_pool_count;
 *     cp_info        constant_pool[constant_pool_count-1];
 *     u2             access_flags;
 *     u2             this_class;
 *     u2             super_class;
 *     u2             interfaces_count;
 *     u2             interfaces[interfaces_count];
 *     u2             fields_count;
 *     field_info     fields[fields_count];
 *     u2             methods_count;
 *     method_info    methods[methods_count];
 *     u2             attributes_count;
 *     attribute_info attributes[attributes_count];
 * }
 * ```
 * 1. 魔数
 * 2. Class文件版本
 * 3. 常量池
 *    - 包括: 字面量(文本字符串、声明为final的常量池)
 *           符号引用(类和接口的全限定名,分号结束、字段的名称和描述符。方法的名称和描述符)
 *           ```
 *           B byte
 *           C char
 *           D double
 *           F float
 *           I int
 *           J long
 *           S short
 *           Z boolean
 *           V void
 *           [ 一维数组 [[ 二维数组 [[[三维数组
 *           ```
 *    - 常量池计数器: 常量池表项-1（从1开始 0 表示不引用任何一个常量池项目）
 *    - 当虚拟机运行时,需要从常量池中获得对应的符号引用,再在类的加载过程中的解析阶段将其替换为直接引用,并翻译到具体的内存地址中
 *    - 直接引语:必定加载到内存中,可以直接定位内存地址
 *    - final int num = 11; 常量,在常量池内
 *    - Java代码在进行编译的时候，没有连接这一步骤,而是在虚拟机加载class文件的时候进行动态链接。
 *      Class文件中不会保存各个方法、字段的布局信息。因此这些字段，方法的符号引用不经过运行期转换的话无法得到真正的内存入口地址
 *      也就无法直接被虚拟机使用
 * 4. 访问标志
 *  ```
 * 标志名称	标志值	含义
 * ACC_PUBLIC	    0x0001	标志为public类型
 * ACC_FINAL	    0x0010	标志被声明为final，只有类可以设置
 * ACC_SUPER	    0x0020	标志允许使用invokespecial字节码指令的新语义，JDK1.0.2之后编译出来的类的这个标志默认为真。（使用增强的方法调用父类方法）
 * ACC_INTERFACE	0x0200	标志这是一个接口
 * ACC_ABSTRACT	    0x0400	是否为abstract类型，对于接口或者抽象类来说，次标志值为真，其他类型为假
 * ACC_SYNTHETIC	0x1000	标志此类并非由用户代码产生（即：由编译器产生的类，没有源码对应）
 * ACC_ANNOTATION	0x2000	标志这是一个注解 带ACC_INTERFACE
 * ACC_ENUM	0x4000	标志这是一个枚举
 *  ```
 *  - 标识并集
 *  - ACC_开头的常量
 *  - 通过设置访问标记的32位中的特定位
 *  - 使用ACC_SUPPER可以让类更准确的定位到父类的方法super.method()
 *  - class文件被设置为ACC_INTERFACE标志,那么同时也得设置ACC_ABSTRACT标识,
 *    同时不能设置为ACC_FINAL、ACC_SUPER、ACC_ENUM标志
 *  - 如果没有ACC_INTERFACE标志,这个class文件可以设置除ACC_ANNOTATION外的其他所有标志
 *    ACC_ABSTRACT和ACC_FINAL除外
 *  - 在jclasslib一般信息中
 * 5. 类索引，父类索引，接口类索引
 * 6. 字段表集合
 * 7. 方法表集合
 * 8. 属性表集合
 */
public class ByteCodeTest {
    public static void main(String[] args) {
        Father f = new Son();
        System.out.println(f.x);
    }
}
class Father {
    int x =10;
    public Father(){
        this.print();
        x = 20;
    }
    public void print(){
        System.out.println("Father.x = " + x);
    }
}
class Son extends Father {
    int x = 30;
    public Son(){
        this.print();
        x = 40;
    }
    public void print(){
        System.out.println("Son.x = "+x);
    }
}
