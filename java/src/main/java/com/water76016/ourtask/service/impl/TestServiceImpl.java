package com.water76016.ourtask.service.impl;

import com.water76016.ourtask.service.TestService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: our-task
 * @description:
 * @author: water76016
 * @create: 2021-04-06 22:41
 **/
public class TestServiceImpl implements TestService {
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();



    @Override
    public void study() {
        //创建泛型对象并且初始化值
        threadLocal.set("one");
        String s = threadLocal.get();

    }
}
