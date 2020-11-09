package com.jvm.jvmlearn;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * 对象的实例化
 * 1. 创建对象的方式
 * 1) new、变形1:Xxx的静态方法、变形2:XxxBuilder/XxxFactory的静态方法
 * 2) Class的newInstance() - 反射的方式,只能调用空参构造器,权限必须是public
 * 3) Constructor的newInstance(Xxx) - 反射的方式,可以调用空参、带参的构造器,权限没有要求
 * 4) 使用clone() - 不调用任何构造器,当前类需要实现Cloneable接口,实现clone()
 * 5) 使用反序列化 - 从文件中、网络中获取一个对象的二进制流
 * 6) 第三方库Objenesis
 * 2. 创建对象的步骤
 * 1) 判断对象对应的类是否是加载、链接、初始化
 * 2) 为对象分配内存 https://www.bilibili.com/video/BV1PJ411n7xZ?p=104
 * a. 内存规整 -- 指针碰撞
 * b。内存不规整 -- 虚拟机需要维护一个列表、空闲列表分配
 * c. 说明
 * 3) 处理并发安全问题
 * a. 采用CAS配上失效重试保证更新的原子性
 * b. 每个线程预先分配一块TLAB  通过-XX:+/-UseTLAB参数设置,默认+
 * 4) 初始化分配到的空间 -- 所有属性设置默认值，保证对象实例字段在不赋值时可以直接使用
 * 5) 设置对象的对象头 -- 所属类(类的元数据信息),对象的HashCode和对象的GC信息、锁信息
 * 6) 执行init方法进行初始化
 * 实例变量-代码块-构造函数
 * https://www.bilibili.com/video/BV1PJ411n7xZ?p=105
 * 3. 对象的内存布局
 * 1) 对象头
 * a. 运行时元数据
 * - 哈希值
 * - GC分代年龄
 * - 锁状态标志
 * - 线程持有的锁
 * - 偏向线程ID
 * - 偏向时间戳
 * b. 类型指针 -- 指向元数据InstanceKlass,  getClass()
 * 确定该对象所属的类型
 * c. 如果是数组,还需要记录数组的长度
 * 2) 实例数据
 * a. 说明 -- 它是对象真正存储的有效信息,
 * 包含程序代码中定义的各种类型的字段(包括从父类继承下来的和本身拥有的字段)
 * b. 规则
 * - 相同宽度的字段总被分配在一起
 * - 父类中定义的变量会出现在子类之前
 * - 如果CompactFields参数为true:子类的窄变量可能插入到父类变量的空隙
 * 3) 对齐填充
 * - 不是必须的,占位符的作用
 * 4) 提示
 * 对象访问定位：【栈帧(reference)】->【堆区(InstanceOopDesc)】->【方法区(InstanceKlass)】
 * <p>
 * 4. 直接内存
 * 1)IO               NIO(Non-Blocking IO)  通过存在于堆中的DirectByteBuffer操作Native内存
 * byte[]/char[]    Buffer
 * Stream           Channel
 */
public class ObjectInsTest {
    /*    int id =1001;
        String name;
        Object abj;
        {
            name = "匿名客户";
        }
        public ObjectInsTest(){
            abj = new Object();
        }
        public static void main(String[] args) {
            Object obj = new Object();
        }
    */
    private static final int BUFFER = 1024 * 1024 * 1024;
    private static final long _1MB = 1024 * 1024;

    public static void main(String[] args) throws IllegalAccessException {
        /**
         * 直接分配内存空间ByteBuffer.allocateDirect()
         * 也要可能导致OutOfMemoryError异常 Direct buffer memory
         * 分配回收成本较高
         * 不受jvm内存回收管理
         * 通过 MaxDirectMemorySize设置,默认 -Xmx一致
         * unsafe
         */
        /*
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER);
        System.out.println("直接内存分配完毕,请求指示！");

        Scanner scanner = new Scanner(System.in);
        scanner.next();

        System.out.println("直接内存开始释放");
        byteBuffer = null;
        System.gc();
        scanner.next();
        */

        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) unsafe.allocateMemory(_1MB);

    }
}
