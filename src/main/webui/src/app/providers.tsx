"use client";

import {SessionProvider} from "next-auth/react";
import {Session} from "next-auth";
import {NextUIProvider} from "@nextui-org/react";

export default function Providers({session, children}: { session: Session | null, children: React.ReactNode }) {
    return (<NextUIProvider>
        <SessionProvider session={session}>
            {children}
        </SessionProvider>
    </NextUIProvider>)
}
