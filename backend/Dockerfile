# Use a base image with Java and Maven installed
FROM maven:3.8.4-openjdk-21-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to the working directory
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline

# Copy the project source code to the working directory
COPY src ./src

# Build the project
RUN mvn package

# Use a lightweight base image for the runtime
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/backend-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar

# Specify the command to run your application
CMD ["java", "-jar", "app.jar"]