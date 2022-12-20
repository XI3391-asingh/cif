# customer-service

## How to start customer-service 

Make sure you are in the `customer-service` directry.

```
cd customer-service
```

Run `./gradlew build` to build your service. 

Connect to your local postgres instance. This assumes you have started Postgres using Docker Compose setup in `local` directory.

```
psql postgresql://postgres:postgres@localhost:5432/postgres
```

Next, create the database

```
create database customerservicedb;
```

Quit psql

```
\q
```

Run the db migration script.

```
java -jar build/libs/customer-service-1.0-SNAPSHOT-all.jar db migrate config.yml
```
Start the application by running following command

```
java -jar build/libs/customer-service-1.0-SNAPSHOT-all.jar server config.yml
```

To check that your application is running enter url `http://localhost:11699`

You run the following http command to test the API endpoint. Make sure to install [HTTPie](https://httpie.io/cli)

```
http :11699/customers name=Dummy
```

Response

```
HTTP/1.1 201 Created
Content-Length: 62
Content-Type: application/json
Date: Fri, 10 Jun 2022 08:11:06 GMT
```

```json
{
    "id": "485d5426-affc-4922-9a5f-d6f8cf79741d",
    "name": "Dummy"
}

```

## Health Check


To see your application's health enter url `http://localhost:11700/healthcheck`

## Docker image

```
docker build -t com.finx/customer-service .
```

Run the docker image

```
docker run -p 11699:11699 com.finx/customer-service
```