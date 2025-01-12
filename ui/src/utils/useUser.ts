import {useSession} from "next-auth/react";

export const useUser = () => {
    const {data} = useSession();
    const user = data?.user;

    const isAdmin: boolean = user?.roles?.includes("sft_admin") || false;
    const isViewer: boolean = user?.roles?.includes("sft_viewer") || false;

    const role = (): "admin" | "viewer" => {
        if (isAdmin) return "admin";
        return "viewer";
    }

    return {user, role, isAdmin, isViewer}
}
