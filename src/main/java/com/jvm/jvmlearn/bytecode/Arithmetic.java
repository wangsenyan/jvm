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
 *  - dcmpg 与 dcmpl 区别
 *   - 遇到NaN,fcmpg会压入1,而fcmpl会压入-1
 * 三。类型转换指令
 *  1. 概念
 *    -数值类型进行相互转换
 *    - 显示类型转换
 *  2. 宽化类型转换
 *    - int->long->float->double
 *      - i2l,i2f,i2d
 *      - l2f,l2d
 *      - f2d
 *    - 精度损失问题(IEEE754最近舍入模式),不会导致运行中抛出异常
 *      - int,long -> float
 *      - long -> double
 *    - 局部变量表中的槽位固定为332位
 * 3. 窄化类型转换--强化类型转换
 *   - double->float->long->int->short->byte/char
 *     - i2b,i2s,i2c
 *     - l2i
 *     - f2i,f2l
 *     - d2i,d2l,d2f
 *   - 窄化精度损失
 *     - 不同正负号，不同的数量级
 *     - 不会导致Java虚拟机抛出运行时异常
 *     - 如果浮点值为NaN,转换结果就是int或long类型的0
 *     - 浮点转int或long,不是无穷大,超出范围,转换为边界
 *     - double转换为float
 *      - 绝对值太小而无法使用float,返回float的正负0
 *      - 绝对值太大而无法使用float,返回float的正负无穷大
 *      - double类型的NaN转换为float类型的NaN
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

    /**
     * 下面的 区别是:
     * i++是先赋值给j,然后自增
     * ++a是先自增,然后赋值给b
     *
     *  0 bipush 10
     *  2 istore_1
     *  3 iload_1       !!!
     *  4 iinc 1 by 1   !!!
     *  7 istore_2
     *
     *  8 bipush 20
     * 10 istore_3
     * 11 iinc 3 by 1   !!!
     * 14 iload_3       !!!
     * 15 istore 4
     * 17 return
     */
    public void method6(){
        int i=10;
        int j=i++;

        int a=20;
        int b=++a;
    }

    @Test
    public void method7(){
        int i=10;
        //i=++i; //最终为11
        i=i++;// 最终为10
        System.out.println(i);
    }

    public static void main(String[] args) {
        Arithmetic a = new Arithmetic();
        a.method7();
        a.downCast2();
        a.downCast3();
    }

    public void upCast2(){
        int i=123123123;
        float f=i;
        System.out.println(f);;
    }
    public void downCast1(){
        int i=10;
        byte b = (byte) i;
        short s = (short) i;
        char c = (char) i;
        byte b2 = (byte) s; //i2b
        long l =10L;
        int i1 = (int)l; //l2i
        byte b1=(byte) l; //l2i,i2b
    }
    public void downCast2(){
       int i = 128;
       byte b = (byte) i;
        System.out.println(b);//-128,截取
    }
    public void downCast3(){
        double d1 = Double.NaN;
        int i = (int) d1;
        System.out.println(d1);
        System.out.println(i);

        double d2 = Double.POSITIVE_INFINITY;
        double d3 = Double.NEGATIVE_INFINITY;
        long  l = (long)d2; // Long.MAX_VALUE

        int j = (int)d2;//Integer.MAX_VALUE
        System.out.println(l);
        System.out.println(j);
        float f = (float) d2;
        float f1 = (float) d3;
        float f2 = (float) d1;
        System.out.println(f); //Infinity
        System.out.println(f1); //-Infinity
        System.out.println(f2); // NaN

    }
}
