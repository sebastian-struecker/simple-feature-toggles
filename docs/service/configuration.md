# Service Configuration References

| Configuration property    | Description                                                                                      | Type    | Default |
|---------------------------|--------------------------------------------------------------------------------------------------|---------|---------|
| DATASOURCE_USERNAME       | The datasource username.                                                                         | string  |         |
| DATASOURCE_PASSWORD       | The datasource password.                                                                         | string  |         |
| DATASOURCE_HOST           | The datasource host.                                                                             | string  |         |
| DATASOURCE_PORT           | The datasource port.                                                                             | int     |         |
| DATASOURCE_DATABASE       | The datasource database name.                                                                    | string  |         |
| OIDC_AUTH_SERVER_URL      | The OIDC authentication server url.                                                              | string  |         |
| OIDC_CLIENT_ID            | The OIDC authentication client id.                                                               | string  |         |
| OIDC_TOKEN_JWT_ISSUER     | The OIDC authentication JWT token issuer.                                                        | string  |         |
| OIDC_TOKEN_JWT_AUDIENCE   | The OIDC authentication JWT token audience.                                                      | string  |         |
| API_AUTHORIZATION_ENABLED | Whether or not the APIs (except the user APIs) should be secured by the JWT token authorization. | boolean | true    |
