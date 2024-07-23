# Getting Started

Get started with insta-toggles on your machine.

## Before you start

Make sure you have a running:
- [Docker](https://www.docker.com/) environment.
- [PostgreSQL](https://www.postgresql.org/) instance version 16 or higher.
- Authentication provider, like [Keycloak](https://www.keycloak.org/).

## Decide how to use insta-toggles

In every case you want to start by running the [service container](service).
Which enables you to manage feature toggles manually by providing a secured REST API.
Additionally you can choose to:
1. use your own UI, by integrating the [API client](api-client)
2. use the [insta-toggles UI](ui)
