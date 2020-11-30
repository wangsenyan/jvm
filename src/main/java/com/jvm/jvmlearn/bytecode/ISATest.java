package com.jvm.jvmlearn.bytecode;

/**
 * 一。字节码指令集的概述
 * 1. 字节码指令集=操作码(Opcode) 操作数(Operands)
 * 2. 操作码长度为一个字节(0~255) 指令集的操作码总数不超过256条
 * 3. 执行模型
 *  do{
 *     自动计算PC寄存器的值加1;
 *     根据PC寄存器的指示位置,从字节码流中取出操作码
 *     if(字节码存在操作码) 从字节码流中取出操作数;
 *     执行操作码所定义的操作;
 *  }while(字节码长度>0);
 *
 * 二。指令与数据类型的关系
 * 1. i int
 * 2. l long
 * 3. s short  -> int
 * 4. b byte   -> int
 * 5. c char   -> int
 * 6.   boolean-> int
 * 7. f float
 * 8. d double
 * 9. a reference
 * 10. arraylength ->数组类型的对象
 *
 * 11. 指令类型:
 *  - 加载与存储指令
 *    - 将数据从栈帧的局部变量表和操作数栈之间来回传递
 *    - 局部变量压栈指令: xload xload_<n>  x为i,l,f,d,a,n为0到3 超过3, xload n
 *    - 常量入栈指令: bipush,sipush,ldc,ldc_w,ldc2_w,
 *      aconst_null,iconst_ml,iconst_<i>
 *      lconst_<l>, fconst_<f>, dconst_<d>
 *    - 出栈装入局部变量表指令: xstore,xstort_<n> x为i,l,f,d,a,n为0到3
 *                          xastore x为i,lf,d,a,b,c,s
 *    - 扩充局部变量表的访问索引的指令 wide
 *  - 算术指令
 *  - 类型转换指令
 *  - 对象的创建于访问指令
 *  - 方法调用与返回指令
 *  - 操作数栈管理指令
 *  - 比较控制指令
 *  - 异常处理指令
 *  - 同步控制指令
 * 12. 一个指令,可以从局部变量表、常量池、堆中对象、方法调用、系统调用中等取得数据，
 *     这些数据（可能是值,可能是对象的引用）被压入操作数栈
 * 13. 一个指令,也可以从操作数栈中取出一个到多个值(pop多次),完成赋值,加减乘除,
 *    方法传参,系统调用等
 */
public class ISATest {
}
