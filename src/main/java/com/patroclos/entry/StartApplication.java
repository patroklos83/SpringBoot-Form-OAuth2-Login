package com.patroclos.entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.patroclos")
public class StartApplication{


	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}


}
