package com.poc.ilovegithub.core.service;

import com.poc.ilovegithub.core.domain.MailResult;
import com.poc.ilovegithub.core.domain.MailSender;
import com.poc.ilovegithub.core.repository.MailTemplateRepository;
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
    private final MailTemplateRepository mailTemplateRepository;

    public MailResult emailSend(MailSender sender) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            mimeMessage.setSubject(sender.getTitle());
            mimeMessage.setText(sender.getContent(), "UTF-8", "html");
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(sender.getEmail()));

            if(isEmailFormat(sender.getEmail())) mailSender.send(mimeMessage);
        } catch( Exception e)
        {
            mailTemplateRepository.insertFailUser(sender.getEmail());
        }


        return MailResult.builder()
                .email(sender.getEmail())
                .content(sender.getContent())
                .title(sender.getTitle())
                .mailType(sender.getMailType())
                .sendAt(LocalDateTime.now())
                .build();
    }


    private boolean isEmailFormat(String toMail) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (toMail != null && toMail.matches(EMAIL_PATTERN)) {
            return true;
        }
        return false;
    }

}
