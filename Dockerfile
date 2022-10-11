FROM openjdk:17-alpine
MAINTAINER Pavel Bodiachevskii
COPY build/libs/pub-sub-0.0.1-SNAPSHOT.jar pub-sub-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/pub-sub-0.0.1-SNAPSHOT.jar"]