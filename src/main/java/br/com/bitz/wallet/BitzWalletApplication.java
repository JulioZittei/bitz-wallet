package br.com.bitz.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BitzWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitzWalletApplication.class, args);
	}

}
