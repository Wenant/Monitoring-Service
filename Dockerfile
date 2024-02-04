FROM openjdk:17

RUN mkdir /app

COPY . /app

WORKDIR /app

CMD ["java", "-jar", "out/artifacts/Monitoring_Service_jar2/Monitoring-Service.jar"]
