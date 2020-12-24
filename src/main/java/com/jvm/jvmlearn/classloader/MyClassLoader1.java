package com.jvm.jvmlearn.classloader;

import java.io.*;

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
        ByteArrayOutputStream baos = null;
        BufferedInputStream bis = null;
        try {
            //获取字节码文件的完整路径
            String fileName = byteCodePath + name + ".class";
            //获取输入流
            bis = new BufferedInputStream(new FileInputStream(fileName));
            //获取一个输出流
            baos = new ByteArrayOutputStream();
            //具体读入数据并写出的过程
            int len;
            byte[] data = new byte[1024];
            while ((len = bis.read(data))!=-1){
                baos.write(data,0,len);
            }
            //获取内存中完整的字节数组数据
            byte[] byteCodes = baos.toByteArray();
            //调用defineClass将字节数组的数据转换为Class实例
            Class clazz =  defineClass(null,byteCodes,0,byteCodes.length);
            return clazz;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(baos!=null)
                   baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(bis!=null)
                   bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
