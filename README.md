# Techstore
Spring MVC application that focuses on e-commerce operations with computers and electronics.<br>
Link: https://techx7.bluesky-e0a9d778.eastus.azurecontainerapps.io

# Description
Techstore is an online store for computers and electronics.<br>
It is developed for customers who are mainly looking for computers, accessories and different kinds of electronics.<br>
The web project is responsive having decent UI and UX.

# Code
The web application follows the best practices for Object-Oriented design and high quality code:

* abstraction, encapsulation, inheritance, polymorphism
* exception handling
* loose coupling and high cohesion
* thin controllers

# Built With

* Spring MVC
* Spring Security
* Spring Data JPA
* Spring Validation
* Spring Mail
* Spring Actuator
* Hibernate ORM
* MySQL Server
* ModelMapper
* Cloudinary
* JUnit
* Thymeleaf
* jQuery
* Bootstrap
* Font Awesome Icons

# Functionality

* User login and registration
* User profile
* Account activation by email
* Password recovery by email
* Administrator management of users
* Administrator management of roles
* Adding product to cart
* Updating quantity of product in cart
* Removing product from cart
* Checkout with items in cart
* Order functionality
* Product CRUD operations
* Category CRUD operations
* Model CRUD operations
* Manufacturer CRUD operations

# Project Architecture
I am using MVC architecture consisting of several layers:

* View
* Controller
* Service
* Repository
* Database

# Quick Start & Implementation

* Administrator - seeded during application initialization
```
Email: admin@techx7.com
Username: admin
Password: admin12345
```

* Roles - seeded during start of the application
```
Admin: Access and modify users, reviews, make changes to the database if needed
Manager: Access, modify products, but cannot access and modify user profiles
Support: Access user reviews, respond to questions and chat with users
User: Access and buy products, write reviews, edit their profile, but cannot add or modify a product
Carrier: Access orders, user's address info
```

# Database Diagram
![image](https://github.com/AlexanderKK/techstore/assets/80641156/3a97c2a4-1816-41fa-93ae-b994d2b60606)

# Test

## Testing dependencies:
* JUnit
* Mockito

---

* Test coverage

![image](https://github.com/AlexanderKK/techstore/assets/80641156/e51cc312-aa62-4985-a2ca-478837a08b64)

# Application pages

* Home

![img_7](https://github.com/AlexanderKK/techstore/assets/80641156/a3f32e72-9600-4fbd-af62-c9b53430b350)

* Products

![img_8](https://github.com/AlexanderKK/techstore/assets/80641156/e6c8ea65-d38a-48d6-a5b2-2dc55b456250)

* Product Details

![img_9](https://github.com/AlexanderKK/techstore/assets/80641156/d187e51d-01c9-405c-a803-ca3828bf0bf2)

* Contact

![img_10](https://github.com/AlexanderKK/techstore/assets/80641156/e4696152-2229-451c-821e-97fe05eb7756)

* Register Popup

![image](https://github.com/AlexanderKK/techstore/assets/80641156/fd0ab3b0-8895-4720-98d6-8ebb92fe592f)

* Activation e-mail

![image](https://github.com/AlexanderKK/techstore/assets/80641156/e4dc31db-36b6-47e7-b2da-75493d66df57)

* Login Page

![image](https://github.com/AlexanderKK/techstore/assets/80641156/22fa9744-83b1-4e9a-81fe-9e52e3079bf9)

* Password Recovery Page

![image](https://github.com/AlexanderKK/techstore/assets/80641156/914918fe-53f0-42b4-ae04-1b5843bc9993)

* Password Reset Page

![image](https://github.com/AlexanderKK/techstore/assets/80641156/7b35dc0c-f181-4d45-84e1-2fc878522d46)

* Profile

![image](https://github.com/AlexanderKK/techstore/assets/80641156/cc852f6a-8715-44b7-afa2-a53c3885367f)

* Cart

![img_11](https://github.com/AlexanderKK/techstore/assets/80641156/521f1c09-da86-4d78-9422-9b99544b603d)

![img_18](https://github.com/AlexanderKK/techstore/assets/80641156/dec0ca8a-b139-4ef9-b0ed-49e6de177552)

* Checkout
  
![image](https://github.com/AlexanderKK/techstore/assets/80641156/c7d3b04a-d249-422a-bf42-10a8652bfcf5)

* Add Product

![img_12](https://github.com/AlexanderKK/techstore/assets/80641156/b4960dc2-2e4d-48ac-8221-5d744b13e5ce)

* Manufacturers

![img_15](https://github.com/AlexanderKK/techstore/assets/80641156/d7235577-12f7-40ec-ad5b-840bf33a525e)

* Models

![img_16](https://github.com/AlexanderKK/techstore/assets/80641156/8a04e620-22d2-4313-86b7-ae0346515798)

* Categories

![img_17](https://github.com/AlexanderKK/techstore/assets/80641156/5347278a-0d56-4ae7-9f75-4184e78384a0)

* Admin Management - Users

![img_20](https://github.com/AlexanderKK/techstore/assets/80641156/01577faa-a8e8-4939-b16c-3b98c504833e)

* Admin Management - Roles

![img_21](https://github.com/AlexanderKK/techstore/assets/80641156/3c1cb31b-ea79-418a-8f0a-bf9a31de52b8)
