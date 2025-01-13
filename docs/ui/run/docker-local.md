# Docker - Local

This documentation should explain how to use the ``ui`` image locally with [Docker](https://www.docker.com/).

## Installation

### Prerequisites

- A [Docker](https://www.docker.com/) environment.
- A ``service`` instance. More information [here](../../service/index).
- A Keycloak instance. More information [here](../../infrastructure/oidc/keycloak).

### Start the service

::: code-group

```sh [docker]
docker run -p 8080:8080 \
-e AUTH_SECRET=$auth_secret  \
-e AUTH_KEYCLOAK_ID=$auth_keycloak_id  \
-e AUTH_KEYCLOAK_SECRET=$auth_keycloak_secret  \
-e AUTH_KEYCLOAK_ISSUER=$auth_keycloak_issuer  \
-e BACKEND_URL=$backend_url  \
ghcr.io/sebastian-struecker/simple-feature-toggles/ui:1.0.0
```

```yml [docker-compose]
services:
  service:
    image: ghcr.io/sebastian-struecker/simple-feature-toggles/ui:1.0.0
    environment:
      AUTH_SECRET: $auth_secret
      AUTH_KEYCLOAK_ID: $auth_keycloak_id
      AUTH_KEYCLOAK_SECRET: $auth_keycloak_secret
      AUTH_KEYCLOAK_ISSUER: $auth_keycloak_issuer
      BACKEND_URL: $backend_url
    ports:
      - "3000:3000"
```

:::

> All configurations can be found [here](../configuration)
