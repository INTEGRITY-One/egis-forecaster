FROM registry.fedoraproject.org/fedora-minimal:latest
VOLUME /tmp
WORKDIR /usr/local/
COPY target/forecaster-1.0-SNAPSHOT-runner forecaster-runner
RUN chmod 777 ./forecaster-runner
ENTRYPOINT ["./forecaster-runner"]
EXPOSE 8090