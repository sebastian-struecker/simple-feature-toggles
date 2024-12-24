"use client"

import React from 'react'
import {useSession} from "next-auth/react";
import {LoginPage} from "@/src/app/components/pages/login-page";
import {useRouter} from "next/navigation";

export default function Page() {
    const session = useSession();
    const router = useRouter();

    if (session.status == "unauthenticated") {
        return (<LoginPage/>);
    }

    return (<>
        <div>
            Hello
        </div>
    </>);
}
