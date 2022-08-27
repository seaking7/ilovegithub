package com.poc.ilovegithub.core.service;

import com.poc.ilovegithub.core.domain.MailResult;
import com.poc.ilovegithub.core.domain.MailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class MailSendService {

    private final JavaMailSender mailSender;

    public MailResult emailSend(MailSender sender) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.setSubject(sender.getTitle());
        mimeMessage.setText(sender.getContent(), "UTF-8", "html");
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(sender.getEmail()));

        mailSender.send(mimeMessage);

        return MailResult.builder()
                .email(sender.getEmail())
                .content(sender.getContent())
                .title(sender.getTitle())
                .sendAt(LocalDateTime.now())
                .build();
    }

}
