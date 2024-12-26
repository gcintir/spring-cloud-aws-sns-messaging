package org.projects.amazon.sns.messaging;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class SqsMessageConsumer {
    public static int numOfPageViewMessageReceived = 0;

    public static List<OrderMessage> receivedOrderMessageList = new ArrayList<>();
    public static List<UserAccountMessage> receivedUserAccountMessageList = new ArrayList<>();

    @SqsListener(value = "${page.view.queue.name}")
    public void consumePageViewMessage(String pageViewMessage) {
        if (Objects.nonNull(pageViewMessage)) {
            numOfPageViewMessageReceived++;
        }
    }

    @SqsListener(value = "${order.fifo.queue.name}")
    public void consumeOrderMessage(OrderMessage orderMessage) {
        receivedOrderMessageList.add(orderMessage);
    }

    @SqsListener(value = "${crm.service.queue.name}")
    public void consumeCrmServiceQueueMessage(UserAccountMessage userAccountMessage) {
        System.out.println("11111");
        receivedUserAccountMessageList.add(userAccountMessage);
    }

    @SqsListener(value = "${notification.service.queue.name}")
    public void consumeNotificationServiceQueueMessage(UserAccountMessage userAccountMessage) {
        System.out.println("222222222222");
        receivedUserAccountMessageList.add(userAccountMessage);
    }
}
