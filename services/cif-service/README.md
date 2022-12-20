# cif-service

## How to start cif-service

Make sure you are in the `cif-service` directry.

```
cd cif-service
```

Run `./gradlew build` to build your service.

Connect to your local postgres instance. This assumes you have started Postgres using Docker Compose setup in `local`
directory.

```
psql postgresql://postgres:postgres@localhost:5432/postgres
```

Next, create the database

```
create database cifservicedb;
```

Quit psql

```
\q
```

Run the db migration script.

```
java -jar build/libs/cif-service-1.0-SNAPSHOT-all.jar db migrate config.yml
```

Start the application by running following command

```
java -jar build/libs/cif-service-1.0-SNAPSHOT-all.jar server config.yml
```

To check that your application is running enter url `http://localhost:11699`

## Health Check

To see your application's health enter url `http://localhost:11700/healthcheck`

## Docker image

```
docker build -t com.cif/cif-service .
```

Run the docker image

```
docker run -p 11699:11699 com.cif/cif-service
```

## Party Service - Release notes


## [![Release Note Date](https://img.shields.io/badge/date-30--09--2022-blue)]()

## What's New

- Contact delete API

- Document delete API

- Address delete API

- Advance Search API

## Bug Fixes

- NA

  

## Deprecated

- Search By Mobile Number

---

## [![Release Note Date](https://img.shields.io/badge/date-30--08--2022-blue)]()

## What's New

- Dedupe Verification API

- Fatca Create | Update | Fetch | Fetch-All APIs

- Memo Create | Update | Fetch | Fetch-All APIs

## Bug Fixes

- Fixed bug to allow multiple party id in fetch API

  

## Deprecated

- NA

---

## [![Release Note Date](https://img.shields.io/badge/date-30--07--2022-blue)]()

## What's New

- Create Party API

- Create Party Address API

- Create Party Documents API

- Fetch Party Details By Party Id

- Update Party API

- Address Create | Update | Fetch | Fetch-All APIs

- Contacts Create | Update | Fetch | Fetch-All APIs

- Documents Create | Update | Fetch | Fetch-All APIs

- Party Risk Create | Update | Fetch | Fetch-All APIs

- Party Soft Delete API

## Bug Fixes

- NA

  

## Deprecated

- NA

---

## [![Release Note Date](https://img.shields.io/badge/date-30--06--2022-blue)]()

## What's New

- Distinctive Search API

- Search By Mobile Number API

- Update Party Email Id | Mobile | Contact API

## Bug Fixes

- NA

  

## Deprecated

- NA

---
