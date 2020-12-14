package com.jvm.jvmlearn.bytecode;

/**
 * 1. 复制 系数为Slot个数--复制栈顶数据并压入栈顶
 *  - dup
 *  - dup2
 * 2. 插入 dup和x的系数相加,即为需要插入的位置
 *  - dup_x1    1+1=2 栈顶2个slot下面
 *  - dup_x2    1+2=3 栈顶3个slot下面
 *  - dup2_x1   2+1=3 栈顶3个slot下面
 *  - dup2_x2   2+2=4 栈顶4个slot下面
 * 3. 弹出
 *  - pop
 *  - pop2
 * 4. 交换
 *  - swap 交换栈顶两个slot数值位置
 * 5. nop 0x00 调试、占位
 *
 *  return的临时值保存在栈顶,如果调用函数不需要,将栈顶元素pop然后return
 */
public class OperationStackTest {
    public void print(){
        Object obj = new Object();
        obj.toString();
    }

    public void foo(){
        bar();
    }
    public long bar(){
        return 0;
    }

    /**
     *  0 aload_0
     *  1 dup
     *  2 getfield #7 <com/jvm/jvmlearn/bytecode/OperationStackTest.index>
     *  5 dup2_x1
     *  6 lconst_1
     *  7 ladd
     *  8 putfield #7 <com/jvm/jvmlearn/bytecode/OperationStackTest.index>
     * 11 lreturn
     */
    public long nextIndex(){
        return  index++;
    }
    private long index =0;
}
