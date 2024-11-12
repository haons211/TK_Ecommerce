# Ecommerce  TK  System

## Overall view

![Ecommerce Architect.svg](Ecommerce%20Architect.svg)]

## List what has been used

- [Spring Boot](https://spring.io/projects/spring-boot), web framework, makes it easy to create stand-alone,
  production-grade Spring based Applications
- [AWS Cloud](https://aws.amazon.com/), using AWS SDK for Java 2.x
    - S3, etc
- [Apache Kafka](https://kafka.apache.org/), a distributed and fault-tolerant stream processing system used for
  event-driven communication between microservices.
- [Spring Data Redis](https://docs.spring.io/spring-data/redis/reference/redis/redis-cache.html), provides an
  implementation of Spring Framework’s Cache Abstraction.
- [Docker](https://www.docker.com/) and docker-compose, for containerization
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
  provides repository support for the Jakarta Persistence API
- [JWT]([JSON Web Tokens - jwt.io](https://jwt.io/)) for providing authentication, user management, fine-grained authorization
- [MySQL]([MySQL](https://www.mysql.com/)
- [ElasticSearch]([Elasticsearch: The Official Distributed Search & Analytics Engine | Elastic](https://www.elastic.co/elasticsearch)) , A distributed search and analytics engine used for quickly querying and analyzing large volumes of data.
- [Kibina]([Kibana: Explore, Visualize, Discover Data | Elastic](https://www.elastic.co/kibana)) , A data visualization tool for ElasticSearch, providing real-time analytics and visual insights.
- [Zookeeper]([Apache ZooKeeper](https://zookeeper.apache.org/)) , A centralized service for maintaining configuration information, naming, and providing distributed synchronization.
- [GCP]([Cloud Computing Services | Google Cloud](https://cloud.google.com/)) , For additional cloud services and infrastructure as needed for scalability, storage, and analytics.

## Prerequisite

- Java 17
- MySql 8
- Maven
- Docker
- GNU Make
- WSL (if using Windows)

## Setup
### 1. Clone the Repository
Start by cloning the repository to your local machine:
```bash
git clone https://github.com/haons211/TK_Ecommerce.git
cd  TK_Ecommerce/
```
## 2. Docker Compose-Up
```bash
docker compose up -d
```
##  3. Run
```bash

./mvnw spring-boot:run

```
## Information


#### VNPay

[Official Document](https://sandbox.vnpayment.vn/apis/docs/thanh-toan-pay/pay.html)

![Pasted image 20241112173454.png](Pasted%20image%2020241112173454.png)![[Pasted image 20241112173454.png]]
##### API Endpoints
| Endpoint                                            | HTTP Method | Description                                                   |
| --------------------------------------------------- | ----------- | ------------------------------------------------------------- |
| `/api/v1/payments   /create_payment_url`            | POST        | Creates a new payment URL for initiating a payment            |
| `/api/v1/payments       /vnpay_ipn`                 | GET         | Handles the VNPAY Instant Payment Notification (IPN) callback |
| `/api/v1/payments            /vnpay-payment-return` | GET         | Processes the VNPAY payment return URL after transaction      |
#### Sample Data  For Testing
```
9704198526191432198
NGUYEN VAN A
07/15
123456
```

####  GHN
[Official Document](https://api.ghn.vn/)
##### API  Endpoints
| Endpoint                      | HTTP Method | Description                                                            |
| ----------------------------- | ----------- | ---------------------------------------------------------------------- |
| `/v1/shop/register`           | POST        | Registers a new shop with GHN                                          |
| `/v1/shipping-order/create`   | POST        | Creates a new shipping order with pickup and delivery details          |
| `/v1/shipping-order/cancel`   | POST        | Cancels an existing shipping order                                     |
| `/v1/shipping-order/detail`   | GET         | Retrieves details of a specific shipping order                         |
| `/v1/shipping-order/leadtime` | GET         | Estimates lead time (delivery time) based on location and service type |
| `/v1/shipping-order/fee`      | GET         | Calculates shipping fee based on weight, distance, and service type    |
| `/v1/shipping-order/status`   | GET         | Tracks the current status of a shipping order                          |
| `/v1/ward`                    | GET         | Retrieves a list of wards in a specific district                       |
| `/v1/district`                | GET         | Retrieves a list of districts in a specific province                   |
| `/v1/province`                | GET         | Retrieves a list of provinces available in the GHN service             |
| `/v1/store`                   | GET         | Retrieves details of the store or origin location information          |
| `/v1/address`                 | POST        | Validates or normalizes an address before creating an order            |
| `/v1/shipping-order/return`   | POST        | Creates a return order for an already delivered or undelivered order   |
