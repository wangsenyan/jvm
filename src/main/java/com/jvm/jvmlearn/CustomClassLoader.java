package com.jvm.jvmlearn;

import java.io.FileNotFoundException;
/**
 * 继承ClassLoader
 * 重写findClass
 * 或者直接继承URLClassLoader,避免编写findClass()方法及获取字节码流的方式
 * abstract ClassLoader -> SecureClassLoader -> URLClassLoader -> ExtClassLoader/AppClassLoader
 * <p>
 * 获取加载类
 * 1. clazz.getClassLoader() 获取当前类的加载类
 * 2. Thread.currentThread().getContextClassLoader() 获取当前线程上下文的ClassLoader
 * 3. ClassLoader.getSystemClassLoader() 获取系统的ClassLoader
 * 4. DriverManager.getCallerClassLoader() 获取调用者的ClassLoader
 * <p>
 * 双亲委派机制
 * 递归委托给父类,直到顶层的启动类加载器,然后由上到下直到找到可以完成加载的加载器
 * 先加载再执行 clinit->init->main
 * <p>
 * 例子：接口由启动类加载器加载,实现由系统加载器加载
 * 沙箱安全机制: 引导类包中不能自定义类???
 * 不能在java.lang中定义新类
 * <p>
 * 两个类相同的条件
 * 1. 类名和包名一样
 * 2. 加载类相同
 * <p>
 * 如果类型由用户类加载器加载,类型信息中有加载器的引用
 * <p>
 * Java程序对类的使用分为:主动使用和被动使用
 * 被动使用不会导致类的初始化
 * 主动使用,七种情况
 * 1. 创建类的实例
 * 2. 访问某个类或接口的静态变量,或者对该静态变量赋值
 * 3. 调用类的静态方法
 * 4. 反射(比如 Class.forName("com.jvm.Test"))
 * 5. 初始化一个类的子类
 * 6. Java虚拟机启动时被标明为启动类的类
 * 7. JDK7 开始提供动态语言支持
 * java.lang.invoke.MethodHandle实例的解析结果
 * REF_getStatic,REF_putStatic,REF_invokeStatic
 * 句柄对应的类没有初始化,则初始化 clinit
 */

/**
 * 获取加载类
 * 1. clazz.getClassLoader() 获取当前类的加载类
 * 2. Thread.currentThread().getContextClassLoader() 获取当前线程上下文的ClassLoader
 * 3. ClassLoader.getSystemClassLoader() 获取系统的ClassLoader
 * 4. DriverManager.getCallerClassLoader() 获取调用者的ClassLoader
 */

/**
 * 双亲委派机制
 * 递归委托给父类,直到顶层的启动类加载器,然后由上到下直到找到可以完成加载的加载器
 * 先加载再执行 clinit->init->main
 *
 * 例子：接口由启动类加载器加载,实现由系统加载器加载
 * 沙箱安全机制: 引导类包中不能自定义类???
 *      不能在java.lang中定义新类
 */

/**
 * 两个类相同的条件
 * 1. 类名和包名一样
 * 2. 加载类相同
 *
 * 如果类型由用户类加载器加载,类型信息中有加载器的引用
 */

/**
 * Java程序对类的使用分为:主动使用和被动使用
 * 被动使用不会导致类的初始化
 * 主动使用,七种情况
 * 1. 创建类的实例
 * 2. 访问某个类或接口的静态变量,或者对该静态变量赋值
 * 3. 调用类的静态方法
 * 4. 反射(比如 Class.forName("com.jvm.Test"))
 * 5. 初始化一个类的子类
 * 6. Java虚拟机启动时被标明为启动类的类
 * 7. JDK7 开始提供动态语言支持
 *    java.lang.invoke.MethodHandle实例的解析结果
 *    REF_getStatic,REF_putStatic,REF_invokeStatic
 *    句柄对应的类没有初始化,则初始化 clinit
 */

/**
 * 运行时数据区
 * 1. 本地方法栈   --- 线程作用域
 * 2. 程序计数器   --- 线程作用域(每个线程一个)
 *    指向下一条执行的指令
 *    Native 则为undefined
 * 3. jvm栈       --- 线程作用域
 * 4. 堆 --- 进程作用域
 * 5. 方法区(Metaspace元数据区(常量池,方法元数据,类元信息)，JIT编译产物) --- 进程作用域
 *
 * 一个jvm实例对应一个runtime实例 getRuntime()
 * HotSpot JVM,每个线程都与操作系统的本地线程直接映射，run()
 * 最后一个普通线程结束，jvm可以停止
 */
public class CustomClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] result = getClassFromCustomPath(name);
            if (result == null) {
                throw new FileNotFoundException();
            } else {
                return defineClass(name, result, 0, result.length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        throw new ClassNotFoundException();
        //return super.findClass(name);
    }

    //如果指定的路径的字节码文件进行了加密,则需要在此方法中进行解密操作
    private byte[] getClassFromCustomPath(String name) {
        return null;
    }
}
