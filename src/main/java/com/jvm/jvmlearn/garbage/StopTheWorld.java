package com.jvm.jvmlearn.garbage;

import java.util.ArrayList;
import java.util.List;

public class StopTheWorld {

    public static  class WorkThread extends Thread{
        List<byte[]> list = new ArrayList<byte[]>();

        @Override
        public void run() {
            try {
                while (true){
                    for (int i=0;i<1000;i++){
                        byte[] buffer = new byte[1024];
                        list.add(buffer);
                    }
                    if(list.size()>10000){
                        list.clear();
                        System.gc();//导致STW
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    public static class PrintThread extends Thread{
        public final long startTime = System.currentTimeMillis();

        public void run(){
            try {
                while (true){
                    long t = System.currentTimeMillis() - startTime;
                    System.out.println(t/1000 + "." + t%1000);
                    Thread.sleep(1000);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        WorkThread w  = new WorkThread();
        PrintThread p = new PrintThread();
        w.start();
        p.start();
    }
}
