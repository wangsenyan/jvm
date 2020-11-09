package com.jvm.jvmlearn.garbage;

public class SystemGCTest {
    public static void main(String[] args) {
        new SystemGCTest();
        //System.gc();
        //Runtime.getRuntime().gc();
        System.runFinalization();//与课程相反
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("SystemGCTest 重写了finalize()");
    }
}
