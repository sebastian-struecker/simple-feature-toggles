export type CreateFeatureToggleInputs = {
    key: string; name: string; description: string; environmentActivation: Map<string, boolean>;
};
