import {auth} from "@/auth"
import {isExpired} from "@/src/utils/jwt";

export default auth((req) => {
    if (!req.auth && !req.nextUrl.pathname.includes("/api/auth/signin")) {
        const newUrl = new URL("/api/auth/signin", req.nextUrl.origin)
        return Response.redirect(newUrl)
    } else if (req?.auth?.expires_at && !req.nextUrl.pathname.includes("/api/auth/signin")) {
        if (isExpired(req?.auth?.expires_at)) {
            const newUrl = new URL("/api/auth/signin", req.nextUrl.origin)
            return Response.redirect(newUrl)
        }
    }
})

export const config = {
    matcher: ["/((?!api|_next/static|_next/image|icon.ico|favicon.ico).*)"],
}
