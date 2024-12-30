import {createStore} from 'zustand/vanilla'
import {FeatureToggle} from "@/src/types/feature-toggle";
import {
    featureToggles_create,
    featureToggles_deleteById,
    featureToggles_getAll,
    featureToggles_getById
} from "@/src/actions/feature-toggles";
import {CreateFeatureToggleInputs} from "@/src/types/create-feature-toggle-inputs";

export type FeatureToggleState = {
    featureToggles: FeatureToggle[]
}

export type FeatureToggleActions = {
    getById: (id: number) => Promise<FeatureToggle>
    getAll: () => Promise<FeatureToggle[]>
    create: (input: CreateFeatureToggleInputs) => void
    deleteById: (id: number) => void
}

export type FeatureToggleStore = FeatureToggleState & FeatureToggleActions

const defaultInitState: FeatureToggleState = {
    featureToggles: [],
}

export const createFeatureToggleStore = (initState: FeatureToggleState = defaultInitState) => {
    return createStore<FeatureToggleStore>()((set) => ({
        ...initState, getById: async (id: number) => {
            return await featureToggles_getById(id);
        }, getAll: async () => {
            const featureTogglesResponse = await featureToggles_getAll();
            set((state) => ({
                featureToggles: featureTogglesResponse
            }));
            return featureTogglesResponse;
        }, create: async (input: CreateFeatureToggleInputs) => {
            await featureToggles_create(input);
            const featureTogglesResponse = await featureToggles_getAll();
            set((state) => ({
                featureToggles: featureTogglesResponse
            }));
        }, deleteById: async (id: number) => {
            await featureToggles_deleteById(id);
            const featureTogglesResponse = await featureToggles_getAll();
            set((state) => ({
                featureToggles: featureTogglesResponse
            }));
        },
    }))
}
