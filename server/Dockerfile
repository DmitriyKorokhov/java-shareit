FROM amazoncorretto:11
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# for local postman tests
# ENV TZ="Europe/Moscow"
