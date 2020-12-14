package com.jvm.jvmlearn;

/** (Z)V Z布尔类型
 * 一。条件跳转指令，都需要两个字节的操作数,用于计算跳转的位置offset
 * 1. ifeq ==0
 * 2. iflt <0
 * 3. ifle <=0
 * 4. ifne !=0
 * 5. ifgt >0
 * 6. ifge >=0
 * 7. ifnull ==null
 * 8. ifnonnull !=null
 * 二。 比较条件跳转指令 满足就跳转
 * 1. if_icmpeq
 * 2. if_icmpne
 * 3. if_icmplt
 * 4. if_icmpgt
 * 5. if_icmple
 * 6. if_icmpge
 * 7. if_acmpeq 对象引用
 * 8. if_acmpne 对象引用
 * 三。多条件分支
 * 1. switch-case
 *  - tableswitch case值连续
 *  - lookupswitch case值不连续
 * 四，无条件跳转指令
 *  - goto  两个字节的操作数
 *  - goto_w 四个字节的操作数
 */
public class GotoTest {
    public void compare1(){
        int a=0;
        if(a!=0)
            a=10;
        else
            a=20;
    }
    public boolean compareNull(String str){
        if(str == null)
            return true;
        else
            return false;
    }
    public void ifCompare2(){
        int i=10;
        int j=20;
        System.out.println(i>j);
    }
    public void ifCompare3(){
        Object obj1 = new Object();
        Object obj2 = new Object();
        System.out.println(obj1 == obj2);
        System.out.println(obj1 != obj2);
    }

    public void switch1(int select){
        int num;
        switch (select){
            case 1:
                num = 10;
                break;
            case 2:
                num = 20;
                break;
            case 3:
                num = 30;
                break;
            default:
                num = 40;
        }
    }
    public void switch2(int select){
        int num;
        switch (select){
            case 100:
                num = 10;
                break;
            case 500:
                num = 20;
                break;
            case 200:
                num = 30;
                break;
            default:
                num = 40;
        }
    }
    //hashCode
    public void switch3(String season){
       switch (season){
           case "SPRING":break;
           case "SUMMER":break;
           case "AUTUMN":break;
           case "WINTER":break;
       }
    }

    public void whileInt()
    {
        int i=0;
        while (i<100){
            String s = "hello,world";
            i++;
        }
    }
}
