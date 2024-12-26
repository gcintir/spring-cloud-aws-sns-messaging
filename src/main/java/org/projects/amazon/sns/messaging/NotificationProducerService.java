package org.projects.amazon.sns.messaging;

import io.awspring.cloud.sns.core.SnsNotification;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class NotificationProducerService {

    @Autowired
    private SnsTemplate snsTemplate;

    public void publishPageViewMessage (PageViewMessage pageViewMessage) {
        snsTemplate.sendNotification("page-view-topic", SnsNotification.of(pageViewMessage));
    }

    public void publishOrderMessage (OrderMessage orderMessage) {
        SnsNotification<OrderMessage> orderMessageSnsNotification = SnsNotification.builder(orderMessage)
                .groupId(orderMessage.getCustomerId())
                .build();
        snsTemplate.sendNotification("order-topic.fifo", orderMessageSnsNotification);
    }

    public void publishUserAccountMessage (UserAccountMessage userAccountMessage) {
        snsTemplate.sendNotification("user-account-topic", SnsNotification.of(userAccountMessage));
    }
}
