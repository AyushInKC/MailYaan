package com.FourAM.MailYaan.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="users")
public class EmailModel {
    private String sendersEmail;
    private List<String> receiversEmail;
    private String subject;
    private List<String> description;
}
