package com.water76016.ourtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: our-task
 * @description: our-task启动类
 * @author: water76016
 * @create: 2020-09-24 16:45
 **/
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class OurTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(OurTaskApplication.class, args);
    }

}
