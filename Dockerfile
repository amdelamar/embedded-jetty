FROM openjdk:8-jre-alpine

# Copy app to new directory
RUN mkdir -p /usr/app
COPY build/libs/embedded-jetty-0.1.0.jar /usr/app

# Work out of the directory
WORKDIR /usr/app

# Expose http ports
EXPOSE 8080 8443

# Start java application
CMD ["java","-jar","embedded-jetty-0.1.0.jar"]
