'use client'

import React, {createContext, useContext, useRef} from 'react'
import {useStore} from 'zustand'
import {createFeatureToggleStore, FeatureToggleStore} from "@/src/stores/feature-toggle-store";


export type FeatureToggleStoreApi = ReturnType<typeof createFeatureToggleStore>;

export const FeatureToggleStoreContext = createContext<FeatureToggleStoreApi | undefined>(undefined);


export const FeatureToggleStoreProvider = ({
                                               children,
                                           }: Readonly<{
    children: React.ReactNode;
}>) => {
    const storeRef = useRef<FeatureToggleStoreApi>(null);
    if (!storeRef.current) {
        storeRef.current = createFeatureToggleStore();
    }

    return (<FeatureToggleStoreContext.Provider value={storeRef.current}>
        {children}
    </FeatureToggleStoreContext.Provider>)
}

export const useFeatureToggleStore = <T, >(selector: (store: FeatureToggleStore) => T,): T => {
    const featureToggleStoreContext = useContext(FeatureToggleStoreContext)

    if (!featureToggleStoreContext) {
        throw new Error(`useFeatureToggleStore must be used within FeatureToggleStoreProvider`);
    }

    return useStore(featureToggleStoreContext, selector);
}
