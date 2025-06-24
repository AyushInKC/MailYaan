package com.FourAM.MailYaan.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class ScheduleRequest {
    private String sendersEmail;
    private List<String> receiversEmail;
    private String subject;
    private List<String> description;
    private LocalDateTime scheduledTime;
}
