# 베이스 이미지 (Java 17)
FROM openjdk:17


# 작업 디렉토리 설정
WORKDIR /app
# JAR 파일 복사
ARG JAR_FILE=./api/build/libs/api.jar
COPY ${JAR_FILE} app.jar
ARG PROFILE
ENV SPRING_PROFILE=$PROFILE

# application.yml 파일 복사
COPY api/src/main/resources/application*.yml /config/
# 컨테이너 실행 시 실행할 명령어
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=/config/ --spring.profiles.active=$SPRING_PROFILE"]
