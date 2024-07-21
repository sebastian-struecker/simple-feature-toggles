# Getting Started

Get started with insta-toggles on your machine

## Before you start

- Make sure you have a running [Docker](https://www.docker.com/) environment.
- Running [PostgreSQL](https://www.postgresql.org/) instance version 16 or higher.
- Running Authentication provider, like [Keycloak](https://www.keycloak.org/).
    - Please check [Authorization Concept](./authorization-concept) for more information.

## Decide which setup to use

You can choose to use only the insta-toggles [service](#start-the-service), which requires you to manage feature toggles manually using the REST API.
In addition, you can choose to use the provided [UI](#start-the-ui) to simplify feature toggle management.

## Start the service

::: code-group

```sh [docker]
docker run -p 8080:8080 \
-e DATASOURCE_USERNAME=${DATASOURCE_USERNAME}  \
-e DATASOURCE_PASSWORD=${DATASOURCE_PASSWORD}  \
-e DATASOURCE_HOST=${DATASOURCE_HOST}  \
-e DATASOURCE_PORT=5432  \
-e DATASOURCE_DATABASE=${DATASOURCE_DATABASE}  \
-e OIDC_AUTH_SERVER_URL=${OIDC_AUTH_SERVER_URL}  \
-e OIDC_CLIENT_ID=${OIDC_CLIENT_ID}  \
-e OIDC_TOKEN_JWT_ISSUER=${OIDC_TOKEN_JWT_ISSUER}  \
-e OIDC_TOKEN_JWT_AUDIENCE=${OIDC_TOKEN_JWT_AUDIENCE}  \
ghcr.io/sebastian-struecker/insta-toggles/service:1.0.0-snapshot
```

## Start the UI

Will come in the future
