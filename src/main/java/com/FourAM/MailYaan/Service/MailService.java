package com.FourAM.MailYaan.Service;

import com.FourAM.MailYaan.DTO.ScheduleRequest;
import com.FourAM.MailYaan.DTO.SendNowRequest;
import com.FourAM.MailYaan.Model.EmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    private final List<EmailModel> scheduledEmails = new ArrayList<>();

    public String scheduleEmail(ScheduleRequest request) {
        if (request.getReceiversEmail().size() != request.getDescription().size()) {
            return "Error: Number of recipients and descriptions must match!";
        }

        EmailModel emailModel = new EmailModel(
                request.getSendersEmail(),
                request.getReceiversEmail(),
                request.getSubject(),
                request.getDescription(),
                request.getScheduledTime()
        );

        scheduledEmails.add(emailModel);
        return "Email(s) scheduled successfully.";
    }

    public String sendNow(SendNowRequest request) {
        if (request.getReceiversEmail().size() != request.getDescription().size()) {
            return "Error: Number of recipients and descriptions must match!";
        }

        for (int i = 0; i < request.getReceiversEmail().size(); i++) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(request.getSendersEmail());
                message.setTo(request.getReceiversEmail().get(i));
                message.setSubject(request.getSubject());
                message.setText(request.getDescription().get(i));

                mailSender.send(message);
                System.out.println("Email sent to: " + request.getReceiversEmail().get(i));
            } catch (Exception e) {
                System.out.println("Failed to send to " + request.getReceiversEmail().get(i) + ": " + e.getMessage());
            }
        }

        return "Email(s) sent immediately.";
    }

    @Scheduled(fixedRate = 30000)
    public void checkAndSendScheduledEmails() {
        LocalDateTime now = LocalDateTime.now();
        Iterator<EmailModel> iterator = scheduledEmails.iterator();

        while (iterator.hasNext()) {
            EmailModel email = iterator.next();
            if (!now.isBefore(email.getScheduledTime())) {
                SendNowRequest request = new SendNowRequest();
                request.setSendersEmail(email.getSendersEmail());
                request.setReceiversEmail(email.getReceiversEmail());
                request.setSubject(email.getSubject());
                request.setDescription(email.getDescription());

                sendNow(request);
                iterator.remove();
            }
        }
    }
}
