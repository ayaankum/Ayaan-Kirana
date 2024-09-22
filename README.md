# KiranaAPI Documentation

## Overview
This document outlines the available APIs for managing transactions, generating reports, and user authentication within the application. Caching of exchange rates is handled using Redis.
### Content
- User Registration API
- JWT Token Generation API
- Create Transaction API
- Report Generation API
- Tech Stack
- Caching Mechanism
- Rate Limiting
- MongoDB


## Techstack
- **Spring Boot**: Used to build and run the backend services..
- **MongoDB**: A NoSQL database used for storing transaction and user data in a flexible and scalable way.
- **Redis**: Implemented for caching exchange rates, reducing the number of external API calls and improving performance.
- **Resilience4j**: Used for rate limiting to protect the APIs from being overwhelmed by too many requests per minute.
- **JWT**: Used for Authentication and role based Authorization.
-----------
### User Registration API
**Purpose**: This API allows the registration of a new user in the system, enabling them to gain access based on their assigned role (ADMIN or USER).

**Request Body**: The API expects a JSON object containing the user's username, password, email, and role, where the role can be either "ADMIN" or "USER".

 **Response**: Upon successful registration, the API returns a confirmation message indicating that the user has been registered successfully, along with the user details.

```http
POST http://localhost:8080/auth/registration
```

##### Request
```json
{
    "userName":"AyaanAdmin",
    "password":"123456",
    "email":"zzss@gmail.com",
    "role":"ADMIN"
}
 ```
##### Response
```json
{
    "Status": 200,
    "data": {
        "userName": "AyaanAdmin",
        "password": "$2a$10$zkWm73ryxr2uaMkasicxceXA7tV5uvCc7f9kfsonwRxa0NZtMj/kW",
        "email": "zzss@gmail.com",
        "role": [
            {
                "id": "66eedb9e3213b718fa93a603",
                "role": "ROLE_ADMIN"
            }
        ]
    },
    "messasge": "User saved successfully : "
}
```
![Create Transaction](images/caching_image_1.png)
![Create Transaction](images/caching_image_1.png)
------------
### JWT Token Generation API
**Purpose**: This API generates a JSON Web Token (JWT) for authenticated users, allowing them to access protected resources within the application.

**Request Body**: The API expects a JSON object containing the user's username and password to authenticate the user.

**Response**: Upon successful authentication, the API returns the generated JWT token as a string.

```http
GET http://localhost:8080/auth/genToken
```

##### Request
```json
  {
    "userName":"AyaanAdmin",
    "password":"123456"
}
 ```
##### Response
```
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MONLYFORDISPLAYWFuQWRtaW4iLCJpYXQiOjE3MjY5OTM4NTAsImV4cCI6MTcyNjk5NDE1MH0.Dix7-zFyG0djENlWzpKeeoGEUWgfyF1IZzlBW3lBvNY
```
![Create Transaction](images/caching_image_1.png)
![Create Transaction](images/caching_image_1.png)
------
### Record Transaction API
**Purpose**: This API allows users (both ADMIN and USER roles) to create a new financial transaction by submitting details such as type, amount, currency, and description.
**Authentication**: Access to this API requires a valid JWT token, provided in the `Authorization` header.
**Response**: Upon successful transaction creation, the API returns a response with the transaction details, including a unique `transactionId`, status, and the submitted information.

```http
POST http://localhost:8080/api/transactions/create
```
##### Header
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `Authorization` | `string` | **Required**. generated jwt token |
##### Access
Roles: ADMIN, USER

##### Request
```json
  {
        "type": "debit",
        "amount": 12.99,
        "currency": "EUR",
        "description": "Test transaction"
  }
 ```
##### Response
```json
{
  {
"transactionId": "0f5b8dba-d12e-48ad-b75a-4bad4bcb658c",
    "type": "debit",
    "amount": 12.99,
    "currency": "EUR",
    "description": "Test transaction",
    "status": "SUCCESS"
}
}
```
![Create Transaction](images/caching_image_1.png)
![Create Transaction](images/caching_image_1.png)

#### Caching Mechanism 

- When a request for a currency exchange rate is made, the result is stored in Redis to reduce the need for frequent external API calls.
- Subsequent requests for the same currency exchange rate retrieve the value directly from the Redis cache, improving performance.
- The exchange rate data is initially fetched from an external API at https://api.fxratesapi.com/latest?Base=INR if not found in the cache.
  
![Create Transaction](images/caching_image_1.png)
![Create Transaction](images/caching_image_1.png)
-----

### Report Generation API
 **Purpose**: This API retrieves transaction reports based on the specified time period (weekly, monthly, or yearly) for authenticated users.

 **URL Parameter**: The API uses a path variable to define the type of report requested. The valid types are:
  - `/weekly`
  - `/monthly`
  - `/yearly`
   ```http
GET http://localhost:8080/api/report/{periodType}
```

 **Response**: The API returns a list of transaction records corresponding to the requested report type, including:
  - Total debit
  - Total credit
  - Net flow

  Note:`The report considers the volatile nature of currency conversion rates. During transaction recording, currencies are converted to INR and stored in the database, which is later used to calculate the total debit, total credit, and net flow while generating the report.`
<br> Note: `This API is rate limitted using resilience4j.`
 
##### Yearly report
---

```http
GET http://localhost:8080/api/report/yearly
```
##### Header
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `Authorization` | `string` | **Required**. generated jwt token |
##### Access
Roles: ADMIN

##### Response

  
```json
[
    {
        "period": "2024",
        "totalDebit": 55186.70456845377,
        "totalCredit": 108177.80335644518,
        "netFlow": 52991.0987879914,
        "transactions": [
            {
                "transactionId": "f8579e93-715b-43e8-b168-d08303254de5",
                "type": "debit",
                "amount": 20.0,
                "currency": "INR",
                "description": "Test transaction 8",
                "status": "SUCCESS"
            },
            {
                "transactionId": "31697455-d64f-4f56-b913-7b52514845df",
                "type": "credit",
                "amount": 27876.16423738925,
                "currency": "GBP",
                "description": "Test transaction 9",
                "status": "SUCCESS"
            },
           ....
           ....
        ]
    },
    {
        "period": "2023",
        "totalDebit": 4672.8144187362395,
        "totalCredit": 8388.47011485345,
        "netFlow": 3715.65569611721,
        "transactions": [
            {
                "transactionId": "afe1f2a1-25e8-40d2-a935-c14993979ff8",
                "type": "debit",
                "amount": 4672.8144187362395,
                "currency": "EUR",
                "description": "Test transaction 6",
                "status": "SUCCESS"
            },
            ...
            ...
        ]
    }
]
```

![Create Transaction](images/caching_image_1.png)
![Create Transaction](images/caching_image_1.png)


 
##### Monthly report
---

```http
GET http://localhost:8080/api/report/monthly
```
##### Header
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `Authorization` | `string` | **Required**. generated jwt token |
##### Access
Roles: ADMIN

##### Response

```json
[
    {
        "period": "2024-04",
        "totalDebit": 20.0,
        "totalCredit": 27876.16423738925,
        "netFlow": 27856.16423738925,
        "transactions": [
            {
                "transactionId": "f8579e93-715b-43e8-b168-d08303254de5",
                "type": "debit",
                "amount": 20.0,
                "currency": "INR",
                "description": "Test transaction 8",
                "status": "SUCCESS"
            },
        ]
    },
    {
        "period": "2023-05",
        "totalDebit": 4672.8144187362395,
        "totalCredit": 0.0,
        "netFlow": -4672.8144187362395,
        "transactions": [
            {
                "transactionId": "afe1f2a1-25e8-40d2-a935-c14993979ff8",
                "type": "debit",
                "amount": 4672.8144187362395,
                "currency": "EUR",
                "description": "Test transaction 6",
                "status": "SUCCESS"
            }
        ]
    },
    ....
]
```


##### Weekly report
```http
GET http://localhost:8080/api/report/weekly
```
##### Header
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `Authorization` | `string` | **Required**. generated jwt token |
##### Access
Roles: ADMIN


##### Response

  ```json
[
    {
        "period": "2024-W15",
        "totalDebit": 0.0,
        "totalCredit": 27876.16423738925,
        "netFlow": 27876.16423738925,
        "transactions": [
            {
                "transactionId": "31697455-d64f-4f56-b913-7b52514845df",
                "type": "credit",
                "amount": 27876.16423738925,
                "currency": "GBP",
                "description": "Test transaction 9",
                "status": "SUCCESS"
            }
        ]
    },
    {
        "period": "2023-W21",
        "totalDebit": 4672.8144187362395,
        "totalCredit": 0.0,
        "netFlow": -4672.8144187362395,
        "transactions": [
            {
                "transactionId": "afe1f2a1-25e8-40d2-a935-c14993979ff8",
                "type": "debit",
                "amount": 4672.8144187362395,
                "currency": "EUR",
                "description": "Test transaction 6",
                "status": "SUCCESS"
            }
	....
        ]
    },
    {
        "period": "2024-W38",
        "totalDebit": 48196.620003697186,
        "totalCredit": 79300.79317407939,
        "netFlow": 31104.1731703822,
        "transactions": [
            
            {
                "transactionId": "f3a8926a-50de-4eba-99a2-3e5335d7265c",
                "type": "debit",
                "amount": 5173.33546586394,
                "currency": "AUD",
                "description": "Test transaction 16",
                "status": "SUCCESS"
            },
           ....
        ]
    },
```


### API Rate Limiting

- The API limits certain endpoints to a maximum of `10 requests per minute` to prevent abuse and ensure fair usage.
- Rate limiting is implemented using `Resilience4j`, which helps control traffic and prevent server overload.
- Endpoints Rate Limited: The /create and /report (weekly, monthly, yearly) endpoints are rate-limited to protect the system from excessive traffic.

### MongoDB

The MongoDB database consists of three collections—Users, Transactions, and Roles.
##### Users Collection:

- This collection stores the information of registered users, such as their userName, email, and password.
- The user's role (like ADMIN or USER) is referenced in this collection.
- When a user logs in, the system retrieves their details from this collection to authenticate and generate a JWT token.
##### Transactions Collection:

- The Transactions collection stores details of each transaction, including transactionId, amount, currency, type (debit/credit), and description.
- The system also stores the converted amount in INR at the time of the transaction, which is later used for generating accurate reports.
- This collection helps in tracking financial transactions and enables reporting based on stored data.
##### Roles Collection:

- This collection defines and stores the available roles in the system, currently limited to ADMIN and USER.
- In the future, additional roles, can be added here.
- The role is linked to each user, and the JWT generation service retrieves the user’s role from this collection to embed it into the token for authorization.

