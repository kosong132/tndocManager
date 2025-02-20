# Use Maven to build the JAR first
FROM maven:3.8.6-eclipse-temurin-17 as build
WORKDIR /app

# Copy source code and pom.xml
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Use OpenJDK to run the built JAR
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
