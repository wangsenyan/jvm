package com.jvm.jvmlearn.jmm;
import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 同步计数器,做减法
 */
public class CountDownTest {
    public static void main(String[] args) {
        CountDownTest ct = new CountDownTest();
        ct.test();
    }
    public void test(){
        String[] country = {"齐","楚","燕","韩","赵","魏"};
        CountDownLatch cdl = new CountDownLatch(6);
        for(int i=0;i<6;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "\t" + "被灭");
                cdl.countDown();
            },country[i]).start();
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("秦灭六国");
    }
}

enum Country{
    ONE(1,"俄罗斯"),TWO(2,"加拿大"),THERE(3,"中国");
    private Integer index;
    private String value;
    Country(Integer index, String value) {
        this.index = index;
        this.value = value;
    }
    public Integer getIndex() {
        return index;
    }
    public String getValue() {
        return value;
    }
    public static Country forEach_Country(int index){
        Country[] countries = Country.values();
        for(Country country:countries){
            if(index == country.getIndex())
                return country;
        }
        return null;
    }
}