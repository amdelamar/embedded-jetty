# Embedded-Jetty

A simple Java servlet with embedded Eclipse Jetty server. (HTTP/1.1, HTTP/2, and SSL)

## Prerequisites

* [JDK 1.8](https://www.java.com/en/download/faq/develop.xml)
* [Eclipse](https://eclipse.org/downloads/) or [Spring Tools Suite](https://spring.io/tools) (Optional)
* [Docker](https://docs.docker.com/engine/installation/) (Optional)

## Run Manually

1. Download code `git clone https://github.com/amdelamar/embedded-jetty`
1. `cd embedded-jetty`
1. Run build `./gradlew clean build`
1. Start server `java -jar build/libs/embedded-jetty-0.1.0.jar`
1. Visit [http://localhost:8080/](http://localhost:8080/) to see the app running.

## Run in Docker

1. Download code `git clone https://github.com/amdelamar/embedded-jetty`
1. `cd embedded-jetty`
1. Run build `./gradlew clean build`
1. Build image `docker build -t embjetty .`
1. Run container `docker run -p 8080:8080 embjetty`
1. Visit [http://localhost:8080/](http://localhost:8080/) to see the app running.

## License

[MIT](/LICENSE)
