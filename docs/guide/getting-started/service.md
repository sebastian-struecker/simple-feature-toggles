# Service

Get started with the simple-feature-toggles **Service** container.

## Before you start

Make sure you have a running:
- [Docker](https://www.docker.com/) environment.
- [PostgreSQL](https://www.postgresql.org/) instance version 16 or higher.

## Start the service

::: code-group

```sh [docker]
docker run -p 8080:8080 \
-e SERVICE_DATASOURCE_USERNAME=${DATASOURCE_USERNAME}  \
-e SERVICE_DATASOURCE_PASSWORD=${DATASOURCE_PASSWORD}  \
-e SERVICE_DATASOURCE_HOST=${DATASOURCE_HOST}  \
-e SERVICE_DATASOURCE_PORT=5432  \
-e SERVICE_DATASOURCE_DATABASE=${DATASOURCE_DATABASE}  \
-e SERVICE_API_AUTHORIZATION_ENABLE=false \
-e SERVICE_USE_TESTING_CONTEXT=false \
ghcr.io/sebastian-struecker/simple-feature-toggles/service:1.0.0-snapshot
```

## Configuration References
