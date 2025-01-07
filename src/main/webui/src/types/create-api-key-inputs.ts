import {EnvironmentActivation} from "@/src/types/environment-activation";

export type CreateApiKeyInputs = {
    name: string; environmentActivations: EnvironmentActivation[];
};
