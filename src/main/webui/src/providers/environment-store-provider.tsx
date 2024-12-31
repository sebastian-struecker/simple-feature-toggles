'use client'

import React, {createContext, useContext, useRef} from 'react'
import {useStore} from 'zustand'
import {createEnvironmentStore, EnvironmentStore} from "@/src/stores/environment-store";

export type EnvironmentStoreApi = ReturnType<typeof createEnvironmentStore>;

export const EnvironmentStoreContext = createContext<EnvironmentStoreApi | undefined>(undefined);


export const EnvironmentStoreProvider = ({
                                             children,
                                         }: Readonly<{
    children: React.ReactNode;
}>) => {
    const storeRef = useRef<EnvironmentStoreApi>(null);
    if (!storeRef.current) {
        storeRef.current = createEnvironmentStore();
    }

    return (<EnvironmentStoreContext.Provider value={storeRef.current}>
        {children}
    </EnvironmentStoreContext.Provider>)
}

export const useEnvironmentStore = <T, >(selector: (store: EnvironmentStore) => T,): T => {
    const context = useContext(EnvironmentStoreContext)

    if (!context) {
        throw new Error(`useEnvironmentStore must be used within EnvironmentStoreProvider`);
    }

    return useStore(context, selector);
}
