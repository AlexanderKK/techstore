package com.techx7.techstore.service.impl;

import com.techx7.techstore.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final String techstoreEmail;

    @Autowired
    public EmailServiceImpl(TemplateEngine templateEngine,
                            JavaMailSender javaMailSender,
                            @Value("${spring.mail.username}") String techstoreEmail) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.techstoreEmail = techstoreEmail;
    }

    @Override
    public void sendRegistrationEmail(String userEmail, String userName, String activationCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setFrom(techstoreEmail);
            mimeMessageHelper.setReplyTo(techstoreEmail);
            mimeMessageHelper.setSubject("Welcome to Tech x7!");
            mimeMessageHelper.setText(
                    generateRegistrationEmailBody(userName, activationCode),
                    true
            );

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateRegistrationEmailBody(String userName, String activationCode) {
        Context context = new Context();
        context.setVariable("username", userName);
        context.setVariable("activationCode", activationCode);

        return templateEngine.process("/email/registration", context);
    }

}
