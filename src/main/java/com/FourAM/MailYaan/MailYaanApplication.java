package com.FourAM.MailYaan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailYaanApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailYaanApplication.class, args);
	}

}
