package com.jvm.jvmlearn.garbage;

/**
 * finalize()
 */
public class CanRelivevObj {

    private static CanRelivevObj obj;

    @Override
    protected void finalize() throws Throwable{
        super.finalize();
        System.out.println("调用当前类重写的finalize()方法");
        obj = this;
    }
    public static void main(String[] args) {
        try {
            obj = new CanRelivevObj();
            obj = null;
            System.gc();
            System.out.println("第一次 gc");
            //因为Finalizer线程优先级很低,暂停2秒,以等待它
            Thread.sleep(2000);
            if(obj == null)
            {
                System.out.println("obj is dead");
            }else{
                System.out.println("obj is still alive");
            }
            System.out.println("第二次gc");
            obj = null;
            System.gc();
            //因为Finalizer线程优先级很低,暂停2秒,以等待它
            Thread.sleep(2000);
            if(obj == null)
            {
                System.out.println("obj is dead");
            }else{
                System.out.println("obj is still alive");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
