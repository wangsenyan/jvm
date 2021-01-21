package com.jvm.annotation.check.zero;

import java.util.List;

/**
 * 注解的使用
 */
public class PasswordUtils {
    @UseCase(id=47,description = "Password must contain at least on numeric")
    public boolean validatePassword(String password){
        return (password.matches("\\w*\\d\\w*"));
    }
    @UseCase(id=48)
    public String encryptPassword(String password){
        return new StringBuilder(password).reverse().toString();
    }
    @UseCase(id=49,description = "New password can't equal previously used ones")
    public boolean checkForNewPassword(List<String> prevPasswords,String password){
        return  !prevPasswords.contains(password);
    }
}
