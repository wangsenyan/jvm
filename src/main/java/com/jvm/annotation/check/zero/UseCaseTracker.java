package com.jvm.annotation.check.zero;

import java.lang.reflect.Method;
import java.util.List;

public class UseCaseTracker {
    public static void trackUseCases(List<Integer> useCases,Class<?>cl){
        for(Method m: cl.getDeclaredMethods()){
            UseCase uc = m.getAnnotation(UseCase.class);
            if(uc!=null){
                System.out.println("Found use Case: " + uc.id() + " " + uc.description() );
                useCases.remove(Integer.valueOf(uc.id()));
            }
        }
        for(int i: useCases){
            System.out.println("Warning: Missing use case-" + i);
        }
    }
}
