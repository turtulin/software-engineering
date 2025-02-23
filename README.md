# 🌿 Agri-chain

![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen?logo=spring)
![Maven](https://img.shields.io/badge/Maven-4.0.0-C71A36?logo=apachemaven)
![H2 Database](https://img.shields.io/badge/Database-H2-lightgrey?logo=h2)
![Hibernate](https://img.shields.io/badge/Hibernate-6.6.8.Final-orange?logo=hibernate)
![Lombok](https://img.shields.io/badge/Lombok-1.18.36-pink?logo=lombok)
![SpringDoc OpenAPI](https://img.shields.io/badge/SpringDoc%20OpenAPI-2.8.5-green?logo=swagger)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📜 Project Description  
The aim of this group project, as part of the Unicam Software Engineering exam, is to teach us how to approach the development of every software-related artifact following a Iterative Development Model (the Unified Process). The focus is not only on the development of the codebase, but also and above all on the analysis process. <br>

The project focuses on managing a marketplace dedicated to selling typical products, processed goods, and experiential packages linked to local traditions.<br>
It involves various actors, including producers, transformers, distributors, event planners, moderators, customer service, and admins, each with specific roles.  

### ✨ Key Features:  
- **🛍️ Article Management**: Uploading, editing, and selling articles.  
- **✅ Article Validation**: Moderator review and approve articles before publication.  
- **📦 Order & Return Management**: Handling shipments, returns, and refunds.  
- **👨‍⚖️ User Moderation**: Managing reports, warnings, and temporary bans.  
- **🎉 Event Management**: Organizing fairs, tours, and tastings to promote local products.  

---

## ⚙️ Setup & Installation

### 📌 Prerequisites
Make sure you have the following installed:
- **☕ JDK 21**  
- **🐘 Maven 4.0.0**

### 📥 Clone the Repository
```sh
$ git clone https://github.com/turtulin/software-engineering.git
$ cd software-engineering
```

### 🛠️ Configure the Database
Edit `src/main/resources/application.properties` to set up the database connection:
```properties
# H2 Database configuration (file-based)
spring.datasource.url=jdbc:h2:file:./data/db/agri-chain
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=root
spring.datasource.password=toor

# Hibernate settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Enable H2 Console (accessible at /h2-console)
spring.h2.console.enabled=false
```

### 🔧 Build and Run the Project
Compile and run the application using Maven:
```sh
$ mvn clean install
$ mvn spring-boot:run
```

### 🧪 API Testing
Samples HTTP requests can be found in `source/agri-chain/data/http-requests/samples.http` to test various use cases.

### 🌍 Accessing the Application
Once started, the application will be available at:
```
http://localhost:8080
```

### 📦 Dependencies
The project uses the following dependencies with specific versions:
- **Spring Boot Starter Data JPA** - `3.4.3`
- **Spring Boot Starter Web** - `3.4.3`
- **Spring Boot Starter Validation** - `3.4.3`
- **H2 Database** - `2.3.232`
- **Lombok** - `1.18.36`
- **SpringDoc OpenAPI Starter WebMVC UI** - `2.8.5`
- **Maven Surefire Plugin** - `3.5.2`

---

## 👥 Team Members
- **Iena Alessandro**
- **Musso Marta**
- **Tonti Francesco**

## 🤝 Contributing
If you wish to contribute, please fork the repository and create a pull request with your changes.

## 📜 License
This project is licensed under the MIT License.


