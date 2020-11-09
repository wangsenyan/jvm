package com.jvm.jvmlearn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * 设置堆空间 -Xms10m -Xms10m 初始和最大
 * -XX +PrintGCDetails 打印GC消息
 * jvisualvm.exe
 * 所有的线程共享堆，在这里可以划分线程私有的缓冲区(TLAB)
 * 逃逸分析--数组和对象可能在栈上分配
 * <p>
 * 一。内存细分
 * 1. 新生区(年轻代)
 * 1） 伊甸区Eden
 * a. 几乎所有的对象都在Eden区分配的
 * b. 绝大部分的Java对象的销毁都在新生代进行了 80%
 * c. 使用-Xmn设置新生代的内存大小(以此为准)
 * <p>
 * 2）from
 * 3) to 复制之后有交换,谁空谁是to
 * 2. 养老区(老年代)
 * 3. 永久区(PermSpace)->元空间(MetaSpace) 8版本及之后
 * <p>
 * 二。堆空间大小的设置
 * 1. -Xms  -XX:InitialHeapSize -Xms6291456 -Xms6144k -Xms6m
 * 2. -Xmx  -XX:MaxHeapSize
 * -X 是jvm的运行参数
 * ms 是memory start
 * mx
 * 3. -XX:NewRatio=2 新生代:老年代=1:2 默认是2
 * -XX:-UseAdaptiveSizePolicy 减号代表关闭自适应内存分配策略
 * -XX:SurvivorRatio=8 指定自适应比例
 * Eden:s0:s1=8:1:1
 * jps  ->  jinfo -flag NewRatio xxx
 * 一旦堆区中的内存大小超过 -Xmx指定的大小，报错OOM,建议初始和最大设置为相同
 * <p>
 * 3. 默认 初始为可用物理电脑内存大小/64
 * 最大为可用物理内存大小/4
 * Runtime.getRuntime().totalMemory()/1024/1024
 * Runtime.getRuntime().maxMemory()/1024/1024
 * 4. 查看
 * 1） jps --> jstat -gc 21688
 * 2） -XX:+PrintGCDetails  -Xlog:gc*
 * 3) S0C/S1C 只能选其一放一数据
 * 5, 异常 Throwable( 异常exception与错误error)
 * <p>
 * 二。对象分配过程
 * 1。 -XX:MaxTenuringThreshold=<N> 设置新生代和老生代划分阈值
 * 2. YGC:对新生代堆进行gc,FGC:全堆范围的gc
 * <p>
 * 三。常见调优工具
 * 1. JDK命令行
 * 2. Eclipse:Memory Analyzer Tool
 * 3. Jconsole
 * 4. VisualVM
 * 5. Jprofiler
 * 6. Java Flight Recorder
 * 7. GCViewer
 * 8. GC Easy
 * <p>
 * 四。垃圾回收算法
 * 1. Minor GC YGC 用户线程暂停
 * 2. Major GC OGC 老年代的垃圾收集 只要CMS GC才单独OGC
 * 3. Full GC 收集整个java堆和方法区的垃圾收集
 * 1） System.gc() 触发
 * 2） 老年代空间不足
 * 3） 方法区空间不足
 * 4） 通过Minor GC后进入老年代的平均大小大于老年代的可用内存
 * 5） 由Eden区，From Space 区向To Space区复制时，对象大小大于To Space可用内存
 * 则把该对象转存到老年代,且老年代的可用内存小于该对象大小
 * 4. Mixed GC 混合收集:收集整个新生代以及部分老年代的垃圾回收，只有G1 GC会有这种行为
 * 5. Eden满触发Minor GC,Survivor满不会引发GC
 * 6. Minor GC 会引发STW,暂停用户进程
 * 7. Major GC 经常会伴随至少一次的Minor GC
 * 8. 动态对象年龄判断
 * 如果survivor区中相同年龄的所有对象大小的总和大于survivor空间的一半，
 * 年龄大于或等于该年龄的对象可以直接进入老年代，无须等到MaxTenuringThreshold中要求的年龄
 * 9。空间分配担保 -XX:HandlePromotionFailure
 * <p>
 * 五。TLAB 默认开启,占Eden 1%
 * 1. jinfo -flag UseTLAB 10676 查看是否启动TLAB
 * 2. -XX:TLABWasteTargetPercent 设置TLAB空间所占用Eden空间的百分比大小
 * <p>
 * 六。参数设置 https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html
 * 1. -XX:+PrintFlagsInitial 查看所有的参数的默认初始值
 * 2. -XX:+PrintFlagsFinal 查看所有的参数的最终值
 * 3. -Xms 初始堆空间内存 默认物理内存1/64
 * 4. -Xmx 最大堆内存空间 默认为1/4
 * 5. -Xmn 设置新生代的大小 初始值及最大值
 * 6. -XX:NewRatio 配置新生代与老年代在堆结构的占比
 * 7. -XX:SurvivorRatio 设置新生代中Eden和s0/s1空间的占比
 * 8. -XX:MaxTenuringThreshold 设置新生代垃圾的最大年龄
 * 9. -XX:+PrintGCDetails 输出详细的GC处理日志
 * -XX:+PrintGC -verbose:gc
 * 10. -XX:HandlePromotionFailure 是否设置空间分配担保
 * 只要老年代的连续空间大于新生代对象总大小或者历次晋升的平均大小就会进行Minor GC,否则进行Full GC
 * <p>
 * 七，逃逸分析
 * 1. 是对象本身是否被在外部引用
 * -XX:+DoEscapeAnalysis
 * 2. 如果一个对象只能在线程内访问到，可以不考虑同步 锁消除
 * 3. 分离对象或标量替换(Java中的原始数据类型就是标量)
 * -XX:+EliminateAllocations
 * HotSpot 对象都在堆上创建
 * 4. -server -Xmx100m -Xmx100m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations
 * 1) -server 只有在server端能开启
 */

public class HeapTest {
    byte[] buffer = new byte[new Random().nextInt(1024 * 1024 * 20)];

    public static void main(String[] args) throws InterruptedException {
        //long initialMemory = Runtime.getRuntime().totalMemory()/1024/1024;
        //long maxMemory = Runtime.getRuntime().maxMemory()/1024/1024;
        ArrayList<HeapTest> list = new ArrayList<HeapTest>();
        while (true) {
            list.add(new HeapTest());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
