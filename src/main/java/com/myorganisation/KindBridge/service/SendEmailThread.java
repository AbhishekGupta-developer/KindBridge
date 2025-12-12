package com.myorganisation.KindBridge.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SendEmailThread extends Thread {

    private JavaMailSender mailSender;
    private String email;
    private String subject;
    private String htmlBody;

    public SendEmailThread(JavaMailSender mailSender, String email, String subject, String htmlBody) {
        this.mailSender = mailSender;
        this.email = email;
        this.subject = subject;
        this.htmlBody = htmlBody;
    }

    @Autowired
    public void run() {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true enables HTML

            mailSender.send(mimeMessage);
        } catch(MessagingException e) {
            System.out.println("An exception occurred during sending an email: " + e.getMessage());
        }
    }
}
