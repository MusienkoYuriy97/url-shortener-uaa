package by.solbegsoft.urlshorteneruaa.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@PropertySource("classpath:constant.properties")
public class EmailService {
    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    @Value("${JWT_CLAIM_SIMPLE_KEY}")
    private String JWT_CLAIM_SIMPLE_KEY;
    @Value("${JWT_CLAIM_ISSUED_AT}")
    private String JWT_CLAIM_ISSUED_AT;
    @Value("${JWT_CLAIM_EXPIRATION}")
    private String JWT_CLAIM_EXPIRATION;
    @Value("${JWT_SUBJECT_ACTIVATE}")
    private String JWT_SUBJECT_ACTIVATE;
    @Value("${ROOT_PATH}")
    private String ROOT_PATH;

    private final String EMAIL_VERIFICATION_TEMPLATE = "email";

    private final String TEMPLATE_VARIABLE_NAME = "name";
    private final String TEMPLATE_VARIABLE_LINK = "link";

    private final String EMAIL_FROM = "yury.musienko@solbeg.com";
    private final String EMAIL_SUBJECT_ACTIVATION_LINK   = "Activation Link";

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender javaMailSender,
                        TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String fullName, String simpleKey){
        try {
            Map<String, Object> variables = new HashMap<>();
            String jwtKey = toJwt(simpleKey);
            String link = ROOT_PATH + jwtKey;
            variables.put(TEMPLATE_VARIABLE_NAME, fullName);
            variables.put(TEMPLATE_VARIABLE_LINK, link);

            String body = buildTemplate(variables);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            messageHelper.setFrom(EMAIL_FROM);
            messageHelper.setTo(to);
            messageHelper.setSubject(EMAIL_SUBJECT_ACTIVATION_LINK);
            messageHelper.setText(body,true);
            javaMailSender.send(mimeMessage);
            log.debug("Send activated link to {}", to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String toJwt(String simpleKey){
        Claims claims = Jwts.claims().setSubject(JWT_SUBJECT_ACTIVATE);
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiration = issuedAt.plusHours(24);
        claims.put(JWT_CLAIM_SIMPLE_KEY, simpleKey);
        claims.put(JWT_CLAIM_ISSUED_AT, issuedAt.toString());
        claims.put(JWT_CLAIM_EXPIRATION, expiration.toString());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    private String buildTemplate(Map<String, Object> variables){
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(EMAIL_VERIFICATION_TEMPLATE, context);
    }
}