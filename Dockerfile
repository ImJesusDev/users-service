FROM openjdk:12
VOLUME /tmp
WORKDIR '/app'
COPY . .
RUN ./mvnw clean install 
ENTRYPOINT java  -jar /app/target/users-microservice-0.0.1-SNAPSHOT.jar