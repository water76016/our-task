package com.water76016.ourtask.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @program: our-task
 * @description: 过期任务类
 * @author: water76016
 * @create: 2021-02-25 10:10
 **/
@Component
public class OutOfDateTask {
    /**
     * cron表达式：Seconds Minutes Hours DayofMonth Month DayofWeek [Year]
     * 把过期的任务，统一展示到过期列表里面
     */
    @Scheduled(cron = "0 0/10 * ? * ?")
    private void showOutOfDateTask(){

    }
}
