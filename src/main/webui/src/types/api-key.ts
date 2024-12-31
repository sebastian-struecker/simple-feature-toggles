export type ApiKey = {
    id: number; name: string; secret: string; environmentActivation: Map<string, boolean>;
}
