# FinX Microservices Monorepo

This repository houses all the FinX Microservices and shared libraries.

Following is the list of Microservices

| S.No. | Service                                             | CI Status | Port  | Dev Swagger URL |
| ----- | --------------------------------------------------- | --------- | ----- | --------------- |
| 1     | [customer-service](./services/customer-service)     | To Add    | 11699 | To Add          |
| 2     | [sample-service](./services/sample-service)         | To Add    | 14893 | To Add          |
| 3     | [onboarding-service](./services/onboarding-service) | To Add    | 13361 | To Add          |

## Tools

Please install following tools

| Tool                                      | Version | Required |
| ----------------------------------------- | ------- | -------- |
| Java                                      | 17      | Yes      |
| IntelliJ Idea Community Edition: Java IDE | Latest  | Yes      |
| Markdown Editor MarkText / Typora(Paid)   | Latest  | Optional |
| Git                                       | Latest  | Yes      |
| VS Code: Code editor                      | Latest  | Yes      |
| Docker                                    | Latest  | Yes      |
| Postgres                                  | 13.3    | Yes      |
| DBeaver: DB client                        | Latest  | Optional |
| Redis                                     | 6       | Yes      |
| Python                                    | 3.9     | Optional |
| CookieCutter                              | 1.7.3   | Optional |
| Talisman                                  | Latest  | Yes      |

You will find instructions to install some of the tools in the [local](./local) directory.

## Code Layout

Below are the important files and directories in this repositiory.

* [local](./local): This directory contains instructions to set the local development environment. It also includes docker-compose setup to start important services that Microservices require for development. This includes Postgres, Redis, and Kafka.  
* `gradle`: Each Microservice in this repository uses Gradle as build and dependency management tool. We have also added a root Gradle build script in case developers want to import all the Microservices and libraries in their IDE.
* [services](./services): This contains all the Microservices that we will build for FinX
* [shared](./shared): This contains all the custom libraries that Microservices will use.
* [templates](./templates): It contains the Microservices starter template
* `tools`: This contains a Python script to instantiate new Microservices from Microservices template in the templates directory.
* `.talismanrc`: This file contains entries of files that we don't want talisman to scan. It will ignore the fileas long as the checksum of the file matches the value mentioned in the `checksum` field.
* `CODEOWNERS`: CODEOWNERS file is used to define individuals or teams that are responsible for code in a repository.

## Creating a new Microservice

You will use `msgen` tool to create a new Microservices. You can read the complete documentation for msgen in the [tools](./tools) directory.

First chnage directory tools and then run the `msgen` tool to create a new Microservice.

```
cd tools
./msgen.py --service
```