"use client";

import {SessionProvider} from "next-auth/react";
import {Session} from "next-auth";
import {FeatureToggleStoreProvider} from "@/src/providers/feature-toggle-store-provider";
import {ApiKeyStoreProvider} from "@/src/providers/api-key-store-provider";
import {EnvironmentStoreProvider} from "@/src/providers/environment-store-provider";
import ConfirmationModalProvider from "@/src/providers/confirmation-modal-provider";

export default function ProviderWrapper({session, children}: { session: Session | null, children: React.ReactNode }) {
    return (<SessionProvider session={session} refetchInterval={5 * 60}>
        <EnvironmentStoreProvider>
            <FeatureToggleStoreProvider>
                <ApiKeyStoreProvider>
                    <ConfirmationModalProvider>
                        {children}
                    </ConfirmationModalProvider>
                </ApiKeyStoreProvider>
            </FeatureToggleStoreProvider>
        </EnvironmentStoreProvider>
    </SessionProvider>)
}
