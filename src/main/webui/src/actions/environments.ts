'use server'

import fetcher from "@/src/utils/fetcher";
import {Environment} from "@/src/types/environment";
import {CreateEnvironmentInputs} from "@/src/types/create-environment-inputs";
import {UpdateEnvironmentInputs} from "@/src/types/update-environment-inputs";

const path = "/environments";

export async function environments_getById(id: number): Promise<Environment> {
    const response = await fetcher(`${path}/` + id);
    return response?.json();
}

export async function environments_getAll(): Promise<Environment[]> {
    const response = await fetcher(`${path}`);
    return response?.json();
}

export async function environments_create(input: CreateEnvironmentInputs): Promise<Environment> {
    const response = await fetcher(`${path}`, {
        method: "POST", body: JSON.stringify(input), headers: {"Content-Type": "application/json"}
    });
    if (response && response.status === 200) {
        return response.json();
    }
    throw new Error("Error while creating an environment");
}

export async function environments_update(input: UpdateEnvironmentInputs): Promise<Environment> {
    const response = await fetcher(`${path}`, {
        method: "PATCH", body: JSON.stringify(input), headers: {"Content-Type": "application/json"}
    });
    if (response && response.status === 200) {
        return response.json();
    }
    throw new Error("Error while updating an environment");
}

export async function environments_deleteById(id: number) {
    const response = await fetcher(`${path}/` + id, {
        method: "DELETE"
    });
    if (response && response.status === 204) {
        return response.json();
    }
    throw new Error("Error while deleting an environment");
}
