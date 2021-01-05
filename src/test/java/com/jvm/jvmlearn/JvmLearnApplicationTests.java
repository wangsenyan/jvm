package com.jvm.jvmlearn;

import com.jvm.jvmlearn.classloader.EncryptUtil;
import com.jvm.jvmlearn.classloader.MyClassLoader1;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

class JvmLearnApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 自定义类加载器实现加密解密功能
     * 1. 定义加密类,实现对字节码文件的加密
     * 2. 定义加载类,实现对字节码文件的解密
     * 3. 通过反射进行使用
     */
    @Test
    void ClassLoaderTest() throws Exception {
        String src = "D:/MyTest.class";
        String des = "D:/MyTestEncrypt.class";
        EncryptUtil.encrypt(new File(src),new File(des));
        MyClassLoader1 myClassLoader1 = new MyClassLoader1("d:/");
        Class clazz =  myClassLoader1.loadClass("MyTestEncrypt");
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method method: declaredMethods){

            //获取修饰符
            int modifiers = method.getModifiers();
            System.out.println(Modifier.toString(modifiers));

            Class<?> returnType = method.getReturnType();
            System.out.println("返回类型" + returnType.getSimpleName());
            //获取方法名
            String name = method.getName();
            System.out.println(name);
            //获取参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            for ( Class c :parameterTypes){
                System.out.println(c.getSimpleName());
            }

        }
        //获取域字段
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field: declaredFields){
            System.out.println(field.getName());
        }

        int modifiers = clazz.getModifiers();
        myClassLoader1.printd(clazz);
    }
}
