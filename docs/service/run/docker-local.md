# Docker - Local

This documentation should explain how to use the ``service`` image locally with [Docker](https://www.docker.com/).

## Installation

### Prerequisites

- A [Docker](https://www.docker.com/) environment.
- A Keycloak instance. More information [here](../../infrastructure/oidc/keycloak).
- A PostgreSQL instance version 16 or higher. More information [here](../../infrastructure/database/postgresql).

### Start the service

::: code-group

```sh [docker]
docker run -p 8080:8080 \
-e DATASOURCE_USERNAME=$db_username  \
-e DATASOURCE_PASSWORD=$db_password  \
-e DATASOURCE_HOST=$db_host  \
-e DATASOURCE_PORT=$db_port  \
-e DATASOURCE_DATABASE=$db_database  \
-e OIDC_AUTH_SERVER_URL=$oidc_auth_server  \
-e OIDC_CLIENT_ID=$oidc_client_id  \
-e OIDC_TOKEN_JWT_ISSUER=$oidc_token_jwt_issuer  \
-e OIDC_TOKEN_JWT_AUDIENCE=$oidc_token_jwt_audience  \
ghcr.io/sebastian-struecker/simple-feature-toggles/service:1.0.0
```

```yml [docker-compose]
services:
  service:
    image: ghcr.io/sebastian-struecker/simple-feature-toggles/service:1.0.0
    environment:
      DATASOURCE_USERNAME: $db_username
      DATASOURCE_PASSWORD: $db_password
      DATASOURCE_DATABASE: $db_database
      DATASOURCE_HOST: $db_host
      DATASOURCE_PORT: $db_port
      OIDC_AUTH_SERVER_URL: $oidc_auth_server
      OIDC_CLIENT_ID: $oidc_client_id
      OIDC_TOKEN_JWT_ISSUER: $oidc_token_jwt_issuer
      OIDC_TOKEN_JWT_AUDIENCE: $oidc_token_jwt_audience
    ports:
      - "8080:8080"
```

:::

> All configurations can be found [here](../configuration)
