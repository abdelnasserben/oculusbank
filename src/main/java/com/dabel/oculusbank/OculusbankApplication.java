package com.dabel.oculusbank;

import com.dabel.oculusbank.app.Generator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OculusbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(OculusbankApplication.class, args);
		String number = Generator.generateAccountNumber();
	}

}
