'use server'

import fetcher from "@/src/utils/fetcher";
import {ApiKey} from "@/src/types/api-key";
import {CreateApiKeyInputs} from "@/src/types/create-api-key-inputs";
import {UpdateApiKeyInputs} from "@/src/types/update-api-key-inputs";


const path = "/api-keys";

export async function apiKeys_getById(id: number): Promise<ApiKey> {
    const response = await fetcher(`${path}/` + id);
    return response.json();
}

export async function apiKeys_getAll(): Promise<ApiKey[]> {
    const response = await fetcher(`${path}`);
    return response.json();
}

export async function apiKeys_create(input: CreateApiKeyInputs): Promise<ApiKey> {
    console.log(input);
    console.log(JSON.stringify(input));
    const response = await fetcher(`${path}`, {
        method: "POST", body: JSON.stringify(input), headers: {"Content-Type": "application/json"}
    })
    if (response && response.status === 200) {
        return response.json();
    }
    throw new Error("Error while creating an api key");
}

export async function apiKeys_update(id: number, input: UpdateApiKeyInputs): Promise<ApiKey> {
    const response = await fetcher(`${path}/` + id, {
        method: "PATCH", body: JSON.stringify(input), headers: {"Content-Type": "application/json"}
    })
    if (response && response.status === 200) {
        return response.json();
    }
    throw new Error("Error while updating an api key");
}

export async function apiKeys_deleteById(id: number) {
    const response = await fetcher(`${path}/` + id, {
        method: "DELETE"
    });
    if (response && response.status === 204) {
        return;
    }
    throw new Error("Error while deleting an api key");
}
