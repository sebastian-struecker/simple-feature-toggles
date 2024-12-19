namespace NodeJS {
    interface ProcessEnv {
        KEYCLOAK_PROVIDER: KeycloakProviderEnv;
        AZURE_ACTIVE_DIRECTORY_PROVIDER: AzureActiveDirectoryProviderEnv;
    }

    interface KeycloakProviderEnv {
        KEYCLOAK_CLIENT_ID: string;
        KEYCLOAK_CLIENT_SECRET: string;
        KEYCLOAK_ISSUER: string;
    }

    interface AzureActiveDirectoryProviderEnv {
        AZURE_AD_CLIENT_ID: string;
        AZURE_AD_CLIENT_SECRET: string;
        AZURE_AD_TENANT_ID: string;
    }

}
