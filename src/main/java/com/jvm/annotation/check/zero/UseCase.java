package com.jvm.annotation.check.zero;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义注解
 * @Target 注解的域
 * @Retention 注解的生命周期
 * 注解元素
 * - 所有基本类型
 * - String
 * - Class
 * - enum
 * - Annotation
 *   AbstractProcessor
 * 找到注解 getAnnotation(Override.class)
 * @SupportedAnnotationTypes("java.lang.Override")
 * @SupportedAnnotationTypes("java.lang.Deprecated") //找到这个
 *   - 找到LoadProc.class 文件
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
    int id();
    String description() default "no description";
}
