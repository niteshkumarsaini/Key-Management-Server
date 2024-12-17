Key Management Server (KMS) for Secure Data Encryption
This project implements a Key Management System (KMS) using Spring Boot, Spring Security, and the AWS SDK to securely generate, store, and manage encryption keys. The system integrates AWS Key Management Service (KMS) and AWS Secrets Manager for cryptographic key operations and secure storage.

The frontend, built with ReactJS, provides an intuitive interface for key management. Authentication and authorization are secured using JSON Web Tokens (JWT), ensuring a robust and secure implementation.

Overview
The system focuses on securely generating and managing Data Encryption Keys (DEKs) with the following steps:

Admin Approval: Requires two admin keys for DEK generation.
AWS KMS Integration: Utilizes AWS KMS for advanced cryptographic operations.
Sensitive Data Protection: DEKs are used to encrypt and decrypt sensitive data.
This solution is ideal for applications where data security is a priority, such as:

Financial systems
Healthcare platforms
Enterprise solutions requiring encryption and access control
Features
Secure DEK Generation: Generates Data Encryption Keys using AWS KMS with two admin approvals.
Encryption & Decryption: Encrypts and decrypts sensitive data using generated DEKs.
Key Storage Management: Secure storage of keys with AWS Secrets Manager.
JWT Security: Implements token-based authentication and authorization with Spring Security.
ReactJS Frontend: Provides an interactive and modern interface for key management.
Tech Stack
Backend
Spring Boot
Spring Security
AWS SDK (KMS, Secrets Manager)
JSON Web Tokens (JWT)
Frontend
ReactJS
Cloud Services
AWS Key Management Service (KMS)
AWS Secrets Manager
Project Structure
plaintext
Copy code
Key-Management-Server/
│
├── backend/           # Spring Boot backend
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── frontend/          # ReactJS frontend
│   ├── src/
│   ├── package.json
│   └── ...
│
└── README.md          # Project documentation
Setup Instructions
Prerequisites
Ensure you have the following tools installed:

Java 17+
Node.js & npm
Maven
AWS Credentials configured in your environment
Backend Setup
Navigate to the backend directory:

bash
Copy code
cd backend
Update application.properties with the required configurations:

AWS Access Key ID
AWS Secret Access Key
AWS Region
KMS Key ARN
Build and run the backend server:

bash
Copy code
mvn spring-boot:run
The backend will run on: http://localhost:8080

Frontend Setup
Navigate to the frontend directory:

bash
Copy code
cd frontend
Install dependencies:

bash
Copy code
npm install
Start the frontend development server:

bash
Copy code
npm start
The frontend will be accessible at: http://localhost:3000

Usage
Admin Login: Authenticate using JWT-based security.
DEK Generation: Requires two admins to approve and trigger the DEK generation.
Encrypt & Decrypt: Use the DEK to encrypt or decrypt sensitive data securely.
Key Management: AWS Secrets Manager ensures secure storage of all keys.
Security
Admin Approval: Dual admin keys are required for DEK generation, ensuring controlled access.
JWT Authentication: Secure authentication and authorization with token-based mechanisms.
AWS Services: Uses AWS KMS for cryptographic operations and Secrets Manager for secure key storage.
