# 🧱 Build Stage
FROM gradle:8.12.1-jdk21 AS build
COPY --chown=gradle:gradle ../.. /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon --stacktrace --info

# 🚀 Runtime Stage
FROM openjdk:21-jdk-slim

EXPOSE 8081

COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

# 타임존 설정 (Asia/Seoul)
RUN apt-get update && \
    apt-get install -y tzdata && \
    ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone && \
    apt-get clean

CMD ["java", "-jar", "/app/app.jar"]
