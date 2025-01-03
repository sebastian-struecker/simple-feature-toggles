import {createStore} from 'zustand/vanilla'
import {Environment} from "@/src/types/environment";
import {CreateEnvironmentInputs} from "@/src/types/create-environment-inputs";
import {
    environments_create,
    environments_deleteById,
    environments_getAll,
    environments_getById,
    environments_update
} from '@/src/actions/environments';
import {UpdateEnvironmentInputs} from "@/src/types/update-environment-inputs";
import {persist} from 'zustand/middleware'


export type EnvironmentState = {
    environments: Environment[]
    hasHydrated: boolean
}

export type EnvironmentActions = {
    getById: (id: number) => Promise<Environment>
    getAll: () => Promise<Environment[]>
    create: (input: CreateEnvironmentInputs) => void
    update: (input: UpdateEnvironmentInputs) => void
    deleteById: (id: number) => void
    setHasHydrated: (value: boolean) => void
}

export type EnvironmentStore = EnvironmentState & EnvironmentActions

const defaultInitState: EnvironmentState = {
    environments: [], hasHydrated: false
}

export const createEnvironmentStore = (initState: EnvironmentState = defaultInitState) => {
    return createStore<EnvironmentStore>()(persist((set) => ({
        ...initState, getById: async (id: number) => {
            return await environments_getById(id);
        }, getAll: async () => {
            const response = await environments_getAll();
            set(() => ({
                environments: response
            }));
            return response;
        }, create: async (input: CreateEnvironmentInputs) => {
            await environments_create(input);
            const response = await environments_getAll();
            set(() => ({
                environments: response
            }));
        }, update: async (input: UpdateEnvironmentInputs) => {
            await environments_update(input);
            const response = await environments_getAll();
            set(() => ({
                environments: response
            }));
        }, deleteById: async (id: number) => {
            await environments_deleteById(id);
            const response = await environments_getAll();
            set(() => ({
                environments: response
            }));
        }, setHasHydrated: (value: boolean) => {
            set({
                hasHydrated: value
            });
        }
    }), {
        name: 'environment-storage',
        partialize: (state) => ({environments: state.environments}),
        onRehydrateStorage: (state) => {
            return () => state.setHasHydrated(true);
        }
    }));
}
