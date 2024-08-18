FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/VolunNearApp-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application-dev.yml application-dev.yml
COPY src/main/resources/roles.sql roles.sql

CMD ["java", "-Xrunjdwp:transport=dt_socket,address=*:5005,server=y,suspend=n", "-Dcom.sun.management.jmxremote.port=9999", "-Dcom.sun.management.jmxremote.rmi.port=9998", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-jar", "app.jar"]
