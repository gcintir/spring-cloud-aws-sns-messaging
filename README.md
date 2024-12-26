# 🛠️ Practicing AWS SNS Integration with Spring Cloud AWS: Hands-On Examples and Testcontainers-Powered Testing

This repository demonstrates how to seamlessly integrate **Amazon SNS** with **Spring Boot** using **Spring Cloud AWS**. We explore practical messaging scenarios and ensure reliability through **Testcontainers**-powered integration tests and **LocalStack** for local simulation.

---

## 🚀 Features

- **Simplified SNS Integration**: Leverage abstractions like `SnsTemplate`.
- **Real-World Scenarios**: Hands-on examples for sending and receiving messages.
- **Fan-out Pattern**: with multiple SQS queue subscriptions. 
- **Reliable Testing**: Integration tests using **Testcontainers** to emulate real-world behavior.
- **Cost-Effective Development**: Utilize **LocalStack** to simulate Amazon SNS in a local environment.

---

## 📚 Table of Contents

- [Getting Started](#-getting-started)
- [How It Works](#-how-it-works)
- [Examples](#-examples)
- [Testing](#-testing)
- [Contributing](#-contributing)

---

## 🔧 Getting Started

### Prerequisites
- Java 17+
- Docker (for Testcontainers and LocalStack)
- Maven

### Installation
1. Clone the repository:
   ```bash  
   git clone https://github.com/gcintir/spring-cloud-aws-sns-messaging.git
   cd spring-cloud-aws-sns-messaging 

2. Build the project:
   ```bash  
   mvn clean package

---

## 🛠️ How It Works

### Publishing Messages
Use the SnsTemplate to publish messages to an SNS topic with ease.

### Receiving Messages
Subscribe SQS queues to SNS topic to receive notifications

### Local Testing
Simulate SNS behavior locally using LocalStack and validate your implementation with Testcontainers.

---

## 💡 Examples

### Send Message:
```bash  
   snsTemplate.sendNotification("page-view-topic", SnsNotification.of(pageViewMessage));
```

### Receive Message:
```bash  
   @SqsListener(value = "${page.view.queue.name}")
   public void consumePageViewMessage(String pageViewMessage) {
      System.out.println("Received pageViewMessage: " + pageViewMessage);
   }
```

---

## ✅ Testing
This project uses Testcontainers to ensure tests are run in an environment that mimics AWS. To test locally:
```bash  
   mvn test
```

---

## 🤝 Contributing
Contributions are welcome! If you have an idea, feature request, or bug report, please open an issue or submit a pull request.