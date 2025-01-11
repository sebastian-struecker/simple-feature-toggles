'use client'

import React, {createContext, useContext, useRef} from 'react'
import {useStore} from 'zustand'
import {ApiKeyStore, createApiKeyStore} from "@/src/stores/api-key-store";

export type ApiKeyStoreApi = ReturnType<typeof createApiKeyStore>;

export const ApiKeyStoreContext = createContext<ApiKeyStoreApi | undefined>(undefined);


export const ApiKeyStoreProvider = ({
                                        children,
                                    }: Readonly<{
    children: React.ReactNode;
}>) => {
    const storeRef = useRef<ApiKeyStoreApi>(null);
    if (!storeRef.current) {
        storeRef.current = createApiKeyStore();
    }

    return (<ApiKeyStoreContext.Provider value={storeRef.current}>
        {children}
    </ApiKeyStoreContext.Provider>)
}

export const useApiKeyStore = <T, >(selector: (store: ApiKeyStore) => T,): T => {
    const context = useContext(ApiKeyStoreContext)

    if (!context) {
        throw new Error(`useApiKeyStore must be used within ApiKeyStoreProvider`);
    }

    return useStore(context, selector);
}
