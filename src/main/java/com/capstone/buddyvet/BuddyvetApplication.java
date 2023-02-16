package com.capstone.buddyvet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BuddyvetApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuddyvetApplication.class, args);
	}

}
