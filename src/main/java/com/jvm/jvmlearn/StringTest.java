package com.jvm.jvmlearn;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 一。String的基本特性
 * 0. 放在常量池中
 * 1. 声明为final,不可被继承
 * 2. String实现了Serializable接口:表示字符串是支持序列化的
 * 实现了Comparable接口,可以比较大小
 * 3. final char[] value 用于存储字符串数据
 * jdk9时改为byte[]
 * 4. 字符串常量池中不会存储相同内容的字符串的
 * <p>
 * 二。String Pool
 * 1. 固定大小的Hashtable jdk7 == 60013
 * 2. -XX:StringTableSize jdk8 == 65536 最小1009
 * <p>
 * 三。String的内存分配
 * 1. java 8种基本数据类型和一种比较特殊的类型String,提供常量池
 * 2. 主要使用方法
 * - String info = "hello world";
 * - intern()方法
 * 3. StringTable为什么要由永久代到堆中?
 * - permSize默认比较小
 * - 永久代垃圾回收频率低
 * 4. String拼接
 * - 有变量,在堆中new
 * StringBuilder s = new StringBuilder()
 * s.append("a")
 * s.append("b")
 * s.toString()
 * - 如果拼接的是字符串常量 或 常量引用,任使用编译器优化,非StringBuilder
 * - 针对final修饰类、方法、基本数据类型、引用数据类型,尽量使用final
 * prepare期已经确认
 * - StringBuilder.append()自始至终只创建过一个StringBuilder对象
 * 使用String的字符串拼接方式,创建过多个StringBuilder和String对象
 * - 使用String的字符串拼接方式,内存中创建了较多的StringBuilder和String，
 * 对象内存大。可能会引发GC
 * - 在实际开发中，如果基本确定总字符串长度不高于某个值len,建议使用
 * StringBuilder = new StringBuilder(len)
 * <p>
 * 5. intern()的使用
 * - 通过equals比较 s.intern() == t.intern() 当且仅当 s.equals(t)
 * - 在常量池中查询字符串是否存在,若不存在就会将当前字符串放入常量池中
 * - 保证变量s指向的是字符串常量池中的数据
 * a. 字面量 String s = "aaaaaa";
 * b. 调用intern方法
 * String s = new String("aaaaaa").intern();
 * String s = new StringBuilder("aaaaaa").toString().intern();
 *
 * 6. String s = new String("abc");在常量池中有 “abc”
 *    String s = new String("ab") + new String("c"); 在常量池中没有“abc“
 * 四。String的G1的String去重操作
 * 1. 是对堆中数据的去重
 * 2. 队列 - hashTable
 * 3. UseStringDeduplication(bool):开启String去重,默认是不开启的,需要手动开启
 *    PrintStringDeduplicationStatistics(bool):打印详细的去重统计信息
 *    StringDeduplicationAgeThreshold(uintx):达到这个年龄的String对象被认为是去重的候选对象
 */
public class StringTest {
    String str = new String("good");
    char[] ch = {'t', 'e', 's', 't'};
    //static final int MAX_COUNT = 1000 * 10000;
    //static final String[] arr = new String[MAX_COUNT];
    /**
     * 形参的区别
     * 因为String是final,内部新建了一个新的String实例str
     */
    public void change(String str, char ch[]) {
        //相当于 String str1 = "test ok";
        str = "test  ok";
        ch[0] = 'b';
    }

    public static void main(String[] args) throws InterruptedException {
        StringTest st = new StringTest();
        //st.test4();
        //st.test6();
        //st.test5();
        st.test6();
        //st.test7();
        //st.test8();
        //st.test9();
    }

    void test2() throws InterruptedException {
        System.out.println("1");
        System.out.println("2");
        //相同的字符串字面量有相同的Unicode字符序列
        System.out.println("1");
        System.out.println("2");

        StringTest ex = new StringTest();
        ex.change(ex.str, ex.ch);
        System.out.println(ex.str);
        System.out.println(ex.ch);
        Thread.sleep(1000000);
    }

    public void test3() {
        Set<String> set = new HashSet<String>();
        long i = 0;
        while (true) {
            set.add(String.valueOf(i++).intern());
        }
    }

    void test4() {
        String a = "a";
        String b = "b";
        /**
         *   StringBuilder s = new StringBuilder()
         *   s.append("a")
         *   s.append("b")
         *   s.toString()
         *   约等于 new String("ab")
         *   StringBuffer
         */
        String ab = a + b;
        String ab1 = "a" + "b";
        System.out.println(ab == ab1);//false

        /**
         * final 导致常量引用,使用编译器优化
         */
        final String fa = "a";
        final String fb = "b";
        String fab = fa + fb;
        System.out.println(fab == ab1); //true
    }

    void test5() {
        long start = System.currentTimeMillis();
        //method1(100000);//1758
        method2(100000);//4
        System.out.println(System.currentTimeMillis() - start);

    }

    void method1(int len) {
        String a = "";
        for (int i = 0; i < len; i++) {
            a += "abc";//每次循环都创建一个StringBuilder,及new String作为返回
        }
    }

    void method2(int len) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            str.append("abc");
        }
    }

    //https://www.bilibili.com/video/BV1PJ411n7xZ?p=126
    void test6() {
        /**
         *  0 new #7 <java/lang/String>
         *  3 dup
         *  4 ldc #9 <ab>
         *  6 invokespecial #11 <java/lang/String.<init>>
         *  9 astore_1
         * 10 return
         */
        String str = new String("ab");
        /**
         *  0 new #7 <java/lang/String>                                            1个
         *  3 dup
         *  4 ldc #9 <a>                                                           2个
         *  6 invokespecial #11 <java/lang/String.<init>>
         *  9 new #7 <java/lang/String>                                            3个
         * 12 dup
         * 13 ldc #14 <b>                                                          4个
         * 15 invokespecial #11 <java/lang/String.<init>>
         * 18 invokedynamic #16 <makeConcatWithConstants, BootstrapMethods #0>
         * 23 astore_1
         * 24 return
         */
        String str1 = new String("a") + new String("b");
        System.out.println(str == str1);
    }

    /**
     * jdk6 false false
     * jdk7/8 false true
     */
    void test7() throws InterruptedException {
        /**
         *  0 new #7 <java/lang/String>
         *  3 dup
         *  4 ldc #36 <1>
         *  6 invokespecial #11 <java/lang/String.<init>>
         *  9 astore_1
         */
        String s = new String("1");
        s.intern();
        /**
         * 10 ldc #36 <1>
         * 12 astore_2
         */
        String s2 = "1";
        System.out.println(s == s2);
        /**
         * new #7 <java/lang/String>
         * 16 dup
         * 17 ldc #43 <2>
         * 19 invokespecial #11 <java/lang/String.<init>>
         *
         * 22 new #7 <java/lang/String>
         * 25 dup
         * 26 ldc #115 <3>
         * 28 invokespecial #11 <java/lang/String.<init>>
         *
         * 31 invokedynamic #81 <makeConcatWithConstants, BootstrapMethods #0>
         * 36 astore_3
         *    引用复制一份？？？
         *    疑问?常量池中有没有23????
         */
        //new String("2") + new String("3");没有在常量池中生成23
        String s3 = new String("2") + new String("3");
        //Thread.sleep(100000);
        s3.intern();
        /**
         * 37 ldc #117 <23>
         * 39 astore 4
         */
        String s4 ="23";
        /**
         * 41 return
         */
        System.out.println(s3==s4);
        //Thread.sleep(100000);
    }

    /**
     * 常量池中引用  new String("ab")
     * https://www.bilibili.com/video/BV1PJ411n7xZ?p=129
     */
    void test8(){
        //String x = new String("ab");
        //String x = "ab"; // true false
        String s = new String("a") + new String("b");
        String s2 = s.intern();
        System.out.println(s2 == "ab"); //jdk8 true
        System.out.println(s =="ab");   //jdk8 true

//        String s3 = "bc";
//        String s4 = new String("b") + new String("c");
//        String s5 = s4.intern();
//        System.out.println(s3 == s5);//true
//
//        String s6 = new String("cd");
//        String s7 = "cd";
//        System.out.println(s6 == s7);//false
    }

    /**
     * intern()更节省空间,当有大量重复的字符串
     * 堆中的对象创建完后销毁
     * -Xms15m -Xmx15m -XX:+PrintStringTableStatistics -XX:+PrintGCDetails
     */
    void test9(){
        //Integer[] data = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        //long start  = System.currentTimeMillis();
        //for(int i=0;i<MAX_COUNT;i++){
            //arr[i] = new String(String.valueOf(data[i%data.length]));
            //String.valueOf().intern()
            //arr[i]=new String(String.valueOf(data[i%data.length])).intern();
        //}
        //long end = System.currentTimeMillis();
        //System.out.println("花费时间为"+ (end -start));
//        try {
//            Thread.sleep(1000000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //System.gc();
        //openjdk.java.net/jeps/192
        for(int i=0;i<100000;i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String.valueOf(i).intern();
        }
    }
}