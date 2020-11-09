package com.jvm.jvmlearn.garbagecollector;

/**
 *
 * 修改jdk依赖
 * 1. pom.xml
 * 2. Project Struct
 *   - project
 *   - module
 * 3. Edit Configure
 * 垃圾回收器 -XX:+PrintCommandLineFlags
 * 一。GC分类与性能指标
 * 1. Java不同版本的新特性
 *  1) 语法层面:Lambda表达式、switch、自动装填、自动拆箱、enum、<>...
 *  2) API层面:Stream API，新的日期时间,Optional,String,集合框架
 *  3) 底层优化: JVM的优化,GC的变化,元空间,静态域,字符串常量池
 * 2. 垃圾回收器分类
 *  1）
 *    - 标记-压缩:指针碰撞
 *    - 标记-清除:空闲列表
 * 二。不同的垃圾回收器概述
 *  1. 评估GC的性能指标
 *   1） 吞吐量: 运行用户代码的时间占总运行时间的比例
 *   2） 垃圾收集开销: 吞吐量的补数,垃圾收集所用时间与总运行时间的比例
 *   3） 暂停时间:执行垃圾收集时,程序的工作线程被暂停的时间
 *   4） 收集频率:相对于应用程序的执行,收集操作发生的频率
 *   5） 内存占用:Java堆区所占的内存大小
 *   6） 快速:一个对象从诞生到被回收所经历的时间
 * 2。不可能三角
 *   1）吞吐量
 *   2）暂停时间
 *   3）内存占用
 * 三。Serial回收器:串行回收
 * 1. -XX:+UseSerialGC 会默认使用Serial Old
 * 四。ParNew回收器:并行回收
 * 1. -XX:+UseParNewGC 在jdk11移除
 * 五、Parallel回收器:吞吐量优先
 * 1. -XX:+UseParallelGC
 * 2. -XX:+UseParallelOldGC  上面两个互相激活
 * 3. -XX:ParallelGCThreads 当CPU数量大于8个,ParallelGCThreads的值等于3+[5*CPU_Count]/8
 * 4. -XX:MaxGCPauseMillis 设置垃圾收集器最大停顿时间(ms)
 * 5. -XX:GCTimeRatio 垃圾收集时间占总时间的比例(吞吐量)
 *    范围(0,100) 默认99,即垃圾回收时间不超过1%
 * 6. -XX:+UseAdaptiveSizePolicy 设置Parallel Scavenge收集器具有自适应调节策略
 * 六。CMS回收器:低延迟
 *  1. 第一款并行(执行线程和回收线程同时运行)
 *  2. 无法与Parallel Scavenge 配合使用,新生代只能与ParNew 与 Serial 一起使用
 *  3. 原理(标记清除)
 *    - 初始标记 STW
 *      - 仅仅是标记处GC Roots能直接关联到的对象
 *    - 并发标记
 *      - 从GC Roots直接关联对象开始遍历整个对象图的过程,不需要STW
 *    - 重新标记(Remark) STW
 *      - 修正并发标记期间,用户程序继续运行而导致标记产生标记的那一部分对象的标记记录 STW
 *    - 并发清理(Concurrent - Sweep)
 *      - 清理删除标记阶段判断的已经死亡的对象,释放内存空间
 *    - 重置线程
 *  4. 由于最耗时的并发标记和并发清除都不需要暂停工作,所以整体的回收是低停顿的
 *  5. 当堆内存使用率达到某一阈值时,便开始进行回收(回收的时候用户线程需要有足够的内存可用)
 *  6. 弊端
 *    - 会产生内存碎片
 *    - 对CPU资源非常敏感,导致吞吐量下降
 *      -XX:ParallelCMSThreads 设置CMS的线程数量,默认启动线程数是(ParallelGCThreads+3)/4
 *          ParallelGCThreads是年轻代并行收集器的线程数
 *      -XX:CMSFullGCsBeforeCompaction 设置执行多少次Full GC后对内存空间进行压缩整理
 *    - 无法处理浮动垃圾(Concurrent Mode Failure)
 *       在并发标记阶段如果产生新的垃圾对象,CMS将无法对这些垃圾对象进行标记
 *  7. CMS收集器可以设置的参数(jdk8之后移除)
 *    - -XX:+UseConcMarkSweepGC == ParNew(Young区)  + CMS(old区) + Serial Old
 *    - -XX:CMSInitiatingOccupancyFraction 设置堆内存使用率的阈值
 *    - -XX:+UseCMSCompactAtFullCollection 指定在执行完Full GC后对内存空间进行压缩整理
 *    - -XX:CMSFullGCsBeforeCompaction 设置执行多少次Full GC后对内存空间进行压缩整理
 *    - -XX:ParallelCMSThreads 设置CMS的线程数据
 * 七。G1回收器:区域化分代式
 * 八。垃圾回收器总结
 * 九。GC日志分析
 * 十。垃圾回收器的新发展
 */
public class GCCollectorTest {
    public static void main(String[] args) {
     while (true){

     }
    }
}
