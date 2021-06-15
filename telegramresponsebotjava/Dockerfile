FROM maven:3.5.4-jdk-8-alpine as maven

COPY ./pom.xml ./pom.xml

COPY ./src ./src

COPY ./config ./config

RUN mvn dependency:go-offline -B

RUN mvn package

FROM openjdk:8u171-jre-alpine

WORKDIR /telegrambotjava

COPY --from=maven target/telegram-response-bot-java-*.jar ./Bot.jar

CMD ["java", "-jar", "./Bot.jar"]