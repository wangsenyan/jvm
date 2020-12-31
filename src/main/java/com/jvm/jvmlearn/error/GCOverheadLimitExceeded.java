package com.jvm.jvmlearn.error;

import java.util.ArrayList;
import java.util.List;

/**
 * 频繁出现95%的时候进行GC就会报错
 * -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:MaxDirectMemorySize=5m
 */
public class GCOverheadLimitExceeded {
    public static void main(String[] args) {
        int i=0;
        List<String> list = new ArrayList<>();
        try {
            while (true){
                list.add(String.valueOf(i++).intern());
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
