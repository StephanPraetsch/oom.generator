# this is a sample application to control your used memory in intention to analyze your environment how it behaves.
we tested
  * **our OOM notification setup** (for instance send a mail on OOM with [-XX:OnOutOfMemoryError](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/clopts001.html))
  * **has your system enough power** (for instance is your docker container configured well)

# how to use that
build and run this app with maven
```
mvn clean package
java -jar target/oom.generator-0.0.1-SNAPSHOT.jar
```

## add to current used memory
  * 500 MB http://localhost:8080/memory/?add=524288000
  * 1G http://localhost:8080/memory/?add=1073741824
  * 2G http://localhost:8080/memory/?add=2147483648

## clear the added memory
  * http://localhost:8080/clear-memory

## generate out of memory
  * OOM http://localhost:8080/generate-oom

## current used memory 
  * http://localhost:8080/actuator/metrics/jvm.memory.used

## max jvm memory
  * http://localhost:8080/actuator/metrics/jvm.memory.max

## max system memory
  * is there a spring boot endpoint?
