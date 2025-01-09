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
import toast from "react-hot-toast";

export type ApiKeyState = {
    apiKeys: ApiKey[]
    selected?: ApiKey
}

export type ApiKeyActions = {
    getById: (id: number) => Promise<ApiKey>
    getAll: () => Promise<ApiKey[]>
    create: (input: CreateApiKeyInputs) => void
    update: (id: number, input: UpdateApiKeyInputs) => void
    deleteById: (id: number) => void
    setSelected: (value: ApiKey | undefined) => void
}

export type ApiKeyStore = ApiKeyState & ApiKeyActions

const defaultInitState: ApiKeyState = {
    apiKeys: [], selected: undefined
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
            try {
                await apiKeys_create(input);
                toast.success("Api key created successfully");
            } catch (e) {
                if (e instanceof Error) {
                    toast.error(e.message);
                }
            }
            const response = await apiKeys_getAll();
            set(() => ({
                apiKeys: response
            }));
        }, update: async (id: number, input: UpdateApiKeyInputs) => {
            try {
                await apiKeys_update(id, input);
                toast.success("Api key updated successfully");
            } catch (e) {
                if (e instanceof Error) {
                    toast.error(e.message);
                }
            }
            const response = await apiKeys_getAll();
            set(() => ({
                apiKeys: response
            }));
        }, deleteById: async (id: number) => {
            try {
                await apiKeys_deleteById(id);
                toast.success("Api key deleted successfully");
            } catch (e) {
                if (e instanceof Error) {
                    toast.error(e.message);
                }
            }
            const response = await apiKeys_getAll();
            set(() => ({
                apiKeys: response
            }));
        }, setSelected: (value: ApiKey | undefined) => {
            set({
                selected: value
            });
        }
    }), {
        name: 'api-key-storage', partialize: (state) => ({environments: state.apiKeys}),
    }));
}
