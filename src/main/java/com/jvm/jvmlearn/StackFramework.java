package com.jvm.jvmlearn;

/**
 * 一. 栈帧的内部结构
 * 1. 局部变量表
 * 数字数组,主要用于存储方法参数和定义在方法体内的局部变量,基本数据类型，对象引用，returnAddress
 * 大小在编译期确定下来,保存在方法的Code属性的maximum local variables数据项中
 * 2. 操作数栈
 * 临时值 ipush
 * 1) 保存计算过程的中间结果,同时作为计算过程中变量临时的存储空间
 * 2） 当一个方法开始执行的时候,新的栈帧被创建,该方法的操作数栈是空的
 * 3） 操作数栈深度在编译期定义好了,保存在Code属性中,为max_stack的值 如何确定？
 * 4) 32bit 一个栈单位深度 64 两个
 * 3. 动态链接(或指向运行时常量池中该帧所属方法的引用) 实现方法动态链接
 * 所有的类变量和方法引用都作为符号引用保存在class文件的常量池中
 * 4. 方法返回地址(或方法正常退出或异常退出的定义)
 * 5， 一些附加信息
 * 3,4,5 帧数据区
 * startPC + length 变量作用域范围 == code length
 * <p>
 * 二. slot(code->LocalVariableTable)
 * 1. 局部变量表,最基本的存储单元是slot
 * 2. 基本数据类型，对象引用，returnAddress
 * 3. 32位以内的类型只占用一个slot(包括returnAddress),
 * 64位的类型(long 和 double)占用两个slot
 * 4. 当前帧由构造方法或实例方法创建  this占用slot索引为0空间
 * <p>
 * 三.变量类型
 * 根据数据类型分 ① 基本数据类型 ② 引用数据类型
 * 根据声明位置 ① 成员变量: 在使用前,都经过默认初始化赋值
 * 类变量: linking的prepare阶段:给类变量默认赋值  方法区
 * --> initial阶段:给变量显示赋值即静态代码块赋值
 * 实例变量:随着对象的创建,会在堆空间中分配实例变量空间,并进行默认赋值 堆中
 * ② 局部变量: 在使用前,必须要显式赋值
 * 作为垃圾回收根节点
 * <p>
 * 四. 代码追踪 PCRegister
 * istore_i ,iload_i 保存/获取在局部变量表i位置
 * bipush,iadd 等对操作数栈
 * ireturn 将返回值压入当期栈帧的操作数栈中
 * <p>
 * 五. 栈顶缓存(ToS,top of stack cached)
 * 1. 零地址指令
 * 2. 将栈顶元素全部缓存在物理CPU的寄存器中,
 * 以此降低对内存的读写次数,提升执行引擎的执行效率
 * <p>
 * 六. 静态绑定和动态绑定
 * 1. 静态绑定 --- 早期绑定
 * 1). 目标方法在编译器可知
 * 2). 运行期间保存不变
 * 2. 动态绑定 --- 晚期绑定
 * 1). 被调用方法在编译器无法被确定下来
 * <p>
 * <p>
 * 七。线程安全
 * 线程共享，不考虑同步，线程不安全
 * 八。本地方法
 * 1. native 不可与 abstract共用
 * 2. java实现jre的与底层系统的交互,甚至jvm的一些部分就是c写的
 * 3. sun的解释器是用c实现的
 * 九。本地方法栈
 * 1. 线程私有的
 * 2. 固定或者可动态扩展的内存大小
 * 3. 本地方法是使用c语言实现的
 * 4. 线程调用一个本地方法时，它就进入一个全新的病且不受虚拟机限制的
 * 世界，与虚拟机有相同的权限
 * 1） 可通过本地方法接口访问虚拟机内部的运行时数据区
 */
public class StackFramework {
    private int a;
    private int b;

    public static void main(String[] args) {

    }

    public void test() {

    }

    public int test(int a) {
        return a;
    }

    public void test1() {
        int a = 0;
        {
            int b = 0;
            b = a + 1;
        }
        //变量c使用之前已经销毁的变量b占据的slot的位置
        int c = a + 1;
    }

    StackFramework(int a, int b) {
        this.a = a;
        this.b = b;
    }
}
