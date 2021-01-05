package com.jvm.jvmlearn.classloader;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 自定义类的加载器
 * 测试为MyClassLoader1Test
 */
public class MyClassLoader1 extends ClassLoader {
    private String byteCodePath;

    public MyClassLoader1(String byteCodePath) {
        this.byteCodePath = byteCodePath;
    }
    public MyClassLoader1(ClassLoader parent, String byteCodePath) {
        super(parent);
        this.byteCodePath = byteCodePath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //ByteArrayOutputStream baos = null;
        //BufferedInputStream bis = null;
        try {
            //获取字节码文件的完整路径

            String fileName = byteCodePath + name + ".class";
//            //获取输入流
//            bis = new BufferedInputStream(new FileInputStream(fileName));
//            //获取一个输出流
//            baos = new ByteArrayOutputStream();
//            //具体读入数据并写出的过程
//            int len;
//            byte[] data = new byte[1024];
//            while ((len = bis.read(data))!=-1){
//                baos.write(data,0,len);
//            }
            //获取内存中完整的字节数组数据
//            byte[] byteCodes = baos.toByteArray();
            byte[] byteCodes = decrypt(fileName);
            //调用defineClass将字节数组的数据转换为Class实例
            Class clazz =  defineClass(null,byteCodes,0,byteCodes.length);
            return clazz;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            try {
//                if(baos!=null)
//                   baos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                if(bis!=null)
//                   bis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return null;
    }

    private byte[] decrypt(String fileName){
        InputStream in = null;
        byte[] data = null;
        ByteArrayOutputStream bos = null;
        try {
           in = new FileInputStream(fileName);
           bos = new ByteArrayOutputStream();
           int ch = 0;
           while (-1!=(ch = in.read())){
               ch=ch^0xff;//解密
               bos.write(ch);
           }
           data = bos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(in!=null)
                    in.close();
                if(bos!=null)
                    bos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }
    public void printd(Class clazz){
        try {
            //Class claxx = Class.forName("Myt");
            //Class clazz = Class.forName("java.lang.String");
            //获取当前运行时类声明的所有方法
            Method[] ms = clazz.getDeclaredMethods();
            for(Method m : ms){
                //获取方法的修饰符
                String mod = Modifier.toString(m.getModifiers());
                System.out.print(mod+" ");
                //获取方法的返回值类型
                String returnType = m.getReturnType().getSimpleName();
                System.out.print(returnType + " ");
                //获取方法名
                System.out.print(m.getName() + "(");
                //获取方法的参数列表
                Class<?>[] ps = m.getParameterTypes();
                if(ps.length == 0) System.out.print(')');
                for(int i =0;i<ps.length;i++){
                    char end = (i== ps.length -1)? ')' :',';
                    //获取参数的类型
                    System.out.print(ps[i].getSimpleName() +end);
                }
                System.out.println();
            }
        }catch (Exception e){
            //在命令行打印异常信息在程序中出错的位置及原因
            e.printStackTrace();
        }
    }
}
