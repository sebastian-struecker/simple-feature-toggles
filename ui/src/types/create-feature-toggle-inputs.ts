import {EnvironmentActivation} from "@/src/types/environment-activation";

export type CreateFeatureToggleInputs = {
    key: string; name: string; description: string; environmentActivations: EnvironmentActivation[];
};
