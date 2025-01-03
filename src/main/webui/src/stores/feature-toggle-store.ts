import {createStore} from 'zustand/vanilla'
import {FeatureToggle} from "@/src/types/feature-toggle";
import {
    featureToggles_create,
    featureToggles_deleteById,
    featureToggles_getAll,
    featureToggles_getById,
    featureToggles_update
} from "@/src/actions/feature-toggles";
import {CreateFeatureToggleInputs} from "@/src/types/create-feature-toggle-inputs";
import {UpdateFeatureToggleInputs} from "@/src/types/update-feature-toggle-inputs";
import {persist} from "zustand/middleware";

export type FeatureToggleState = {
    featureToggles: FeatureToggle[]
    hasHydrated: boolean
}

export type FeatureToggleActions = {
    getById: (id: number) => Promise<FeatureToggle>
    getAll: () => Promise<FeatureToggle[]>
    create: (input: CreateFeatureToggleInputs) => void
    update: (input: UpdateFeatureToggleInputs) => void
    deleteById: (id: number) => void
    setHasHydrated: (value: boolean) => void
}

export type FeatureToggleStore = FeatureToggleState & FeatureToggleActions

const defaultInitState: FeatureToggleState = {
    featureToggles: [], hasHydrated: false
}

export const createFeatureToggleStore = (initState: FeatureToggleState = defaultInitState) => {
    return createStore<FeatureToggleStore>()(persist((set) => ({
        ...initState, getById: async (id: number) => {
            return await featureToggles_getById(id);
        }, getAll: async () => {
            const response = await featureToggles_getAll();
            set(() => ({
                featureToggles: response
            }));
            return response;
        }, create: async (input: CreateFeatureToggleInputs) => {
            await featureToggles_create(input);
            const response = await featureToggles_getAll();
            set(() => ({
                featureToggles: response
            }));
        }, update: async (input: UpdateFeatureToggleInputs) => {
            await featureToggles_update(input);
            const response = await featureToggles_getAll();
            set(() => ({
                featureToggles: response
            }));
        }, deleteById: async (id: number) => {
            await featureToggles_deleteById(id);
            const response = await featureToggles_getAll();
            set(() => ({
                featureToggles: response
            }));
        }, setHasHydrated: (value: boolean) => {
            set({
                hasHydrated: value
            });
        }
    }), {
        name: 'feature-toggle-storage', partialize: (state) => ({
            environments: state.featureToggles
        }), onRehydrateStorage: (state) => {
            return () => state.setHasHydrated(true);
        }
    }))
}
