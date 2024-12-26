#!/bin/bash

export AWS_ACCESS_KEY_ID=000000000000 AWS_SECRET_ACCESS_KEY=000000000000

sns_arn_prefix="arn:aws:sns:us-east-1:000000000000"
sqs_arn_prefix="arn:aws:sqs:us-east-1:000000000000"


page_view_topic_name="page-view-topic"
page_view_queue_name="page-view-queue"

awslocal sns create-topic --name $page_view_topic_name --endpoint-url=http://localhost:4566 --region us-east-1
echo "SNS topic '$page_view_topic_name' created successfully"

awslocal sqs create-queue --queue-name $page_view_queue_name --endpoint-url=http://localhost:4566 --region us-east-1
echo "SQS queue '$page_view_queue_name' created successfully"

awslocal sns subscribe --topic-arn "$sns_arn_prefix:$page_view_topic_name" --protocol sqs --notification-endpoint "$sqs_arn_prefix:$page_view_queue_name"
echo "Subscribed SQS queue '$page_view_queue_name' to SNS topic '$page_view_topic_name' successfully"

###############################################

order_fifo_topic_name="order-topic.fifo"
order_fifo_queue_name="order-queue.fifo"

awslocal sns create-topic --name $order_fifo_topic_name --endpoint-url=http://localhost:4566 --region us-east-1 --attributes FifoTopic=true,ContentBasedDeduplication=true
echo "SNS topic '$order_fifo_topic_name' created successfully"

awslocal sqs create-queue --queue-name $order_fifo_queue_name --endpoint-url=http://localhost:4566 --region us-east-1 --attributes FifoQueue=true,ContentBasedDeduplication=true
echo "SQS queue '$order_fifo_queue_name' created successfully"

awslocal sns subscribe --topic-arn "$sns_arn_prefix:$order_fifo_topic_name" --protocol sqs --notification-endpoint "$sqs_arn_prefix:$order_fifo_queue_name" --attributes RawMessageDelivery=true
echo "Subscribed SQS queue '$order_fifo_queue_name' to SNS topic '$order_fifo_topic_name' successfully"

###############################################

user_account_topic_name="user-account-topic"
crm_service_queue_name="crm-service-queue"
notification_service_queue_name="notification-service-queue"

awslocal sns create-topic --name $user_account_topic_name --endpoint-url=http://localhost:4566 --region us-east-1
echo "SNS topic '$user_account_topic_name' created successfully"

awslocal sqs create-queue --queue-name $crm_service_queue_name --endpoint-url=http://localhost:4566 --region us-east-1
echo "SQS queue '$crm_service_queue_name' created successfully"

awslocal sns subscribe --topic-arn "$sns_arn_prefix:$user_account_topic_name" --protocol sqs --notification-endpoint "$sqs_arn_prefix:$crm_service_queue_name" --attributes RawMessageDelivery=true
echo "Subscribed SQS queue '$crm_service_queue_name' to SNS topic '$user_account_topic_name' successfully"

awslocal sns subscribe --topic-arn "$sns_arn_prefix:$user_account_topic_name" --protocol sqs --notification-endpoint "$sqs_arn_prefix:$notification_service_queue_name" --attributes 'RawMessageDelivery=true'
echo "Subscribed SQS queue '$notification_service_queue_name' to SNS topic '$user_account_topic_name' successfully"

echo "resources provisioned successfully"