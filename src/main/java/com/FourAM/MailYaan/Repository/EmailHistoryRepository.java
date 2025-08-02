package com.FourAM.MailYaan.Repository;

import com.FourAM.MailYaan.Model.EmailHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmailHistoryRepository extends MongoRepository<EmailHistory, String> {
    List<EmailHistory> findBySendersEmailOrderBySentAtDesc(String sendersEmail);
}

