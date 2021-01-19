package com.jvm.jvmlearn.jmm;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionTest {
    public static void main(String[] args) {
        //初始化大小为10,扩容后的大小= oldCapacity + oldCapacity >> 1
        List<String> a0 = new ArrayList<>();
        //初始化大小为0,不扩容
        List<String> a1 = new LinkedList<>();
        //初始化大小为10,扩容后大小 = oldCapacity + oldCapacity    synchronized
        List<String> a2 = new Vector<>();
        //末尾添加
        List<String> a3 = new CopyOnWriteArrayList<>();
        //扩容和树形化
        //初始化为16,扩容为2倍
        //查过阈值,且落到key的桶上,扩容
        //否则:
        //桶中元素大于8,且总容量大于64,使用红黑树
        //桶中元素小于6,退化为链表
        //哈希表的最小树形化容量64
        Map<String,Object> m0 = new HashMap<>();
        Map<String,Object> m1 = new LinkedHashMap<>();
        Map<String,Object> m2 = new Hashtable<>();
        //throw "a";
    }

}
