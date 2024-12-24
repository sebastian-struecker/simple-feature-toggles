"use client";

import {SessionProvider, useSession} from "next-auth/react";
import {LoginButton} from "@/src/app/components/molecules/login-button";
import {LogoutButton} from "@/src/app/components/molecules/logout-button";
import {useEffect, useState} from "react";

export default function Home() {
    const session = useSession();

    const [posts, setPosts] = useState([])

    useEffect(() => {
        async function fetchPosts() {
            const res = await fetch('http://localhost:8080/feature-toggles', {
                headers: {
                    Authorization: "Bearer " + session?.data?.access_token
                }
            })
            const data = await res.json()
        }

        if (session.status == "authenticated") {
            fetchPosts()
        }
    }, [session?.data?.access_token, session.status])

    return (<SessionProvider>
        <p>Welcome, {session.data?.user?.name} - {session.status}</p>
        <p>Token: {session.data?.access_token}</p>
        <LoginButton/>
        <LogoutButton/>
    </SessionProvider>)
}
