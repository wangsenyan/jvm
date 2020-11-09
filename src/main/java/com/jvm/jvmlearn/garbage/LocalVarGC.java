package com.jvm.jvmlearn.garbage;

/**
 * https://www.bilibili.com/video/BV1PJ411n7xZ?p=156
 */
public class LocalVarGC {
    public static void main(String[] args) {
        LocalVarGC lv = new LocalVarGC();
        lv.localvarGC3();
    }
    public void localvarGC3(){
        {
            byte[] buffer = new byte[10 * 1024 * 1024];
        }
        //没有value这句,buffer在局部变量表slot1中,有value在slot1中,buffer不再被引用,被回收
        //int value = 21;
        System.gc();
    }
}
