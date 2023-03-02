FROM openjdk:8-jre-slim
WORKDIR /app

ENV TZ="America/Vancouver"
RUN date

COPY build/build/akka-http-basic-assembly-0.1.jar /app/akka-http-basic-assembly-0.1.jar
COPY build/build/application.conf /app/application.conf 

EXPOSE 8000

ENTRYPOINT ["java","-Dconfig.file=application.conf", "-jar", "akka-http-basic-assembly-0.1.jar" ]
