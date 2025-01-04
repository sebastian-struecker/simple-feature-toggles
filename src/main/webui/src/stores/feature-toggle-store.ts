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
import toast from "react-hot-toast";

export type FeatureToggleState = {
    featureToggles: FeatureToggle[]
    selected?: FeatureToggle
    isLoading: boolean
}

export type FeatureToggleActions = {
    getById: (id: number) => Promise<FeatureToggle>
    getAll: () => Promise<FeatureToggle[]>
    create: (input: CreateFeatureToggleInputs) => void
    update: (input: UpdateFeatureToggleInputs) => void
    deleteById: (id: number) => void
    setSelected: (value: FeatureToggle | undefined) => void
    setIsLoading: (value: boolean) => void
}

export type FeatureToggleStore = FeatureToggleState & FeatureToggleActions

const defaultInitState: FeatureToggleState = {
    featureToggles: [], selected: undefined, isLoading: false
}

export const createFeatureToggleStore = (initState: FeatureToggleState = defaultInitState) => {
    return createStore<FeatureToggleStore>()(persist((set) => ({
        ...initState, getById: async (id: number) => {
            return await featureToggles_getById(id);
        }, getAll: async () => {
            set(() => ({
                isLoading: true
            }));
            const response = await featureToggles_getAll();
            set(() => ({
                featureToggles: response, isLoading: false
            }));
            return response;
        }, create: async (input: CreateFeatureToggleInputs) => {
            try {
                await featureToggles_create(input);
                toast.success("Feature toggle created successfully");
            } catch (e) {
                console.error(e);
                if (e instanceof Error) {
                    toast.error(e.message);
                }
            }
            const response = await featureToggles_getAll();
            set(() => ({
                featureToggles: response
            }));
        }, update: async (input: UpdateFeatureToggleInputs) => {
            try {
                await featureToggles_update(input);
                toast.success("Feature toggle updated successfully");
            } catch (e) {
                console.error(e);
                if (e instanceof Error) {
                    toast.error(e.message);
                }
            }
            const response = await featureToggles_getAll();
            set(() => ({
                featureToggles: response
            }));
        }, deleteById: async (id: number) => {
            try {
                await featureToggles_deleteById(id);
                toast.success("Feature toggle deleted successfully");
            } catch (e) {
                console.error(e);
                if (e instanceof Error) {
                    toast.error(e.message);
                }
            }
            const response = await featureToggles_getAll();
            set(() => ({
                featureToggles: response
            }));
        }, setSelected: (value: FeatureToggle | undefined) => {
            set({
                selected: value
            });
        }, setIsLoading: (value: boolean) => {
            set({
                isLoading: value
            });
        }
    }), {
        name: 'feature-toggle-storage', partialize: (state) => ({
            environments: state.featureToggles
        }), onRehydrateStorage: (state) => {
            return () => state.setIsLoading(false);
        }
    }))
}
