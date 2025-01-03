import {createStore} from 'zustand/vanilla'
import {ApiKey} from "@/src/types/api-key";
import {CreateApiKeyInputs} from "@/src/types/create-api-key-inputs";
import {
    apiKeys_create,
    apiKeys_deleteById,
    apiKeys_getAll,
    apiKeys_getById,
    apiKeys_update
} from "@/src/actions/api-keys";
import {UpdateApiKeyInputs} from "@/src/types/update-api-key-inputs";
import {persist} from 'zustand/middleware'

export type ApiKeyState = {
    apiKeys: ApiKey[]
    hasHydrated: boolean
}

export type ApiKeyActions = {
    getById: (id: number) => Promise<ApiKey>
    getAll: () => Promise<ApiKey[]>
    create: (input: CreateApiKeyInputs) => void
    update: (input: UpdateApiKeyInputs) => void
    deleteById: (id: number) => void
    setHasHydrated: (value: boolean) => void
}

export type ApiKeyStore = ApiKeyState & ApiKeyActions

const defaultInitState: ApiKeyState = {
    apiKeys: [], hasHydrated: false
}

export const createApiKeyStore = (initState: ApiKeyState = defaultInitState) => {
    return createStore<ApiKeyStore>()(persist((set) => ({
        ...initState, getById: async (id: number) => {
            return await apiKeys_getById(id);
        }, getAll: async () => {
            const response = await apiKeys_getAll();
            set(() => ({
                apiKeys: response
            }));
            return response;
        }, create: async (input: CreateApiKeyInputs) => {
            await apiKeys_create(input);
            const response = await apiKeys_getAll();
            set(() => ({
                apiKeys: response
            }));
        }, update: async (input: UpdateApiKeyInputs) => {
            await apiKeys_update(input);
            const response = await apiKeys_getAll();
            set(() => ({
                apiKeys: response
            }));
        }, deleteById: async (id: number) => {
            await apiKeys_deleteById(id);
            const response = await apiKeys_getAll();
            set(() => ({
                apiKeys: response
            }));
        }, setHasHydrated: (value: boolean) => {
            set({
                hasHydrated: value
            });
        }
    }), {
        name: 'api-key-storage',
        partialize: (state) => ({environments: state.apiKeys}),
        onRehydrateStorage: (state) => {
            return () => state.setHasHydrated(true);
        }
    }));
}
