# Base image được sử dụng để build image
FROM openjdk:21

# Set working directory trong container
WORKDIR /app

# Copy file JAR được build từ ứng dụng Spring Boot vào working directory trong container
COPY target/shopbase-0.0.1-SNAPSHOT.jar app.jar

# Expose port của ứng dụng
EXPOSE 9090

# Chỉ định command để chạy ứng dụng khi container khởi chạy
CMD ["java", "-Dspring.profiles.active=k8s", "-jar", "app.jar"]