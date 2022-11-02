FROM eclipse-temurin:17.0.3_7-jre-alpine
RUN apk add curl
ARG JAR_FILE=build/libs/eureka-server-1.0.1.jar
COPY ${JAR_FILE} /opt/root.jar
ENTRYPOINT ["java", "-jar", "/opt/root.jar"]