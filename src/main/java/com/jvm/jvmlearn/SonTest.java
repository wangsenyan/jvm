package com.jvm.jvmlearn;

/**
 * 多态和重载
 * 1. 多态 - 继承后重写方法
 * 2. 重载 - 同一个名字，参数不同，方法不同
 * <p>
 * 非虚方法:
 * 1. 方法在编译器就确定了具体的调用版本.在运行中不可变
 * 2. 静态方法,私有方法,final方法,实例构造器,父类方法非虚方法
 * <p>
 * 普通调用指令:
 * 1. invokestatic:调用静态方法,解析阶段确定唯一方法版本
 * 2. invokespecial:调用<init> 方法/私有及父类方法,
 * 解析阶段确定唯一方法版本
 * 3. invokevirtual: 调用所有虚方法
 * final 非虚方法
 * 4. involeinterface: 调用接口方法
 * <p>
 * 动态调用指令: 动态类型语言
 * 5. invokedynamic: 动态解析出需要调用的方法,然后执行
 * 静态类型语言是判断变量自身的类型信息,
 * 动态类型语言是判断变量值的类型信息,变量没有类型信息，变量值才有类型信息
 * Lambda
 * <p>
 * 方法重写的本质
 * 1. 向上递归查找符合的方法，找到但是权限校验不通过 IllegalAccessError
 * 否则 AbstractMethodError
 * 2. 为提高性能,在方法去建立一个虚方法表
 * 在链接阶段的解析(resolve) 创建
 * <p>
 * 方法返回地址:
 * 1. ireturn(返回值是boolean,byte,char,short,int)
 * lreturn、freturn、dreturn、areturn(引用)
 * return(void,实例化初始化方法,类和接口的初始化方法使用)
 * 2. 异常退出 -- 异常处理表
 */
class AnimalTest {

    public AnimalTest() {
        System.out.println("Test构造器");
    }

    //invokestatic
    public static void showStatic(String str) {
        System.out.println("Test" + str);
    }

    //final修饰不能被重写,也是非虚方法
    public final void showFinal() {
        System.out.println("Test show final");
    }

    //invokespecial
    public void showCommon() {
        System.out.println("Test show common");
    }


}

public class SonTest extends AnimalTest {
    public SonTest() {
        //invokespecial
        super();
    }

    public SonTest(int age) {
        //invokespecial
        this();
    }

    //静态方法不能被重写
    //invokestatic
    public static void showStatic(String str) {
        System.out.println("son " + str);
    }

    //invokevirtual
    private void showPrivate(String str) {
        System.out.println("son private " + str);
    }

    public void show() {
        //invokestatic
        showStatic("w.com");
        //invokestatic
        super.showStatic("good");
        //invokevirtual
        showPrivate("hello");
        //invokespecial
        super.showCommon();
        //invokevirtual
        showFinal();
        //invokevirtual
        showCommon();
        //invokevirtual
        info();
    }

    public void info() {
    }

    public static void main(String[] args) {
        SonTest st = new SonTest();
        st.show();
    }
}