package by.solbegsoft.urlshorteneruaa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String message){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            mimeMessage.setFrom("yury.musienko@solbeg.com");
            mimeMessage.setRecipients(Message.RecipientType.TO, to);
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message,"utf-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        log.info("Send activated link to " + to);
        javaMailSender.send(mimeMessage);
    }
}
