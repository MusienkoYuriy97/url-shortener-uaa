package by.solbegsoft.urlshorteneruaa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailService {
    public static final String EMAIL_VERIFICATION_TEMPLATE = "email";
    public static final String TEMPLATE_VARIABLE_EMAIL = "email";
    public static final String TEMPLATE_VARIABLE_NAME = "name";
    public static final String TEMPLATE_VARIABLE_TOKEN = "token";
    public static final String TEMPLATE_VARIABLE_HOST = "host";

    public static final String EMAIL_FROM = "yury.musienko@solbeg.com";
    public static final String EMAIL_SUBJECT_ACTIVATION_LINK   = "Activation Link";
    public static final String HOST = "http://localhost:8080/api/v1/user/";

    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;


    @Autowired
    public EmailService(JavaMailSender javaMailSender,
                        TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String fullName, String activationKey){

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put(TEMPLATE_VARIABLE_NAME, fullName);
            variables.put(TEMPLATE_VARIABLE_TOKEN, activationKey);
            variables.put(TEMPLATE_VARIABLE_HOST, HOST);

            String body = buildTemplate(EMAIL_VERIFICATION_TEMPLATE, variables);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            messageHelper.setFrom(EMAIL_FROM);
            messageHelper.setTo(to);
            messageHelper.setSubject(EMAIL_SUBJECT_ACTIVATION_LINK);
            messageHelper.setText(body,true);
            javaMailSender.send(mimeMessage);
            log.info("Send activated link to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private String buildTemplate(String template, Map<String, Object> variables){
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }
}