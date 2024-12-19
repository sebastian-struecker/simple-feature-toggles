import NextAuth, {AuthOptions} from "next-auth";
import KeycloakProvider from "next-auth/providers/keycloak"

function getProviders() {
    let providers = [];
    if (process.env.KEYCLOAK_PROVIDER != null) {
        providers.push(KeycloakProvider({
            clientId: process.env.KEYCLOAK_PROVIDER.KEYCLOAK_CLIENT_ID,
            clientSecret: process.env.KEYCLOAK_PROVIDER.KEYCLOAK_CLIENT_SECRET,
            issuer: process.env.KEYCLOAK_PROVIDER.KEYCLOAK_ISSUER
        }));
    }
    return providers;
}

export const authOptions: AuthOptions = {
    providers: getProviders()
}
const handler = NextAuth(authOptions);
export {handler as GET, handler as POST}
