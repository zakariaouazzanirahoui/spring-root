# Use an official Java 17 base image as a builder
FROM eclipse-temurin:17-jdk AS builder
#FROM maven:3.9.4-ibmjava-8 AS builder
# Set the working directory inside the container
WORKDIR /app

RUN apt-get update && apt-get install -y maven

# Copy the Spring Boot application source code into the container
COPY . .

# Build the Spring Boot application using Maven
RUN mvn clean install

# Use a smaller base image for the runtime environment
FROM eclipse-temurin:17-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage to the runtime stage
COPY --from=builder /app/target/Users-MicroService-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port that your Spring Boot application listens on (change to the actual port)
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]

LABEL authors="ramo"
