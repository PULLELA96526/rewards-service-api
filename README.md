# Rewards Program API

## Overview
A simple Spring Boot API that calculates customer reward points based on purchase transactions.

## How It Works
- **2 points per dollar** over $100.
- **1 point per dollar** between $50 and $100.

### Example:
A purchase of **$120** earns **90 points**:
- 2 × ($120 - $100) = 40 points
- 1 × ($100 - $50) = 50 points

**Total: 90 points**

## API Endpoints

### 1. Record a Transaction
**POST** `/api/rewards/transactions`

Request Body:
```json
{
  "customerId": 101,
  "customerName": "Rajkumar",
  "amount": 120.50,
  "transactionDate": "2023-11-15"
}
```

### 2. Get Rewards Summary
**GET** `/api/rewards/customer/{customerId}`

Response:
```json
{
  "customerId": 101,
  "monthlyPoints": {
    "NOVEMBER": 90
  },
  "totalPoints": 90
}
```

## Setup

### Requirements
- Java 17
- Spring Boot 3.X
- RestFul Webservice
- Maven 3.8.3


### Run
```bash
mvn spring-boot:run
```

### Test
```bash
mvn test
```

## Project Structure
```
rewardsapp/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── charter/
│ │ │ └── rewardsapp/
│ │ │ ├── controller/
│ │ │ ├── dto/
│ │ │ ├── entity/
│ │ │ ├── exception/
│ │ │ ├── repository/
│ │ │ ├── service/
│ │ │ └── RewardsappApplication.java
│ │ └── resources/
│ │ ├── static/
│ │ ├── templates/
│ │ └── application.properties
│ └── test/
│ └── java/
│ └── com/
│ └── charter/
│ └── rewardsapp/
│ ├── controller/
│ ├── integration/
│ ├── service/
│ ├── util/
│ └── RewardsappApplicationTests.java
├── .gitattributes
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── .rewardsapp.iml
```



- For MySQL,  `application.properties` file.
## Database Configuration :

### MySQL Setup
```properties
# application.properties
spring.application.name=rewardsapp
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/sony_db
spring.datasource.username=root
spring.datasource.password=root

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server
server.port=8080

## Database Schema :

### `transactions` Table
| Column Name         | Type         | Constraints       | Description                     |
|---------------------|--------------|-------------------|---------------------------------|
| id                  | BIGINT       | PRIMARY KEY, AUTO_INCREMENT | Unique transaction ID     |
| customer_id         | BIGINT       | NOT NULL          | Associated customer ID          |
| customer_name       | VARCHAR(255) | NOT NULL          | Customer name                   |
| amount              | DOUBLE       | NOT NULL          | Transaction amount              |
| transaction_date    | DATE         | NOT NULL          | Date of transaction             |
| points              | INTEGER      | NOT NULL          | Calculated reward points        |

## Technical Stack :computer:

### Backend
- **Framework**: Spring Boot 3.x
- **Database**: MySQL
- **ORM**: JPA
- **Build Tool**: Maven
