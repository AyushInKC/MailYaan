package com.FourAM.MailYaan.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class SendNowRequest {
    private String sendersEmail;
    private List<String> receiversEmail;
    private String subject;
    private List<String> description;
}
