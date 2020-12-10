package com.jvm.jvmlearn.bytecode;

import org.junit.Test;

/**
 * 1. 运算时的溢出
 *  - 只除数为0的情况,抛出ArithmeticException
 * 2. 运算模式
 *  - 向最近数舍入模式
 *  - 向零舍入模式
 * 3. NaN值
 *  - 溢出，用有符号的无穷大 Infinity
 *  - 结果没明确的数学定义,NaN
 *
 * 二。算术指令
 * 1. 加法指令 iadd,ladd,fadd,dadd
 * 2. 减法指令 isub,lsub,fsub,dsub
 * 3. 乘法指令 imul,lmul,fmul,dmul
 * 4. 除法指令 idiv,ldiv,fdiv,ddiv
 * 5. 求余指令 irem,lrem,frem,drem  # remainder:余数
 * 6. 取反指令 ineg,lneg,fneg,done  # negation:取反
 * 7. 自增指令 iinc
 * 8. 位运算指令
 *   - 位移指令 ishl,ishr,iushr,lshl,lshr,lushr
 *   - 按位或指令 ior,lor
 *   - 按位与指令 iand,land
 *   - 按位异或指令 ixor,lxor
 * 9. 比较指令 dcmpg,dcmpl,fcmpg,fcmpl,lcmp
 */
class Arithmetic {

    @Test
    public void method1(){
        int i=10;
        double j = i/0.0; //将0.0看成极小值
        System.out.print(j);//infinity

        double d1 = 0.0;
        double d2 = d1/0.0; //两个极小值相除,值不确定
        System.out.println(d2); //NaN
    }
    public void  method2(){
        float i = 10;
        float j= -i;
        i=-j;
    }
    public void method3(int j){
        int i=100;
        i=i+10;// iadd
        i+=10;// iinc 2 by 10
    }
    public int method4(){
        int a=80;
        int b=7;
        int c=10;
        return (a+b)*c;
    }

    /**
     * 必须从操作数栈取临时变量
     * 取反 = 与-1取异或
     *  0 iload_1
     *  1 iload_2
     *  2 iadd
     *  3 iconst_1
     *  4 isub
     *  5 iload_2
     *  6 iconst_1
     *  7 isub
     *  8 iconst_m1
     *  9 ixor
     * 10 iand
     * 11 ireturn
     */
    public int method5(int i,int j){
        return ((i+j-1) & ~(j-1));
    }
}
