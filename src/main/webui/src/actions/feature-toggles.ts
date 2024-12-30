'use server'

import fetcher from "@/src/utils/fetcher";
import {FeatureToggle} from "@/src/types/feature-toggle";
import {CreateFeatureToggleInputs} from "@/src/types/create-feature-toggle-inputs";


export async function featureToggles_getById(id: number): Promise<FeatureToggle> {
    const response = await fetcher("/feature-toggles/" + id);
    return response.json();
}

export async function featureToggles_getAll(): Promise<FeatureToggle[]> {
    const response = await fetcher("/feature-toggles");
    return response.json();
}

export async function featureToggles_create(input: CreateFeatureToggleInputs): Promise<FeatureToggle> {
    const response = await fetcher("/feature-toggles", {
        method: "POST", body: JSON.stringify(input), headers: {"Content-Type": "application/json"}
    })
    return response.json();
}

export async function featureToggles_deleteById(id: number) {
    await fetcher("/feature-toggles/" + id, {
        method: "DELETE"
    });
}
