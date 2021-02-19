package com.water76016.ourtask.service;

import com.water76016.ourtask.entity.Feedback;

/**
 * @program: our-task
 * @description: 邮件服务接口
 * @author: water76016
 * @create: 2021-02-17 22:52
 **/
public interface MailService {
    void sendSimpleMail(Feedback feedback);
}
