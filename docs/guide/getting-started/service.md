# Service

Get started with the insta-toggles **Service** container.

## Before you start

Make sure you have a running:
- [Docker](https://www.docker.com/) environment.
- [PostgreSQL](https://www.postgresql.org/) instance version 16 or higher.
- Authentication provider, like [Keycloak](https://www.keycloak.org/).

## Start the service

::: code-group

```sh [docker]
docker run -p 8080:8080 \
-e SERVICE_DATASOURCE_USERNAME=${DATASOURCE_USERNAME}  \
-e SERVICE_DATASOURCE_PASSWORD=${DATASOURCE_PASSWORD}  \
-e SERVICE_DATASOURCE_HOST=${DATASOURCE_HOST}  \
-e SERVICE_DATASOURCE_PORT=5432  \
-e SERVICE_DATASOURCE_DATABASE=${DATASOURCE_DATABASE}  \
-e SERVICE_OIDC_AUTH_SERVER_URL=${OIDC_AUTH_SERVER_URL}  \
-e SERVICE_OIDC_CLIENT_ID=${OIDC_CLIENT_ID}  \
-e SERVICE_OIDC_TOKEN_JWT_ISSUER=${OIDC_TOKEN_JWT_ISSUER}  \
-e SERVICE_OIDC_TOKEN_JWT_AUDIENCE=${OIDC_TOKEN_JWT_AUDIENCE}  \
-e SERVICE_API_KEY_ENABLE=true \
-e SERVICE_API_KEY_VALUES=test,prod \
-e SERVICE_API_AUTHORIZATION_ENABLE=true \
-e SERVICE_USE_TESTING_CONTEXT=false \
ghcr.io/sebastian-struecker/insta-toggles/service:1.0.0-snapshot
```

## Configuration References

| Configuration property           | Description                                                                                                                                                                               | Type     | Default |
|----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|---------|
| SERVICE_DATASOURCE_USERNAME      | The datasource username.                                                                                                                                                                  | string   |         |
| SERVICE_DATASOURCE_PASSWORD      | The datasource password.                                                                                                                                                                  | string   |         |
| SERVICE_DATASOURCE_HOST          | The datasource host.                                                                                                                                                                      | string   |         |
| SERVICE_DATASOURCE_PORT          | The datasource port.                                                                                                                                                                      | int      |         |
| SERVICE_DATASOURCE_DATABASE      | The datasource database name.                                                                                                                                                             | string   |         |
| SERVICE_OIDC_AUTH_SERVER_URL     | The OIDC authentication server url.                                                                                                                                                       | string   |         |
| SERVICE_OIDC_CLIENT_ID           | The OIDC authentication client id.                                                                                                                                                        | string   |         |
| SERVICE_OIDC_TOKEN_JWT_ISSUER    | The OIDC authentication JWT token issuer.                                                                                                                                                 | string   |         |
| SERVICE_OIDC_TOKEN_JWT_AUDIENCE  | The OIDC authentication JWT token audience.                                                                                                                                               | string   |         |
| SERVICE_API_KEY_ENABLE           | Whether or not the user APIs should be secured by an API key authorization.                                                                                                               | boolean  | true    |
| SERVICE_API_KEY_VALUES           | The API key list that is accepted, by using the following format: "key1,key2,...".                                                                                                        | string[] | [ ]     |
| SERVICE_API_AUTHORIZATION_ENABLE | Whether or not the APIs (except the user APIs) should be secured by the JWT token authorization.                                                                                          | boolean  | true    |
| SERVICE_USE_TESTING_CONTEXT      | Whether the feature toggles should have an additional testing context or not. This option is helpful if you want to deploy an instance and use it for multiple stages in the development. | boolean  | false   |
