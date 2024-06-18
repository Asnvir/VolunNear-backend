## Use the official OpenJDK image as a parent image
#FROM openjdk:17-jdk-slim
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the jar file and other necessary files into the container
#COPY target/VolunNearApp-0.0.1-SNAPSHOT.jar app.jar
#COPY src/main/resources/application.yml application.yml
#COPY src/main/resources/roles.sql roles.sql
#
## Set the command to run your application
#CMD ["java", "-Dspring.profiles.active=dev","-Ddebug", "-jar", "app.jar"]

# Use the official OpenJDK image as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file and other necessary files into the container
COPY target/VolunNearApp-0.0.1-SNAPSHOT.jar app.jar


# Set the command to run your application
CMD ["java",  "-jar", "app.jar"]