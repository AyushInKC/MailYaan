package com.FourAM.MailYaan.Controller;

import com.FourAM.MailYaan.DTO.ScheduleRequest;
import com.FourAM.MailYaan.DTO.SendNowRequest;
import com.FourAM.MailYaan.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/email")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/schedule")
    public String scheduleEmail(
            @RequestHeader(value = "access-token", required = false) String accessToken,
            @RequestBody ScheduleRequest emailModel) {

        validateAccessToken(accessToken);
        return mailService.scheduleEmail(emailModel);
    }

    @PostMapping("/send-now")
    public String sendEmailNow(
            @RequestHeader(value = "access-token", required = false) String accessToken,
            @RequestBody SendNowRequest emailModel) {

        validateAccessToken(accessToken);
        return mailService.sendNow(emailModel);
    }

    private void validateAccessToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or empty access token");
        }
    }
}
