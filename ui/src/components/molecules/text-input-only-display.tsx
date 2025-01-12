import React, {useEffect} from "react";

export const UsedPatterns = {
    key: "(([a-z])+(_[a-z])*)+", default: ".*"
}

type Inputs = {
    label: string; control: Control; validation: Validation;
}

type Control = {
    key: string; setValue: any; disabled?: boolean; value?: string;
}

type Validation = {
    isRequired: boolean;
}

export function TextInputOnlyDisplay({
                                         label = "",
                                         control: {key, setValue, disabled = false, value = undefined},
                                         validation: {
                                             isRequired = false,
                                         },
                                     }: Inputs) {
    useEffect(() => {
        setValue(key, value);
    }, [value]);

    return (<>
        <fieldset className="fieldset">
            <legend className="fieldset-legend gap-0">{label}
                {isRequired && <div className="text-error">*</div>}
            </legend>
            <input
                disabled={disabled}
                name={key}
                type="text" className="input input-primary input-md w-full validator"
                placeholder={value}/>
        </fieldset>
    </>)

}
