package com.FourAM.MailYaan.Repository;

import com.FourAM.MailYaan.Model.EmailModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<EmailModel, String> {

}
