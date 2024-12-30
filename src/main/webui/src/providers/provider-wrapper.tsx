"use client";

import {SessionProvider} from "next-auth/react";
import {Session} from "next-auth";
import {FeatureToggleStoreProvider} from "@/src/providers/feature-toggle-store-provider";

export default function ProviderWrapper({session, children}: { session: Session | null, children: React.ReactNode }) {
    return (<SessionProvider session={session} refetchInterval={5 * 60}>
        <FeatureToggleStoreProvider>
            {children}
        </FeatureToggleStoreProvider>
    </SessionProvider>)
}
