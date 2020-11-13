package com.jvm.jvmlearn.garbagecollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
 * 七。G1回收器:区域化分代式 (Garbage First)
 *    - -XX:+UseG1GC
 *    - 特点
 *     1. 并行与并发
 *        - 并行性: 多个GC线程同时工作,有效利用多核计算能力,STW
 *        - 并发性: G1拥有与应用程序交替执行的能力,部分工作可以和应用程序同时执行
 *     2. 分代收集
 *        - 堆空间分成若干区域region,同时兼顾年轻代和老年代
 *     3. 空间整合
 *        - region之间是复制算法,整体上是标记-压缩算法
 *     4. 可预测的停顿时间模型(软实时 soft real-time)
 *        - 优先列表,优先回收价值最大的Region
 *    - 缺点

 *     1. -XX:+UseG1GC 手动指定使用G1收集器执行内存回收任务
 *     2. -XX:G1HeapRegionSize 设置每个region的大小,值是2的幂,范围是1MB到32MB之间,
 *        目标是根据最小的java堆划分出约2048个区域,默认是堆内存的1/2000
 *     3. -XX:MaxGCPauseMillis 设置期望达到的最大GC停顿时间指标。默认200ms
 *     4. -XX:ParallelGCThread 设置STW时线程数的值,最多为8
 *     5. -XX:ConcGCThreads 设置并发标记的线程数.n=(ParallelGCThreads)/4
 *     6. -XX:InitiatingHeapOccupancyPercent 设置除非并发GC周期的java堆占用率阈值，默认45
 *    - 步骤（Young GC 、Mixed GC 、Full GC）
 *     1. 开启G1垃圾收集器
 *     2. 设置堆的最大内存
 *     3. 设置最大停顿时间
 *    - G1回收器的适用场景
 *     1. 大内存、多处理器
 *     2. 低GC延迟、大堆的应用程序
 *     3. 替换JDk1.5中的CMS收集器
 *       - 超过50%的java堆被活动数据占用
 *       - 对象分配频率或年代提升频率变化很大
 *       - GC停顿时间过长(长于0.5~1秒)
 *     4. 采用应用线程承担后台运行的GC工作
 *   - 分区Region
 *    1. eden
 *    2. survivor
 *    3. old
 *    4. Humongous 存储大对象 >=1.5个region
 *       连续H存放,充当老年代
 *   - 回收过程
 *    1. 年轻代GC
 *    2. 年轻代GC + 老年代并发标记过程
 *    3. 混合回收
 *    4. Full GC
 *  - Remembered Set region之间的引用
 *    1. 每个Region都有一个记忆集
 *    2. WRITE BARRIER
 *    3. CardTable
 *    4. 在GC根节点的枚举范围加入Remembered Set,不用全局扫描
 * 八。垃圾回收器总结
 *  -
 *   1. -XX:+PrintGC -verbose:gc
 *   2. -XX:+PrintGCDetails
 *   3: -XX:+PrintGCTimeStamps jdk8
 *   4: -XX:+PrintGCDateStamps jdk8
 *   5. -XX:+PrintHeapAtGC jdk8
 *   6. -Xlog:gc:../logs/gc.log 日志文件的输出路径
 *  - 【名称:GC前内存占用->GC后内存占用(该区域内存总大小)
 * 九。GC日志分析
 *  - gcviewer
 * 十。垃圾回收器的新发展
 *
 *
 *
 */

// -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC
public class GCCollectorTest {
    private  byte[] buffer = new byte[1024 * 1024 * 20];
    public static void main(String[] args) {
        List<GCCollectorTest> l_gc = new ArrayList<GCCollectorTest>();
         while (true){
            l_gc.add(new GCCollectorTest());
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }
}

// -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC
class GCLogTest1 {
    private static final int _1MB=1024*1024;

    /**
     * jdk7 新生代4g 老年代6g
     * jdk8 新生代6g 老年代4g
     * @param args
     */
    public static void main(String[] args) {
        byte[] allocation1,allocation2,allocation3,allocation4;
        allocation1 = new byte[2*_1MB];
        allocation2 = new byte[2*_1MB];
        allocation3 = new byte[2*_1MB];
        allocation4 = new byte[4*_1MB];
    }
}