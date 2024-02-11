FROM openjdk:17

RUN mkdir /app

COPY . /app

WORKDIR /app

CMD ["java", "-jar", "target/Monitoring-Service.jar"]