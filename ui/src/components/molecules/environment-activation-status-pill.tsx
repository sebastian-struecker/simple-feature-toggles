import {OverflowText} from "@/src/components/atoms/overflow-text";
import React from "react";

type Inputs = {
    environmentKey: string; activated: boolean;
}

export function EnvironmentActivationStatusPill({environmentKey, activated}: Inputs) {
    return (<div
        className={`flex flex-row gap-2 rounded-box items-center p-2 border ${activated ? "border-primary" : "border-base-300"}`}>
        <div className="lg:tooltip" data-tip={`${activated ? "Active" : "Not active"}`}>
            <div aria-label={`${activated}`}
                 className={`status status-xl ${activated ? "status-success" : "status-error"}`}></div>
        </div>
        <OverflowText text={environmentKey} length={7}/>
    </div>)
}
