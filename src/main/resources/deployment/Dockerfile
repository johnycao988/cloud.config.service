FROM 10.39.101.226:5000/openjdk:8-jdk-alpine
VOLUME /tmp
ADD cloud.config.service-1.0.jar app.jar
ENV SERVICE_NAME cloud-config-service
ENV SERVICE_TAGS test
EXPOSE 3080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]