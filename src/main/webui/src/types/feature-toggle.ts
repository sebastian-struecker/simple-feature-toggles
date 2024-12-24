import { Context } from "./context";

export type FeatureToggle = {
    id: number; key: string; name: string; description: string; contexts: Context[];
}
