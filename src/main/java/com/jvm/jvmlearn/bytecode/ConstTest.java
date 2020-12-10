package com.jvm.jvmlearn.bytecode;

import java.util.Date;

/**
 * 常量入栈指令
 * 一。指令 <>内为数据值
 * 1. iconst_<i> (i从-1到5） iconst_m1 将-1压入操作数栈
 * 2. lconst_<l> (l从0到1)
 * 3. fconst_<f> (f从0到2)
 * 4. dconst_<d> (d从0到1)
 * 5. aconst_null
 *
 * push系列
 * 1. bipush (byte push) [-128,127]
 * 2. sipush (short push) [-32768,32767]
 *
 * ldc系列
 * 1. ldc:接收一个8位的参数,该参数指向常量池中的int,
 *    float或String的索引,将制定的内容压入堆栈
 * 2. ldc_w: 接收两个8位参数,支持范围大于ldc
 * 3. ldc2_w: 压入的元素为long或者double类型
 *
 * 保存到局部变量表
 * istore_<i> (从0搭3)
 * lstore_<i>
 * fstore_<i>
 * dstore_<i>
 * astore_<i> string
 *
 * xstore i (大于3)
 */
public class ConstTest {
    public void load(int num,Object obj,long count,boolean flag,short[] arr){
        System.out.println(num);
        System.out.println(obj);
        System.out.println(count);
        System.out.println(flag);
        System.out.println(arr);
    }
    //常量入栈指令
    public void pushConstLdc(){
        int a=-1;
        int b=6;
        int c=127;
        int d=128;
        int e=32767;
        int f=32768;
    }
    //ldc
    public void constLdc(){
        long a1=1;//lconst_1
        long a2=2;//ldc #8
        float b1=2;//fconst_2
        float b2 =3;//ldc #10
        double c1=1;//dconst_1
        double c2=2;//ldc2_w #11
        Date d = null;
    }

    /**
     *
     * @param k
     * @param d
     */
    public void store(int k,double d){
        k=2;
        int m=k+2; //istore 4
        long l=12; //lstore 5
        String str="demo"; //astore 7
        float f=10.0f; //fstore 8
        d=10; //dstore_2
    }
    public void foo(long l,float f){
        {
            int i=0;
        }
        {
            String s = "hello,world";
        }
    }
}
