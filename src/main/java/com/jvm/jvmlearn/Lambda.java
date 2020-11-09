package com.jvm.jvmlearn;

interface Func {
    public boolean func(String str);
}

public class Lambda {
    public void lambda(Func func) {
        return;
    }

    public static void main(String[] args) {
        Lambda lambda = new Lambda();

        //invokedynamic
        Func func = s -> {
            return true;
        };

        lambda.lambda(func);
        //invokedynamic
        lambda.lambda(s -> {
            return true;
        });
    }
}
