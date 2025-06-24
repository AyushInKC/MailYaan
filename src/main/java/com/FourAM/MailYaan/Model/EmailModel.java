package com.FourAM.MailYaan.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailModel {
    private String sendersEmail;
    private List<String> receiversEmail;
    private String subject;
    private List<String> description;
    private LocalDateTime scheduledTime;
}
