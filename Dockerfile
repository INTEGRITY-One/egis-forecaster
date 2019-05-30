FROM quay.io/whisk/graalvm:latest
VOLUME /tmp
ARG JAR_FILE
WORKDIR /usr/local/
COPY target/forecaster-1.0-SNAPSHOT.jar ./forecaster.jar
COPY target/forecaster-1.0-SNAPSHOT-runner.jar ./forecaster-runner.jar
COPY target/lib ./lib
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usr/local/forecaster-runner.jar"]
EXPOSE 8090