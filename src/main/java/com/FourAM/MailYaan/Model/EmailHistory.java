package com.FourAM.MailYaan.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Document(collection = "email_history") // store in email_history collection
public class EmailHistory {
    @Id
    private String id;

    private String sendersEmail;
    private List<String> receiversEmail;
    private String subject;
    private List<String> description;
    private Instant sentAt;
}
