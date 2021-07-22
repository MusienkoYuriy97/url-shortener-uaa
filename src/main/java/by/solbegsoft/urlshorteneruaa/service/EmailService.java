package by.solbegsoft.urlshorteneruaa.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class EmailService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${root.path}")
    private String rootPath;
    @Value("${jwt.claimSimpleKey}")
    private String claimSimpleKey;
    @Value("${jwt.claimIssuedAt}")
    private String claimIssuedAt;
    @Value("${jwt.claimExpiration}")
    private String claimExpiration;

    public static final String EMAIL_VERIFICATION_TEMPLATE = "email";

    public static final String TEMPLATE_VARIABLE_NAME = "name";
    public static final String TEMPLATE_VARIABLE_LINK = "link";

    public static final String EMAIL_FROM = "yury.musienko@solbeg.com";
    public static final String EMAIL_SUBJECT_ACTIVATION_LINK   = "Activation Link";

    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;

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
            String link = rootPath + jwtKey;
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
            log.debug("Send activated link to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String toJwt(String simpleKey){
        Claims claims = Jwts.claims().setSubject("activate key");
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiration = issuedAt.plusHours(24);
        claims.put(claimSimpleKey, simpleKey);
        claims.put(claimIssuedAt, issuedAt.toString());
        claims.put(claimExpiration, expiration.toString());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private String buildTemplate(Map<String, Object> variables){
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(EmailService.EMAIL_VERIFICATION_TEMPLATE, context);
    }
}