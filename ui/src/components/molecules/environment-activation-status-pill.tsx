import {OverflowText} from "@/src/components/atoms/overflow-text";

type Inputs = {
    environmentKey: string; isActive: boolean;
}

export function EnvironmentActivationStatusPill({environmentKey, isActive}: Inputs) {
    return (<div
        className={`flex flex-row gap-2 rounded-box items-center p-2 border ${isActive ? "border-primary" : "border-base-300"}`}>
        <div aria-label={`${isActive}`}
             className={`status status-xl ${isActive ? "status-error" : "status-success"}`}></div>
        <OverflowText text={environmentKey} length={7}/>
    </div>)
}
