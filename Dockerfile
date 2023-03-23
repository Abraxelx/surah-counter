FROM openjdk:17
EXPOSE 9090
ARG JAR_FILE=target/quran-counter.jar
ADD ${JAR_FILE} quran.jar
ADD serviceAccount.json .
ENTRYPOINT ["java", "-jar", "/quran.jar"]