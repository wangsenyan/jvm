package com.jvm.jvmlearn.classloader;

import java.io.*;

public class EncryptUtil {
    public static void encrypt(File src,File des) throws Exception {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(des);
        int ch;
        while (-1!=(ch=in.read())){
            ch = ch ^ 0xff;//加密,0变1,1变0
            out.write(ch);
        }
        in.close();
        out.close();
    }
}
