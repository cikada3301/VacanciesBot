FROM openjdk:17-alpine as build
ARG SERVICE_PATH
WORKDIR /app
COPY $SERVICE_PATH/gradle gradle
COPY $SERVICE_PATH/build.gradle ./
COPY $SERVICE_PATH/settings.gradle ./
COPY $SERVICE_PATH/gradlew ./
COPY $SERVICE_PATH/src src

RUN /app/gradlew build -x test


FROM bellsoft/liberica-openjre-alpine-musl:17
ARG DEPENDENCY=/app/build
COPY --from=build ${DEPENDENCY}/libs /app/lib
RUN mv /app/lib/*SNAPSHOT.jar /app/lib/app.jar
ENTRYPOINT ["java","-jar","app/lib/app.jar"]