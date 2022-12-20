FROM gcr.io/distroless/java17-debian11
COPY build/libs/customer-service-1.0-SNAPSHOT-all.jar /app/app.jar
COPY config.yml /app/config.yml
WORKDIR /app
CMD ["app.jar","server","config.yml"]

