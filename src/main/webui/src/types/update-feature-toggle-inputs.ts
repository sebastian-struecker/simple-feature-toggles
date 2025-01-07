import {EnvironmentActivation} from "@/src/types/environment-activation";

export type UpdateFeatureToggleInputs = {
    name?: string; description?: string; environmentActivations?: EnvironmentActivation[];
};
