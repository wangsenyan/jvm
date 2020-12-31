package com.jvm.jvmlearn.error;

import java.nio.ByteBuffer;

/**
 * 直接内存用完了 NIO
 * 配置参数
 *   -
 *   - -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:MaxDirectMemorySize=5m
 * 故障现象
 *   - Exception in thread "main" java.lang.OutOfMemoryError: Cannot reserve 15728640 bytes of direct buffer memory (allocated: 8192, limit: 5242880)
 * 导致原因
 *   - ByteBuffer.allocate(capacity) 分配JVM堆内存,属于GC管辖范围
 *   - ByteBuffer.allocateDirect(capacity) 分配本地内存,不需要GC管理,速度较快
 *
 */
public class DirectBufferError {
    public static void main(String[] args) {
        //System.out.println("maxDirectMemory:");
        //ByteBuffer.allocate(5*1024*1024);
        ByteBuffer.allocateDirect(6*1024*1024);
    }
}
