package com.jvm.jvmlearn.garbage;

import java.lang.ref.SoftReference;

public class SoftReferenceTest {
    public static class User{
        private String name;
        private Integer age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static void main(String[] args) {
       // User u = new SoftReference<User>(new User("wang",1));
    }
}
