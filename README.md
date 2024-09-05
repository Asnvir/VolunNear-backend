
# VolunNear-backend

[Link to VolunNear Frontend Repository](https://github.com/Asnvir/VolunNear-frontend)


VolunNear is an application designed to bridge the gap between volunteers and organizations in need. With its user-friendly interface and powerful features, the platform makes the user experience smooth, fast, and enjoyable, saving valuable time for everyone involved. For volunteers, it’s incredibly easy and convenient to find opportunities that match their preferences, while organizations can effortlessly find and manage volunteers, as well as coordinate volunteer activities.

## Project Video

[![Watch the project video](https://img.youtube.com/vi/sWHwgk6Db3A/0.jpg)](https://www.youtube.com/watch?v=sWHwgk6Db3A)

## Technologies Used

- **Java**: A versatile and secure programming language used for building the backend of VolunNear, ideal for enterprise-level applications.
- **Spring Boot**: A framework that simplifies the development of Java-based applications by providing pre-configured tools, enabling rapid creation of production-ready applications.
- **Spring Security**: Provides authentication and authorization, ensuring secure access to the application's features and protecting sensitive data.
- **Spring Data JPA**: Simplifies database interactions by providing a repository-based approach, allowing developers to focus on business logic.
- **Hibernate**: An ORM tool that manages data persistence, converting Java objects into database tables and enabling seamless database interactions.
- **JavaMailSender**: A Spring component for sending emails from the application, used for notifications and communication with users.
- **JWT (JSON Web Token)**: Secures API endpoints by ensuring only authenticated users can access specific services.
- **MySQL**: A reliable relational database that manages the application’s data, supporting complex queries and efficient data handling.
- **Docker**: A containerization platform used to ensure consistency across environments, making deployment and scaling more efficient.
- **Lombok**: A Java library that reduces boilerplate code by automatically generating common methods, simplifying development and maintenance.

## Features

- **Volunteer Registration**: Volunteers can easily sign up and manage their profiles.
- **Organization Registration**: Organizations can register and manage their presence on the platform.
- **Activity Management**: Organizations can create, edit, and manage volunteer activities.
- **Searching Activities by Different Filters**: Users can search for activities using various filters such as location, type, date, and more.
- **Feedback Collection**: Volunteers can provide feedback on activities, helping organizations improve their events.
- **Notification Subscriptions**: Users can subscribe to notifications for updates on activities they are interested in.


## Getting Started

### Prerequisites

- **Node.js** and **npm**

### Installation

### Prerequisites

- **Java** (version 11 or higher)
- **Maven** (for building the project)
- **MySQL** (for the database)
- **Docker** (optional, for containerization)

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/Asnvir/VolunNear-backend.git

2. **Navigate to the project directory**:
   ```bash
   cd VolunNear-backend
   ```

 3. *Create a .env file**:
    In the root directory of the project, create a .env file with the following content:

    ```bash
    DOCKER_NETWORK_MODE=host

    # SPRING - Application properties
    SPRING_PROFILES_ACTIVE=dev
    EXTERNAL_APP_PORT=8081
    APP_JWT_TOKEN_SECRET="your_jwt_secret"
    APP_JWT_TOKEN_LIFETIME=your_token_lifetime

    # Credentials of Google account that will be used to send emails
    APP_MAIL_HOST=your_mail_host
    APP_MAIL_PORT=your_mail_port
    APP_EMAIL_USERNAME=your_email_username
    APP_EMAIL_PASSWORD=your_email_password

    # DB - MySQL
    APP_DB_HOSTNAME=your_db_hostname
    APP_DB_HOST=your_db_host
    INTERNAL_APP_DB_PORT=your_internal_db_port
    EXTERNAL_APP_DB_PORT=your_external_db_port
    APP_DB_NAME=your_db_name
    APP_DB_OPTIONS=your_db_options
    APP_DB_USERNAME=your_db_username
    APP_DB_PASSWORD=your_db_password
    APP_DB_URL=jdbc:mysql://${APP_DB_HOSTNAME}:${EXTERNAL_APP_DB_PORT}/${APP_DB_NAME}?${APP_DB_OPTIONS}
    APP_DB_DRIVER_CLASS_NAME=your_db_driver_class_name

    # Frontend
    FRONTEND_HOST=your_frontend_host
    FRONTEND_PORT=your_frontend_port

    # Google Maps API
    GOOGLE_MAPS_API_KEY=your_google_maps_api_key

    # MinIO  
    MINIO_EXTERNAL_PORT=your_minio_external_port
    MINIO_INTERNAL_PORT=your_minio_internal_port
    MINIO_EXTERNAL_PORT_HTTPS=your_minio_external_port_https
    MINIO_INTERNAL_PORT_HTTPS=your_minio_internal_port_https
    MINIO_API_URL=http://your_minio_api_url
    MINIO_UPLOAD_URL=http://your_minio_upload_url
    MINIO_ACCESS_KEY=your_minio_access_key
    MINIO_SECRET_KEY=your_minio_secret_key


4. **Build the project**:

   Run the following command to build the project using Maven:

   ```bash
   mvn clean install

5. **Start the application**:

   To start the Spring Boot application, run the following command:

   ```bash
   java -jar target/volunnear-backend.jar

6. **Access the application**:

   Once the application is running, it will be accessible at:

   ```bash
   http://localhost:your_backend_port

    
