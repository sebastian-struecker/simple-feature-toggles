import {EnvironmentActivation} from "@/src/types/environment-activation";

export type ApiKey = {
    id: number; name: string; secret: string; environmentActivations: EnvironmentActivation[];
}
