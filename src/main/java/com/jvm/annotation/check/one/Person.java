package com.jvm.annotation.check.one;

import lombok.Getter;

@Getter
public class Person {
    private int age;
    private String name;
    @BuilderProperty
    public void setAge(int age) {
        this.age = age;
    }
    @BuilderProperty
    public void setName(String name) {
        this.name = name;
    }
}

