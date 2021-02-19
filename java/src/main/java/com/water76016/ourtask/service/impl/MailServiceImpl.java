package com.water76016.ourtask.service.impl;

import cn.hutool.extra.template.TemplateEngine;
import com.water76016.ourtask.entity.Feedback;
import com.water76016.ourtask.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

/**
 * @program: our-task
 * @description: 邮件服务接口实现类
 * @author: water76016
 * @create: 2021-02-17 22:54
 **/
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;
    @Value("${spring.mail.username}")
    private String to;

    @Override
    public void sendSimpleMail(Feedback feedback) {
        SimpleMailMessage message=new SimpleMailMessage();
        //邮件发送者帐号
        message.setFrom(from);
        //邮件接收者帐号
        message.setTo(to);
        message.setSubject(feedback.getTitle());
        message.setText(feedback.getDescription());
        //发送邮件
        mailSender.send(message);
    }
}
