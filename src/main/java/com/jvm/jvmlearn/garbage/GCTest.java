package com.jvm.jvmlearn.garbage;

/**
 * 一。经典三问
 * 1. 哪些内存需要回收
 * 2. 什么时候回收
 * 3. 如何回收
 * 二。常见问题
 * 1。什么是垃圾
 *  1）在运行程序中没有任何指针指向的对象
 * 2. 为啥GC
 *  1）空间用完了
 *  2）分配大空间
 *  3）效率问题
 *  4）保证程序的正常进行
 * 3。主动释放内存 C++
 *  1） 内存泄漏:不用了但是不能被清除
 * 4. 回收算法
 *  1） 标记阶段:引用计数算法
 *    - 引用计数器属性
 *    - 优点
 *      - 实现简单，便于辨识
 *      - 判定效率高，回收没有延迟性
 *    - 缺点
 *      - 存储空间的开销
 *      - 加、减增加时间开销
 *      - 无法处理循环引用,内存泄漏
 *  2） 标记阶段:可达性分析算法 Java、C#
 *    - 标记的是可达对象
 *    - 根搜索算法 GC roots 追踪性
 *    - 根集合就是一组必须活跃的引用
 *    - 连通图
 *    - 优点
 *      - 解决循环引用问题
 *    - GC roots具体元素
 *      - 虚拟机栈中引用的对象
 *      - 本地方法栈内JNI(通常说的本地方法)引用的对象
 *      - 方法区中类静态属性引用的对象
 *      - 方法区中常量引用的对象
 *      - 同步锁synchronize持有的对象
 *      - java虚拟机内部的引用
 *       - 基本数据类型对应的class对象
 *       - 常驻的异常对象(NullPointerException,OutOfMemoryError)
 *       - 系统类加载器
 *      - 反应java虚拟机内部情况的JMXBean、JVMTI中注册的回调、本地代码缓存等
 *    - 注意
 *      - 除固定的GC Roots集合外《根据用户所选用的垃圾收集器以及当前回收的内存
 *        区域不同,还可以有其他对象“临时性”地加入,共同构成完整GC Roots集合。
 *        比如:分代收集和局部回收(Partial GC)
 *      - 如果一个指针,它保存了堆内存里面的对象，但是自己又不存放在堆内存里面,那它就是一个Root
 *      - 一致性要求: Stop The World
 *  3）对象的finalization机制
 *   - 对象呗销毁之前的自定义处理逻辑
 *   - 垃圾回收此对象之前,总会先调用这个对象的finalize()方法
 *   - finalize()方法允许在子类中被重写
 *     - 资源释放:关闭文件、套接字、数据库连接等
 *   - 不建议主动调用finalize()方法
 *   - 由于finalization存在,三种可能的状态
 *     - 可触及的:从根节点开始,可以到达这个对象
 *     - 可复活的:对象的所有引用都被释放,但是对象有可能在finalize()中复活
 *     - 不可触及的:对象的finalize()被调用,并且没有复活.那么就会进入不可触及状态。
 *       不可触及的对象不能被复活,finalize()只会被调用一次
 *   - 两次标记
 *    - 对象ObjA到GC Roots没有引用链,进行第一次标记
 *    - 筛选,是否有必要执行finalize()方法
 *      - ObjA没有重写finalize()方法,或已被虚拟机调用过 ->不可触及
 *      - ObjA重写finalize()方法,且未被执行过, ObjA被插入F-Queue队列,
 *        由Finalizer线程触发finalize()方法执行(第二次标记)
 *      - 如果ObjA在finalize()方法中与引用链上的任何一个对象建立联系.
 *        第二次标记时,ObjA被移出"即将回收"集合。
 *      - 之后再次出现没有引用存在的情况,直接不可触及
 *        finalize()只会被调用一次
 *  4）MAT与Jprofiler的GC Roots溯源
 *    - MAT
 *     - open query browser -> java basics -> GC roots
 *    - dump
 *      - jmap -dump:format=b,live,file=test1.bin {pid}
 *      - JVisualVM导出
 *    - JProfiler的GC溯源
 *  5）清除阶段:标记-清除算法(Mark-Sweep)
 *    - 线性遍历
 *    - 缺点
 *      - 效率不算高
 *      -  进行GC时,需要停止整个应用程序,导致用户体验差
 *      - 空闲内存不连续,产生内存碎片
 *  6）清除阶段:复制算法(Copying)
 *    - 优点
 *      - 没有标记和清除阶段,实现简单,运行高效
 *      - 保证空间的连续性,不会出现“碎片”问题
 *    - 缺点
 *      - 需要两倍的空间
 *      - G1:GC需要维护对象的引用关系,内存占用或时间开销不小
 *      - 需要复制的要小才行
 *  7）清除阶段:标记-压缩算法(Mark-Compat)
 *    - 标记阶段(可达性分析算法)
 *    - 压缩阶段(移动到一端)
 *    - 类似 标记 - 清除 - 压缩算法
 *    - 优点
 *      - 消除内存区域分散的缺点,jvm只需要持有一个内存的起始地址即可
 *      -  消除复制算法内存减半的高额代价
 *    - 缺点
 *     - 效率低于复制算法
 *     - 如果对象被其他对象引用，还需要调整引用的地址
 *     - 移动过程需要全程暂停用户的应用程序。
 *  8）分代收集算法
 *    - 目前几乎所有GC都是采用分代收集(Generational collecting)算法执行垃圾回收的
 *    - 年轻代(Young Gen) 复制算法
 *    - 老年代(Tenured Gen) 标记清除和标记整理混合
 *     - CMS基于Mark-Sweep实现的
 *       碎片问题,CMS采用基于Mark-Compact算法的Serial Old回收器作为补偿器
 *       Concurrent Mode Failure -> Serial Old ->Full GC
 *  9）增量收集算法、分区算法
 *   - 垃圾收集线程只收集一小片区域的内存空间,接下来切换到应用程序线程
 *     依次反复,直到垃圾收集完成
 *   - 缺点
 *    - 线程切换和上下文装换消耗,会使得垃圾回收的总体成本上升,造成系统吞吐量的下降
 * 5. 垃圾回收相关概念
 *  1） System.gc()
 *    - 触发 Full GC
 *    - Runtime.getRuntime().gc()
 *    - 不保证对垃圾收集器的调用
 *    - System.runFinalization() 强制执行垃圾回收
 *    - LocalVarGC.Class
 *  2) 内存溢出与内存泄漏
 *    - OOM
 *     - 没有空闲内存,并且垃圾收集器也无法提供更多内存
 *     - Java虚拟机的堆内存设置不够 -Xms,-Xmx
 *     - 代码中创建了大量大对象,并且长时间不能被垃圾收集器收集(存在被引用)
 *     - 在报OOM前,通常垃圾收集器会被触发
 *       但如果分配一个对象大小超过堆最大内存,直接报OOM
 *   - Memory Leak
 *     - 严格意义: 只有对象不会再被程序用到了,但是GC又不能回收他们的情况
 *     - 宽泛意义: 对象的生命周期非常长,不能被及时回收
 *     - 举例
 *       - 单例的生命周期和应用程序一样长
 *       - 一些提供close的资源未关闭导致内存泄漏
 *  3）Stop The World
 *   - GC roots 一直在变
 *   - 如果出现分析过程中对象引用关系还在不断变化,则分析结果的准确性无法保证
 *   - 所有GC都有STW
 *   - STW是JVM在后台自动发起和自动完成
 *   - StopTheWorld.class
 *  4) 垃圾回收的并行与并发
 *   - 并行 CPU核数
 *   - 并发
 *     用户线程和垃圾回收线程同时执行
 *
 *  5）安全点与安全区域
 *   - 选择一些执行时间较长的指令作为Safe Point,如方法调用、循环跳转和异常跳转等
 *   - 如何在GC发生时,检查所有线程都跑到最近的安全点停顿下来?
 *     - 抢先式中断(没有虚拟机采用了)
 *     - 主动式中断
 *       设置一个中断标志,各个线程运行到Safe Point的时候主动轮询这个标志,
 *       如果中断标志位真,则将自己进行中断挂起
 *  - 安全区域(Safe Region)
 *    - 安全区域是指在一段代码片段中,对象的引用关系不会发生变化,在这个区域中的任何位置开始都是安全的
 *    -
 *  6）引用
 *    - 强引用(Strong Reference)
 *     - 无论任何情况,只要强引用关系还存在,垃圾收集器永远不会回收掉被引用的对象
 *    - 软引用(SoftReference)
 *     - 内存不足即回收(第二次回收)
 *     - 通常用来实现内存敏感的缓存
 *    - 弱引用(WeakReference)
 *     - 只能生存到下一次垃圾收集之前
 *    - 虚引用(PhantomReference)
 *     - 不影响其生存时间构成,也无法通过虚引用来获得一个对象的实例
 *       目的是能在这个对象被收集器回收时收到一个系统通知
 *     - 当垃圾回收器准备回收一个对象时,如果发现它还有虚引用,
 *       就会在回收对象后,将这个虚引用加入引用队列,以通知应用程序对象的回收情况
 *       - PhantomReferenceTest.java
 *    - 终结期引用(FinalReference)
 *     - 用于实现对象的finalize()方法,也可以称为终结器引用
 *     - 无需手动编码,其内部配合引用队列使用
 *     - 在GC时,终结期引用入队。由Finalizer线程通过终结器引用找到
 *       被引用对象并调用它的finalize()方法,第二次GC时才能回收被引用对象
 */
public class GCTest {
    private byte[] bigSize = new byte[5 * 1024 * 1024];
    Object reference = null;

    public static void main(String[] args) {
        //证明:java使用的不是引用计数算法
        //python: 手动解除、弱引用weakref
        GCTest obj1 = new GCTest();
        GCTest obj2 = new GCTest();
        obj1.reference = obj2;
        obj2.reference = obj1;

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        obj1 = null ;
        obj2 = null;
        System.gc();//建议Full GC
    }
}
