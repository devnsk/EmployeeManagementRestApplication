package com.fyl.demo.service;

import com.fyl.demo.dto.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;


    public void sendEmail(EmailDetail emailDetail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(emailDetail.getRecipient());
        mimeMessageHelper.setSubject(emailDetail.getSubject());
        //

        mimeMessageHelper.setText(emailDetail.getContent(),true);
//        mimeMessageHelper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
        log.info("Email Sent Successfully to " + emailDetail.getRecipient());
        log.info("Email sender " + from);
    }
}
