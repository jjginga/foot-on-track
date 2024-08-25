# Foot on Track - Running Tracker Application

## Note

This project is under continuous development and was originally created as part of a [Project](https://guiadoscursos.uab.pt/ucs/projeto-de-engenharia-informatica/?lang=en) course during the Bachelor's degree in [Computer Engineering](https://guiadoscursos.uab.pt/cursos/licenciatura-em-engenharia-informatica/?lang=en) at Universidade Aberta, where it received a grade of 18 out of 20.

## Project Overview

Foot on Track is a running tracker application designed to assist users in monitoring their running activities in real-time, analyzing performance, and planning future training sessions.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Future Improvements](#future-improvements)
- [Contributors](#contributors)
- [License](#license)

## Features

- **Real-time Running Tracking**: Monitor running activities with real-time updates on distance, time, and pace using GPS data.
- **Performance Analysis**: Analyze past running sessions, including distance, time, elevation, and predict future running performance.
- **Training Plan Management**: Create and manage training plans to achieve specific running goals.
- **User Authentication and Authorization**: Secure access to the application with JWT-based authentication.
- **Cross-Platform Mobile Application**: Available on both Android and iOS platforms using React Native.

## Technologies Used

- **Backend**: 
  - Java 17
  - Spring Boot (Microservices, MVC, Security)
  - Spring Cloud & Spring Cloud Eureka (Service Discovery)
  - PostgreSQL (Relational Database)
  - MongoDB (NoSQL Database)
  - JWT (JSON Web Tokens for Authentication)
  - Smile (Machine Learning for performance prediction)
  
- **Frontend**:
  - React Native (Cross-platform mobile development)
  - Expo (Development environment for React Native)

- **APIs**:
  - RESTful APIs with Spring Boot Starter Web
  - Swagger for API documentation

- **Others**:
  - Docker (Containerization)
  - Maven (Build and Dependency Management)
  - OpenFeign (External API Integration)
  - Lombok (Reduce Boilerplate Code in Java)

## Architecture

The application follows a microservices architecture, divided into several distinct services:

1. **Authentication and Authorization**: Manages user login, registration, and access control.
2. **Running Tracking**: Handles the real-time tracking of running activities.
3. **Training Plan Management**: Manages user-created training plans.
4. **Performance Analysis**: Provides analysis and predictions based on historical running data.
5. **API Gateway**: Serves as the entry point for all API requests, routing them to the appropriate service.
6. **Mobile Application**: A React Native app that interacts with the backend services to provide a seamless user experience.

## Setup and Installation

To set up the project locally, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/jjginga/foot-on-track.git
   cd foot-on-track
2. **Backend Setup**:
  - Ensure you have Java 17 installed.
  - Install Maven.
  - Navigate to each service directory and run:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
  - Ensure PostgreSQL and MongoDB are installed and running. Update the configuration files if necessary.
3. **Frontend Setup**:
  - Install Node.js and npm.
  - Navigate to the mobile app directory.
  - Install dependencies:
    ```bash
    npm install
    ```
  - Start the Expo development server:
    ```bash
    npm start
    ```
4. **Docker Setup (Optional for containerization)**:
  - Ensure Docker is installed.
  - Use the provided Docker files to build and run the services in containers.

## Usage
  - **Running Tracking**: After logging in, start a new running session via the mobile app. The app will track your run, sending updates to the backend every few seconds.
  - **Training Plans**: Create custom training plans in the app. The backend will notify you of scheduled workouts.
  - **Performance Analysis**: View detailed analytics and predictions about your running performance directly in the app.

## API Documentation
All the backend services expose RESTful APIs documented using Swagger. You can access the API documentation by navigating to `http://localhost:{PORT}/swagger-ui.html` for each service.

Key APIs:
  - **Authentication*: `/auth/login`, `/auth/register`
  - **Running Tracking*: `/running-session/start`, `/running-session/{id}/update`
  - **Performance Analysis*: `/analysis/performance/{userId}`, `/analysis/session/{sessionId}`

## Future Improvements
  - **Refactoring**: Reduce repetitive code by centralizing common models.
  - **Password Encryption**: Address the security flaw in password transmission between the app and the backend.
  - **Training Module**: Finish implementing the training model.
  - **Consolidated Swagger Documentation**: Unify Swagger documentation for all services on a single page.
  - **Optimized Endpoints**: Add timestamps and improve endpoint entities to avoid unnecessary data.
  - **Service Compartmentalization**: Refactor services into even more specific modules to isolate responsibilities.
  - **Caching and Elevation Database**: Explore the use of caching or a database to reduce dependency on external APIs.
  - **Temporary Storage**: Implement temporary storage for running session data and performance predictions.
  - **Enhance Prediction Models**: Implement more sophisticated machine learning models for better performance predictions.
  - **Improve UI/UX**: Further refine the mobile app's interface for a better user experience.
  - **Add Social Features**: Allow users to share their running stats and progress with friends.
  - **Expand Sensor Integration**: Integrate more sensors (like heart rate monitors) for a more comprehensive analysis.

## Contributors
[@jjginga](https://github.com/jjginga/)

## License
This project is licensed under the MIT License - see the LICENSE file for details.
