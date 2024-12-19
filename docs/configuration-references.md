# Configuration References

## Service Configurations <Badge type="warning" text="1.0.0-SNAPSHOT" />

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
| SERVICE_API_KEY_VALUES           | The API key list that is accepted, by using the following format: "key1,key2,...". If the list is empty no API key authorization will be done.                                            | string[] | [ ]     |
| SERVICE_API_AUTHORIZATION_ENABLE | Whether or not the APIs (except the user APIs) should be secured by the JWT token authorization.                                                                                          | boolean  | true    |
| SERVICE_USE_TESTING_CONTEXT      | Whether the feature toggles should have an additional testing context or not. This option is helpful if you want to deploy an instance and use it for multiple stages in the development. | boolean  | false   |
