import {auth} from "@/auth";
import toast from "react-hot-toast";

export default async function fetcher(path: string, options: RequestInit = {}) {
    const session = await auth();
    const url = process.env.BACKEND_URL;
    console.log(session?.access_token);
    options.headers = {
        ...options.headers, Authorization: `Bearer ${session?.access_token}`,
    };
    return fetch(url + path, options).catch((error) => {
        console.error(error);
        toast.success('Error:', error);
    });
}
