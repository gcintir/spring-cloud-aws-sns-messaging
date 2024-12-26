package org.projects.amazon.sns.messaging;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private NotificationProducerService notificationProducerService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*
		notificationProducerService.sendNotification("hello world");
		notificationProducerService.sendNotification("Current Time Notification", LocalDateTime.now().toString(), UUID.randomUUID().toString());
		notificationProducerService.sendNotification("User Created", new UserInfo("John", "Doe"));

		 */
	}
}
/**
 * https://docs.awspring.io/spring-cloud-aws/docs/3.0.0-M2/reference/html/index.html#spring-cloud-aws-sns
 * https://reflectoring.io/publisher-subscriber-pattern-using-aws-sns-and-sqs-in-spring-boot/
 * https://www.baeldung.com/spring-cloud-aws-messaging
 * https://medium.com/codex/spring-boot-sns-sqs-localstack-8e58a4add4b1
 */
