package br.com.bitz.wallet;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.config.TopicBuilder;

@EnableFeignClients
@EnableAspectJAutoProxy
@SpringBootApplication
public class BitzWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitzWalletApplication.class, args);
	}

    @Bean
    NewTopic notificationTopic() {
        return TopicBuilder.name("transaction-notification")
            .build();
    }

}
