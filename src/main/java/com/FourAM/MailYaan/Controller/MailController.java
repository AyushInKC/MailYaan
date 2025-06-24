package com.FourAM.MailYaan.Controller;

import com.FourAM.MailYaan.DTO.ScheduleRequest;
import com.FourAM.MailYaan.DTO.SendNowRequest;
import com.FourAM.MailYaan.Model.EmailModel;
import com.FourAM.MailYaan.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/schedule")
    public String scheduleEmail(@RequestBody ScheduleRequest emailModel) {
        return mailService.scheduleEmail(emailModel);
    }

    @PostMapping("/send-now")
    public String sendEmailNow(@RequestBody SendNowRequest emailModel) {
        return mailService.sendNow(emailModel);
    }
}
