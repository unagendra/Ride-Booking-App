package com.project.rideBooking.UberApp;

import com.project.rideBooking.UberApp.service.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UberAppApplicationTests {

	@Autowired
	private  EmailSenderService emailSenderService;

	//send Email to One User
	@Test
	void contextLoads() {
		emailSenderService.sendEmail("cepovoc607@ndiety.com",
				"Test Email","This is a Test Email");
	}

	@Test
	void sendMultipleEmail(){
		String[] emails={
				"cepovoc607@ndiety.com","unagendra850@gmail.com","site9861@gmail.com"
		};
		emailSenderService.sendEmail(emails,"Test Email 2","This is a Test Email");
	}

}
