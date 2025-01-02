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
import toast from "react-hot-toast";
import { FaCheck } from "react-icons/fa";


export type EnvironmentState = {
    environments: Environment[]
}

export type EnvironmentActions = {
    getById: (id: number) => Promise<Environment>
    getAll: () => Promise<Environment[]>
    create: (input: CreateEnvironmentInputs) => void
    update: (input: UpdateEnvironmentInputs) => void
    deleteById: (id: number) => void
}

export type EnvironmentStore = EnvironmentState & EnvironmentActions

const defaultInitState: EnvironmentState = {
    environments: [],
}

export const createEnvironmentStore = (initState: EnvironmentState = defaultInitState) => {
    return createStore<EnvironmentStore>()((set) => ({
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
        },
    }))
}
