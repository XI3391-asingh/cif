# cif-syncer-service

## How to start cif-syncer-service 

Make sure you are in the `cif-syncer-service` directry.

```
cd cif-syncer-service
```

Run `./gradlew build` to build your service. 

Connect to your local postgres instance. This assumes you have started Postgres using Docker Compose setup in `local` directory.

```
psql postgresql://postgres:postgres@localhost:5432/postgres
```

Next, create the database

```
create database syncerservicedb;
```

Quit psql

```
\q
```

Run the db migration script.

```
java -jar build/libs/cif-syncer-service-1.0-SNAPSHOT-all.jar db migrate config.yml
```
Start the application by running following command

```
java -jar build/libs/cif-syncer-service-1.0-SNAPSHOT-all.jar server config.yml
```

To check that your application is running enter url `http://localhost:14893`

## Health Check


To see your application's health enter url `http://localhost:11701/healthcheck`

## Docker image

```
docker build -t com.cif/cif-syncer-service .
```

Run the docker image

```
docker run -p 14893:14893 com.cif/cif-syncer-service
```