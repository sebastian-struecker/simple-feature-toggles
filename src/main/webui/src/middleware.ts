import {auth} from "@/auth"

export default auth((req) => {
    if (!req.auth && !req.nextUrl.pathname.includes("/api/auth/signin")) {
        const newUrl = new URL("/api/auth/signin", req.nextUrl.origin)
        return Response.redirect(newUrl)
    } else if(req?.auth?.expires_at && !req.nextUrl.pathname.includes("/api/auth/signin")) {
        const expirationDate = new Date(req?.auth?.expires_at * 1000);
        if(expirationDate.valueOf() < Date.now()) {
            const newUrl = new URL("/api/auth/signin", req.nextUrl.origin)
            return Response.redirect(newUrl)
        }
    }
    console.log(req?.auth?.access_token);
})

export const config = {
    matcher: ["/((?!api|_next/static|_next/image|icon.ico|favicon.ico).*)"],
}
