"use client"

import React from 'react'
import {useSession} from "next-auth/react";
import {useRouter} from "next/navigation";

export default function Page() {
    const session = useSession();
    const router = useRouter();

    return (<>
        <div>
            Hello
        </div>
    </>);
}
