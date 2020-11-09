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
