package org.projects.amazon.sns.messaging;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SNS;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class NotificationServiceIT {

    @Autowired
    private NotificationProducerService notificationProducerService;

    static SnsClient snsClient;

    static SqsClient sqsClient;

    @Container
    static LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3.4.0"))
            .withServices(SNS,SQS)
            .withCopyFileToContainer(MountableFile.forClasspathResource("resource-provisioning.sh"),
                    "/etc/localstack/init/ready.d/resource-provisioning.sh")
            .waitingFor(Wait.forLogMessage(".*resources provisioned successfully.*", 1));

    @BeforeAll
    static void beforeAll() {
        localStack.start();

        snsClient = SnsClient.builder()
                .endpointOverride(localStack.getEndpointOverride(SNS))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())
                ))
                .region(Region.of(localStack.getRegion()))
                .build();

        sqsClient = SqsClient.builder()
                .endpointOverride(localStack.getEndpointOverride(SQS))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())
                ))
                .region(Region.of(localStack.getRegion()))
                .build();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.sns.endpoint", () -> localStack.getEndpointOverride(SNS));
        registry.add("spring.cloud.aws.sqs.endpoint", () -> localStack.getEndpointOverride(SQS));
        registry.add("spring.cloud.aws.region.static", () -> localStack.getRegion());
        registry.add("spring.cloud.aws.credentials.access-key", () -> localStack.getAccessKey());
        registry.add("spring.cloud.aws.credentials.secret-key", () -> localStack.getSecretKey());

        registry.add("page.view.queue.name", () -> "page-view-queue");
        registry.add("order.fifo.queue.name", () -> "order-queue.fifo");

        registry.add("crm.service.queue.name", () -> "crm-service-queue");
        registry.add("notification.service.queue.name", () -> "notification-service-queue");
    }

    @BeforeEach
    public void resetVariables() {
        SqsMessageConsumer.numOfPageViewMessageReceived = 0;
        SqsMessageConsumer.receivedOrderMessageList.clear();
        SqsMessageConsumer.receivedUserAccountMessageList.clear();
    }

    @Test
    public void givenValidPageViewMessage_whenPublishValidPageViewMessage_thenConsumePublishValidPageViewMessage () {
        int numOfPageViewMessagePublished= 5;
        for (int i = 0; i < numOfPageViewMessagePublished; i++) {
            notificationProducerService.publishPageViewMessage(new PageViewMessage(String.valueOf(i), String.valueOf(i), i + 1));
        }
        Awaitility.await().atMost(2, SECONDS).untilAsserted(() -> {
            Assertions.assertEquals(numOfPageViewMessagePublished, SqsMessageConsumer.numOfPageViewMessageReceived);
        });
    }

    @Test
    public void givenValidOrderMessage_whenPublishValidOrderMessage_thenConsumeRawOrderMessage () {
        OrderMessage orderMessage = new OrderMessage(UUID.randomUUID().toString(), "1", 3);
        notificationProducerService.publishOrderMessage(orderMessage);
        Awaitility.await().atMost(2, SECONDS).untilAsserted(() -> {
            Assertions.assertEquals(1, SqsMessageConsumer.receivedOrderMessageList.size());
        });
    }

    @Test
    public void givenValidOrderMessage_whenPublishDuplicateOrderMessage_thenConsumeDeduplicatedOrderMessage () {
        OrderMessage orderMessage = new OrderMessage(UUID.randomUUID().toString(), "1", 3);
        notificationProducerService.publishOrderMessage(orderMessage);
        notificationProducerService.publishOrderMessage(orderMessage);
        Awaitility.await().atMost(3, SECONDS).untilAsserted(() -> {
            Assertions.assertEquals(1, SqsMessageConsumer.receivedOrderMessageList.size());
        });
    }

    @Test
    public void givenValidUserAccountMessage_whenPublishUserAccountMessage_thenFanoutUserAccountMessage () {
        UserAccountMessage userAccountMessage = new UserAccountMessage(UUID.randomUUID().toString(), "CREATED");
        notificationProducerService.publishUserAccountMessage(userAccountMessage);
        Awaitility.await().atMost(5, SECONDS).untilAsserted(() -> {
            Assertions.assertEquals(2, SqsMessageConsumer.receivedUserAccountMessageList.size());
        });
    }
}
