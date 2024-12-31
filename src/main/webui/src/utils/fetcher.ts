import {auth} from "@/auth";

export default async function fetcher(path: string, options: RequestInit = {}) {
    const session = await auth();
    const url = process.env.BACKEND_URL;
    console.log(session?.access_token);
    options.headers = {
        ...options.headers, Authorization: `Bearer ${session?.access_token}`,
    };
    console.log("before", options.body)
    return fetch(url + path, options);
}
