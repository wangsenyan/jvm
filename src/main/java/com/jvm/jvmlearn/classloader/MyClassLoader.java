package com.jvm.jvmlearn.classloader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class MyClassLoader extends ClassLoader{
    private String rootDir;
    public MyClassLoader(String rootDir){
        this.rootDir = rootDir;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        //return super.findClass(className);
        Class clazz = this.findLoadedClass(className);
        FileChannel fileChannel = null;
        WritableByteChannel outChannel = null;
        if(null == clazz){
            try{
                String classFile = getClassFile(className);
                FileInputStream fis = new FileInputStream(classFile);
                fileChannel = fis.getChannel();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return super.findClass(className);
    }
    public String getClassFile(String className){
        return null;
    }
}
