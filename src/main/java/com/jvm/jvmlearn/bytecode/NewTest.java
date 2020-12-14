package com.jvm.jvmlearn.bytecode;

import java.io.File;

/**
 * 一。创建指令
 * new
 * - dup原因: 父类构造函数 + 存储局部变量表
 *
 * 指令：
 * 1。 new
 *     dup
 * 2. 数组
 *   - newarray
 *   - anewarray
 *   - multianewarray
 * 二。字段访问指令
 * 1. 访问类字段
 *   - getstatic
 *   - putstatic
 * 2. 访问类实例字段
 *   - getfield
 *   - putfield 引用数据类型）
 * 三。数组指令操作(a表示引用)
 *  1.  xstore -- 要求操作数栈顶元素为数组索引i,
 *                栈顶顺位第2个元素为数组引用a,
 *                弹出a,i，并将a[i]重新压入堆栈
 *    - baload(byte,boolean)
 *    - caload
 *    - saload
 *    - iaload
 *    - laload
 *    - faload
 *    - daload
 *    - aaload
 *  2.  xload -- (将一个操作数栈的值存储到数组元素中的指令)
 *               三个元素 值，索引，数组引用
 *               数组引用[索引]=值
 *    - bastore
 *    - castore
 *    - sastore
 *    - iastore
 *    - lastore
 *    - fastore
 *    - dastore
 *    -  aastore
 *
 * 3. arraylength
 *   - 弹出栈顶的数组元素,获取数组的长度,将长度压入栈
 * 四。 类型检测指令
 * 1. instanceof
 * 2. checkcast
 */
public class NewTest {
    /**
     *  0 new #2 <java/lang/Object>
     *  3 dup
     *  4 invokespecial #1 <java/lang/Object.<init>>
     *  7 astore_1
     *  8 new #7 <java/io/File>
     * 11 dup
     * 12 ldc #9 <lj.avi>
     * 14 invokespecial #11 <java/io/File.<init>>
     * 17 astore_2
     * 18 return
     */
    public void newInstance(){
        Object obj = new Object();

        File file = new File("lj.avi");
    }
    public void newArray(){
        int[] intArray = new int[10];
        Object[] objArray = new Object[10];
        int[][]mintArray = new int[10][10];
        String[][] strArray= new String[10][];
    }
    //类变量System.out
    public void sayHello(){
        System.out.println("hello");//getstatic
    }

    /**
     *  0 bipush 10
     *  2 newarray 10 (int)
     *  4 astore_1
     *  5 aload_1
     *  6 iconst_3
     *  7 bipush 20
     *  9 iastore
     * 10 getstatic #18 <java/lang/System.out>
     * 13 aload_1
     * 14 iconst_1
     * 15 iaload
     * 16 invokevirtual #31 <java/io/PrintStream.println>
     * 19 bipush 10
     * 21 newarray 4 (boolean)
     * 23 astore_2
     * 24 aload_2
     * 25 iconst_1
     * 26 iconst_1
     * 27 bastore
     * 28 return
     */
    public void setArray(){
        int [] intArray = new int[10];
        intArray[3]=20;
        System.out.println(intArray[1]);

        boolean[] arr = new boolean[10];
        arr[1]=true;
    }
    public void arrLength(){
        double[]arr = new double[10];
        System.out.println(arr.length);
    }
    public void ins(Object obj){
        if(obj instanceof String)
            obj = (String) obj;
        else
            obj  =null;
    }
}
