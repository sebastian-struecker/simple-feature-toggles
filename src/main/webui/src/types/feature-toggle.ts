import {EnvironmentActivation} from "@/src/types/environment-activation";

export type FeatureToggle = {
    id: number; key: string; name: string; description: string; environmentActivations: EnvironmentActivation[];
}
