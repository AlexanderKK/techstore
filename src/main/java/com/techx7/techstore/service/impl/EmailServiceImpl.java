package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.EmailService;
import com.techx7.techstore.service.PasswordResetService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;

@Service
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final String techstoreEmail;
    private final UserRepository userRepository;
    private final PasswordResetService passwordResetService;

    @Autowired
    public EmailServiceImpl(TemplateEngine templateEngine,
                            JavaMailSender javaMailSender,
                            @Value("${spring.mail.username}") String techstoreEmail,
                            UserRepository userRepository,
                            PasswordResetService passwordResetService) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.techstoreEmail = techstoreEmail;
        this.userRepository = userRepository;
        this.passwordResetService = passwordResetService;
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

    @Override
    public void sendPasswordRecoveryEmail(String emailOrUsername) {
        User user = userRepository.findByEmailOrUsername(emailOrUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        String userEmail = user.getEmail();
        String userName = user.getUsername();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        String passwordResetCode = passwordResetService.createPasswordResetCode(userEmail);

        try {
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setFrom(techstoreEmail);
            mimeMessageHelper.setReplyTo(techstoreEmail);
            mimeMessageHelper.setSubject("Hello, " + userName);
            mimeMessageHelper.setText(
                    "A request has been received to change the password for your Techx7 account.\n" +
                    "https://techx7.bluesky-e0a9d778.eastus.azurecontainerapps.io/users/password/reset/" + passwordResetCode + "?email=" + userEmail);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateRegistrationEmailBody(String userName, String activationCode) {
        return getProcessedTemplate(userName, activationCode);
    }

    private String getProcessedTemplate(String userName, String activationCode) {
        Context context = new Context();
        context.setVariable("username", userName);
        context.setVariable("activationCode", activationCode);

        String processedTemplate = templateEngine.process("email/registration", context);

        return processedTemplate;
    }

    private String getProcessedTemplateDeprecated(String userName, String activationCode, String templateString) {
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setCacheable(false);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        Context context = new Context();
        context.setVariable("username", userName);
        context.setVariable("activationCode", activationCode);

        String processedTemplate = templateEngine.process(templateString, context);

        return processedTemplate;
    }

}
