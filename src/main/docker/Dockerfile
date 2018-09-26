FROM frolvlad/alpine-oraclejdk8:cleaned

COPY oom.generator.jar /bin/app.jar
EXPOSE 8080

ENV JAVA_OPTS=""
ENTRYPOINT java $JAVA_OPTS -jar /bin/app.jar