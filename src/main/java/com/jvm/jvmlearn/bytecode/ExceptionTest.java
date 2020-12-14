package com.jvm.jvmlearn.bytecode;

/**
 * 一。抛出异常指令
 *  - throw(主动、被动)          --->athrow
 *  - JVM运行时异常会在其他Java虚拟机指令监测到异常状况时自动抛出
 * 二。异常及异常的处理
 *  - 抓抛模型 try-catch-finally --->使用异常表
 *  - 在抛出异常时,Java虚拟机会清除操作数栈上的所有内容,
 *    而后将异常实例压入调用者操作数栈上
 */
public class ExceptionTest {
    public void throwZero(int i){
        if(i==0)
            throw new RuntimeException("参数值为0");
    }
}
