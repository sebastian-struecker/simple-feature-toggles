import NextAuth, {DefaultSession} from "next-auth"
import Keycloak from "next-auth/providers/keycloak";
import {parseJwt} from "@/src/utils/jwt";

/**
 * For more information see:
 * https://stackoverflow.com/questions/69068495/how-to-get-the-provider-access-token-in-next-auth
 */
declare module "next-auth" {
    interface Session {
        user: {
            token?: string; roles?: string[];
        } & DefaultSession["user"];
        access_token: string;
        expires_at?: number;
    }
}

/**
 * For more information see:
 * https://github.com/nextauthjs/next-auth/issues/9645
 */
declare module '@auth/core/jwt' {
    interface JWT {
        roles?: string[];
        accessToken?: string
        expires_at?: number
        refresh_token?: string
    }
}


export const {handlers, auth, signIn, signOut} = NextAuth({
    providers: [Keycloak], callbacks: {
        async jwt({token, account}) {
            let roles: string [] = []
            if (account?.access_token) {
                const decodedToken = parseJwt(account?.access_token)
                if (decodedToken) {
                    roles = decodedToken.realm_access.roles
                }
            }
            if (account) {
                return {
                    ...token,
                    roles: roles,
                    access_token: account.access_token,
                    expires_at: account.expires_at,
                    refresh_token: account.refresh_token,
                }
            }
            return token;
        }, async session({session, token}) {
            if (session) {
                session = Object.assign({}, session, {access_token: token.access_token})
                session.expires_at = token.expires_at;
                session.user.roles = token.roles;
            }
            return session;
        }, async authorized({auth}) {
            return !!auth
        },
    }, session: {
        strategy: "jwt",
    },
});
