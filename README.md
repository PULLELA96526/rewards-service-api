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
**GET** `/api/rewards/customer/101`

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
- Java 17+
- Maven

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
src/
├── main/
│   ├── controller/   # API endpoints
│   ├── service/      # Business logic
│   └── repository/   # Database access
└── test/             # Tests
```

- For MySQL, edit the `application.properties` file.
