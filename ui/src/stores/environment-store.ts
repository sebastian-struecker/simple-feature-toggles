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
import toast from "react-hot-toast";


export type EnvironmentState = {
    environments: Environment[]
    selected?: Environment
}

export type EnvironmentActions = {
    getById: (id: number) => Promise<Environment>
    getAll: () => Promise<Environment[]>
    create: (input: CreateEnvironmentInputs) => void
    update: (id: number, input: UpdateEnvironmentInputs) => void
    deleteById: (id: number) => void
    setSelected: (selected?: Environment) => void
}

export type EnvironmentStore = EnvironmentState & EnvironmentActions

const defaultInitState: EnvironmentState = {
    environments: [], selected: undefined
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
            try {
                await environments_create(input);
                toast.success("Environment created successfully");
            } catch (e) {
                console.error(e);
                if (e instanceof Error) {
                    toast.error(e.message);
                }
            }
            const response = await environments_getAll();
            set(() => ({
                environments: response
            }));
        }, update: async (id: number, input: UpdateEnvironmentInputs) => {
            try {
                await environments_update(id, input);
                toast.success("Environment updated successfully");
            } catch (e) {
                console.error(e);
                if (e instanceof Error) {
                    toast.error(e.message);
                }
            }
            const response = await environments_getAll();
            set(() => ({
                environments: response
            }));
        }, deleteById: async (id: number) => {
            try {
                await environments_deleteById(id);
                toast.success("Environment deleted successfully");
            } catch (e) {
                console.error(e);
                if (e instanceof Error) {
                    toast.error(e.message);
                }
            }
            const response = await environments_getAll();
            set(() => ({
                environments: response
            }));
        }, setSelected: (selected?: Environment) => {
            set({
                selected: selected
            });
        }
    }), {
        name: 'environment-storage', partialize: (state) => ({environments: state.environments}),
    }));
}
