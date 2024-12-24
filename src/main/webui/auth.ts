import NextAuth, {DefaultSession} from "next-auth"
import Keycloak from "next-auth/providers/keycloak";

/**
 * For more information see:
 * https://stackoverflow.com/questions/69068495/how-to-get-the-provider-access-token-in-next-auth
 */
declare module "next-auth" {
    interface Session {
        user: {
            token?: string;
        } & DefaultSession["user"];
        access_token: string;
    }
}

export const {handlers, auth, signIn, signOut} = NextAuth({
    providers: [Keycloak], callbacks: {
        async jwt({token, account}) {
            if (account) {
                token = Object.assign({}, token, {access_token: account.access_token});
            }
            return token
        }, async session({session, token}) {
            if (session) {
                session = Object.assign({}, session, {access_token: token.access_token})
            }
            return session
        }, async authorized({auth}) {
            return !!auth
        },
    }, session: {
        strategy: "jwt",
    },
});
