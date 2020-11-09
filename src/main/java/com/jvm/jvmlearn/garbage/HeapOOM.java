package com.jvm.jvmlearn.garbage;

import java.util.ArrayList;

/**
 * -Xms8m -Xmx8m -XX:+HeapDumpOnOutOfMemoryError
 * 生成的文件在项目根目录下
 */
public class HeapOOM {
    byte[] buffer = new byte[1 * 1024* 1024];
    public static void main(String[] args) {
        ArrayList<HeapOOM> list = new ArrayList<>();
        int count = 0;
        try {
            while (true){
                list.add(new HeapOOM());
                count++;
            }
        }catch (Throwable e){
            System.out.println("count = "+ count);
            e.printStackTrace();
        }
    }
}
