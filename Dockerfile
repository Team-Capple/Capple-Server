FROM amazoncorretto:17-alpine-jdk AS builder

#작업 디렉토리를 /app으로 설정
WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src
COPY config ./config

RUN ./gradlew bootJar

FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT java -jar "-Dspring.profiles.active=dev" /app/app.jar 
