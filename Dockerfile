FROM openjdk:11
COPY target/healthcenter-*.jar /home/dev/app.jar
CMD ["java","-jar","/home/dev/app.jar"]