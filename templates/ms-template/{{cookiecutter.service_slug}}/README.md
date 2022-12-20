# {{ cookiecutter.service_slug }}

## How to start {{ cookiecutter.service_slug }} 

Make sure you are in the `{{ cookiecutter.service_slug }}` directry.

```
cd {{ cookiecutter.service_slug }}
```

Run `./gradlew build` to build your service. 

Connect to your local postgres instance. This assumes you have started Postgres using Docker Compose setup in `local` directory.

```
psql postgresql://postgres:postgres@localhost:5432/postgres
```

Next, create the database

```
create database {{ cookiecutter.db_name }};
```

Quit psql

```
\q
```

Run the db migration script.

```
java -jar build/libs/{{ cookiecutter.service_slug }}-1.0-SNAPSHOT-all.jar db migrate config.yml
```
Start the application by running following command

```
java -jar build/libs/{{ cookiecutter.service_slug }}-1.0-SNAPSHOT-all.jar server config.yml
```

To check that your application is running enter url `http://localhost:{{ cookiecutter.service_port }}`

You run the following http command to test the API endpoint. Make sure to install [HTTPie](https://httpie.io/cli)

```
http :{{ cookiecutter.service_port }}/{{ cookiecutter.entity.lower() + 's' }} name=Dummy
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


To see your application's health enter url `http://localhost:{{ cookiecutter.admin_port }}/healthcheck`

## Docker image

```
docker build -t com.cif/{{ cookiecutter.service_slug }} .
```

Run the docker image

```
docker run -p {{ cookiecutter.service_port }}:{{ cookiecutter.service_port }} com.cif/{{ cookiecutter.service_slug }}
```