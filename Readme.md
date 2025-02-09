# Video Streaming API (eeze backend coding sssignment)

## **Project Overview**
This project implements a **Video Streaming API** designed to fulfill the requirements of a major Hollywood production house. It allows content managers to **publish videos**, manage **metadata**, and track **user engagement metrics** (views and impressions) for monetization purposes.

> **Note:** This implementation is designed as a **monolithic architecture** for simplicity and rapid development. In a real-world production environment, this solution should be refactored into **microservices** to enhance scalability, maintainability, and fault tolerance.


---

## **Functional Requirements**

### **Video Management:**
- **Publish Video:** Add new videos with metadata.
- **Edit Metadata:** Update video information (title, director, cast, genre, etc.).
- **Soft Delete Video:** Mark videos as deleted without removing them from the database.

### **Content Handling:**
- **Load Video:** Retrieve metadata and content for a video.
- **Play Video:** Return mock video content while tracking views.
- **List Videos:** Display a list of available videos with basic metadata.
- **Search Videos:** Search videos based on criteria (e.g., director).

### **Engagement Tracking:**
- **Impressions:** Recorded when metadata is loaded.
- **Views:** Recorded when a video is played.


---

## **Project Structure**

```
└── src/main/java/org/sb/eezebeassignment/
    ├── controller/      # REST controllers
    ├── service/         # Business logic
    ├── repository/      # Data access layer
    ├── model/           # JPA entities (Video, VideoEngagement)
    ├── dto/             # Data Transfer Objects (VideoRequest, VideoResponse)
    └── exception/       # Global exception handling
```


---

## **Technology Stack**
- **Java 17** (LTS)
- **Spring Boot 2.7**
- **Maven** for dependency management
- **H2 Database** (local) & **PostgreSQL** (production)
- **JUnit & Mockito** for unit tests
- **Swagger/OpenAPI** for API documentation


---

## **API Endpoints**

### **Video Management**

- **POST `/videos`** - *Publish Video*
  - **Request:** Title, Director, Genre, Release Year, Synopsis, Cast
  - **Response:** Video metadata with ID

- **PUT `/videos/{id}`** - *Edit Video Metadata*
- **DELETE `/videos/{id}`** - *Soft Delete Video*
- **GET `/videos/{id}`** - *Load Video Metadata* (tracks impressions)
- **GET `/videos`** - *List All Available Videos*
- **GET `/videos/search`** - *Search Videos by Criteria*

### **Engagement Tracking**

- **GET `/videos/{id}/play`** - *Play Video* (tracks views, returns mock content)
- **GET `/videos/{id}/engagement`** - *Retrieve Engagement Metrics* (views & impressions)


---

## **Engagement Logic**

- **Impressions:** Incremented when metadata is accessed (`GET /videos/{id}`).
- **Views:** Incremented when video is played (`GET /videos/{id}/play`).


---

## **Database Schema**

### **Video Table:**
- `id` (PK, auto-generated)
- `title`, `director`, `synopsis`, `genre`, `release_year`, `running_time`, `cast`
- `deleted` (boolean for soft deletes)

### **VideoEngagement Table:**
- `id` (PK)
- `video_id` (FK)
- `views`, `impressions`


---

## **Error Handling**

Implemented using `@RestControllerAdvice`:
- **400 Bad Request:** Validation errors
- **404 Not Found:** Resource not found
- **500 Internal Server Error:** Unhandled exceptions


---

## **Testing Strategy**

### **Unit Tests:**
- **Controller & Service Layer** covered using **JUnit + Mockito**
- Edge cases for validation, not-found, and error scenarios

### **Integration Tests:**
- End-to-end tests using **MockMvc** to simulate HTTP requests
- Tests for all CRUD operations and engagement tracking


---

## **Setup & Deployment**

### **Build the Project:**
```bash
mvn clean install
```

### **Run Locally:**
```bash
mvn spring-boot:run
```

### **Run with Docker:**
```bash
docker-compose up --build
```


---

## **API Documentation (Swagger)**
Access Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```

View OpenAPI spec:
```
http://localhost:8080/v3/api-docs
```


---

## **Key Design Considerations**

- **Scalability:** While this solution uses a monolithic architecture for simplicity, it is designed with future scalability in mind.
- **Error Handling:** Global exception handling for consistent API responses.
- **Database Flexibility:** Supports H2 for local and PostgreSQL for production.


---

## **Future Improvements & Microservices Architecture**

For a real-world production environment, the following architectural improvements are recommended:

### **1. Microservices Architecture**
- **Service Decomposition:** Separate services for **Video Management**, **Content Delivery**, and **Engagement Tracking**.
- **Communication:** Use **REST** or **gRPC** for inter-service communication.
- **Database Per Service:** Each microservice manages its own database.

### **2. Scalability Enhancements**
- **Load Balancing:** Deploy with **Kubernetes** or Docker Swarm.
- **Caching:** Implement **Redis** for frequently accessed metadata.
- **CDN Integration:** For real video streaming, integrate with CDNs for performance.

### **3. Reliability & Monitoring**
- **Circuit Breaker Patterns** with tools like **Resilience4j**.
- **Centralized Logging** using **ELK Stack**.
- **Monitoring:** Implement monitoring with **Prometheus** and **Grafana**.

### **4. Security Improvements**
- **Authentication & Authorization:** Implement **JWT** or **OAuth2**.
- **API Gateway:** Use **Kong** or **NGINX** as a gateway for rate limiting and security.


---

## **Known Limitations & Future Improvements**
- **Monolithic Structure:** Designed for simplicity; should be refactored into microservices for production.
- **Concurrency Handling:** Optimistic locking added, but future enhancements may include distributed locking.
- **Authentication:** Currently missing; can be integrated with JWT or OAuth for securing APIs.
- **Video Content:** Mock content used; integration with real streaming services could be added later.


---

## **Conclusion**

This project provides a solid foundation for managing video content and tracking user engagement metrics effectively. While the current implementation is monolithic for ease of development, it is designed to be scalable and adaptable for microservices-based architecture in production environments.
