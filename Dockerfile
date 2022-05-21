FROM openjdk:8-jdk-alpine
COPY ./target/MicroserviceDiscoveryServer-0.0.1-SNAPSHOT.war MicroserviceDiscoveryServer-0.0.1-SNAPSHOT.war
CMD [ "sh", "-c", "java -jar /MicroserviceDiscoveryServer-0.0.1-SNAPSHOT.war > /tmp/output.log"]