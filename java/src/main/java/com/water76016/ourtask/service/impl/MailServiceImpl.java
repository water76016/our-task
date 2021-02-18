package com.water76016.ourtask.service.impl;

import com.water76016.ourtask.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @program: our-task
 * @description: 邮件服务接口实现类
 * @author: water76016
 * @create: 2021-02-17 22:54
 **/
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String mailFrom;

    @Override
    public void sendSimpleMail(String mailTo) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(mailTo);
        message.setSubject("simple mail");
        message.setText("hello world");
        mailSender.send(message);
    }
}
