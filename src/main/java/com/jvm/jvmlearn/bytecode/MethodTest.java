package com.jvm.jvmlearn.bytecode;

import java.util.Date;

/**
 * 1. invokevirtual
 *   - 调用对象的实例方法,根据对象的实际类型进行分派(虚方法分派),支持多态
 *
 * 2. invokeinterface
 *  - 调用接口方法,运行时搜索由特定对象所实现的这个接口方法
 * 3. invokespecial
 *  - 需要特殊处理的实例方法,包括实例初始化方法(构造器),私有方法和父类方法,
 *    这类方法都是静态类型绑定的,不会在调用时进行动态派发。静态类型绑定
 * 4. invokestatic
 *  - 调用命名类中的类方法(static方法),静态绑定的
 * 5. invokedynamic
 *  - 动态绑定,由用户所设定的方法决定的
 *
 * return
 * - ireturn,lreturn,freturn,dreturn,areturn
 * - 当前返回的是synchronized方法,还会执行一个隐含的monitorexit指令,退出临界区
 * - 丢弃整个帧
 */
public class MethodTest {

    public void  invoke1(){
        Date date = new Date();

        Thread t1 = new Thread();

        super.toString();
        methodPrivate();//为什么是 invokevirtual
    }
    private void methodPrivate(){}
}
