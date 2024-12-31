'use server'

import fetcher from "@/src/utils/fetcher";
import {FeatureToggle} from "@/src/types/feature-toggle";
import {CreateFeatureToggleInputs} from "@/src/types/create-feature-toggle-inputs";
import {UpdateFeatureToggleInputs} from "@/src/types/update-feature-toggle-inputs";

const path = "/feature-toggles"

export async function featureToggles_getById(id: number): Promise<FeatureToggle> {
    const response = await fetcher(`${path}/` + id);
    return response.json();
}

export async function featureToggles_getAll(): Promise<FeatureToggle[]> {
    const response = await fetcher(`${path}`);
    return response.json();
}

export async function featureToggles_create(input: CreateFeatureToggleInputs): Promise<FeatureToggle> {
    const response = await fetcher(`${path}`, {
        method: "POST", body: JSON.stringify(input), headers: {"Content-Type": "application/json"}
    })
    return response.json();
}

export async function featureToggles_update(input: UpdateFeatureToggleInputs): Promise<FeatureToggle> {
    const response = await fetcher(`${path}`, {
        method: "PATCH", body: JSON.stringify(input), headers: {"Content-Type": "application/json"}
    })
    return response.json();
}

export async function featureToggles_deleteById(id: number) {
    await fetcher(`${path}/` + id, {
        method: "DELETE"
    });
}
