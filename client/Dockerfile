FROM adoptopenjdk/openjdk11:latest
ADD target/admin-starter.war app.war
ENTRYPOINT ["java", "-jar", "app.war"]