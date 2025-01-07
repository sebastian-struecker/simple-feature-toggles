import {OverflowText} from "@/src/components/atoms/overflow-text";

type Inputs = {
    environmentKey: string; isActive: boolean;
}

export function EnvironmentActivationStatusPill({environmentKey, isActive}: Inputs) {
    return (<div
        className={`flex flex-row max-w-[7rem] gap-2 rounded-box items-center p-2 border ${isActive ? "border-primary" : "border-base-300"}`}>
        <div aria-label={`${isActive}`}
             className={`status status-xl ${isActive ? "status-success" : "status-error"}`}></div>
        <OverflowText text={environmentKey} length={6}/>
    </div>)
}
