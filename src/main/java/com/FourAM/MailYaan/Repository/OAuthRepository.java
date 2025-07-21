package com.FourAM.MailYaan.Repository;


import com.FourAM.MailYaan.Model.OAuthModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRepository extends MongoRepository<OAuthModel, String> {

    boolean existsByEmail(String email);
    OAuthModel findByEmail(String email);
}
