FROM azul/zulu-openjdk:17
WORKDIR /app

# Install netcat
RUN apt update && apt install -y curl wget netcat

# Copy the application JAR
COPY zetfilestore/build/libs/zetfilestore.jar /app.jar

# Copy external configuration files
COPY application.properties /config/application.properties
COPY application-dev.properties /config/application-dev.properties
COPY application-prod.properties /config/application-prod.properties

# Set default environment variables for dev profile
ENV SPRING_PROFILES_ACTIVE=dev
ENV SPRING_CONFIG_ADDITIONAL_LOCATION=/config/

# Expose the port your app will run on
EXPOSE 80

# Run the application
ENTRYPOINT ["java", "-Dspring.config.additional-location=/config/", "-jar", "/app.jar"]

#marek@marek-zezula:/mnt/c/Users/ma43k/IdeaProjects/zetcloud$ docker build -t ma43z/zetcloud-repo:zetfilestore-1.0.12 -f zetfilestore/Dockerfile .
#marek@marek-zezula:/mnt/c/Users/ma43k/IdeaProjects/zetcloud$ docker tag 4b7132833fcc ma43z/zetcloud-repo:zetfilestore-1.0.12
#marek@marek-zezula:/mnt/c/Users/ma43k/IdeaProjects/zetcloud$ docker push ma43z/zetcloud-repo:zetfilestore-1.0.12