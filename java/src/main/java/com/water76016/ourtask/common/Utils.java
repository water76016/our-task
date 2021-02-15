package com.water76016.ourtask.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @program: our-task
 * @description: 针对当前项目，编写的Utils工具类
 * @author: water76016
 * @create: 2020-09-28 17:24
 **/
public class Utils {
    public static BCryptPasswordEncoder bcp = new BCryptPasswordEncoder();
    /**
     * 根据传入的字符串，把传入的字符串进行加密并返回结果
     */
    public static String encode(String str){
        String result = bcp.encode(str);
        return result;
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword){
        return bcp.matches(rawPassword, encodedPassword);
    }
}
