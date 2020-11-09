package com.jvm.jvmlearn;

import java.util.ArrayList;

/**
 * 执行引擎
 * 1. JIT编译器  (https://www.bilibili.com/video/BV1PJ411n7xZ?p=111)
 * 1） 解释器: 对字节码采用逐行解释的方式执行
 * 2） 编译器: 虚拟机将源代码直接编译成本地机器平台相关的机器语言
 * 3） jconsole
 * 4) 热点代码及其探测方法
 * - 栈上替换OSR
 * - HotSpot VM采用基于计数器的热点探测
 * - 方法调用计数器 client 1500次,server 10000次 -XX:CompileThreshold
 * - 热度衰减 半衰周期
 * a. -XX:-UseCounterDecay 关闭热度衰减
 * b. -XX:CounterHalfLifeTime 设置半衰周期的时间
 * - 回边计数器
 * 2. 解释器
 * 1） 翻译者: 将字节码文件中的内容翻译为对应平台的本地机器指令执行
 * 2） 分类:
 * - 字节码解释器
 * - 模板解释器
 * - Interpreter模块:实现了解释器的核心功能
 * - Code模块:用于管理HotSpot VM在运行时生成的本地机器指令
 * 3. HotSpot VM可以设置程序执行方式
 * 1） -Xint:完全采用解释器模式执行程序
 * 2） -Xcomp: 完全采用即时编译器模式执行程序。如果即时编译出现问题,解释器会介入执行
 * 3） -Xmixed: 采用解释器 + 即时编译器的混合模式共同执行程序
 * <p>
 * 4. C1和C2编译器不同的优化策略
 * 1）C1  -client 简单可靠的优化,耗时短
 * - 方法内联:将引用的函数代码编译到引用点处,这样可以减少栈帧的生成,减少参数传递以及跳转过程
 * - 去虚拟化:对唯一的实现类进行内联
 * - 冗余消除:在运行期间把一些不会执行的代码折叠掉
 * 2）C2 -server 耗时较长的优化,以及激进优化
 * - 标量替换
 * - 栈上分配
 * - 同步消除
 * - 同步消除：清除同步操作,通常指synchronized
 * <p>
 * 5. JDK10之后,引入Graal编译器 与C1 C2并列
 * - XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompliler
 * 6. AOT(Ahead of Time Compiler)编译器 同hotspot并列
 * - jaotc
 */
public class EngineTest {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add("hhhhhhhhhhhhhh");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
