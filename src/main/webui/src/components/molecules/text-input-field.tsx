import React from "react";

export const UsedPatterns = {
    key: "(([a-z])+(_[a-z])*)+", default: ".*"
}

type Inputs = {
    label: string; placeholder: string; control: Control; validation: Validation;
}

type Control = {
    key: string; register: any; isSubmitting: boolean;
}

type Validation = {
    minLength: number; pattern?: string; isRequired: boolean; validatorHint: string;
}

export function TextInputField({
                                   label = "", placeholder = "", control: {key, register, isSubmitting}, validation: {
        pattern = UsedPatterns.default, isRequired = false, minLength = 0, validatorHint = ""
    }
                               }: Inputs) {
    return (<>
        <fieldset className="fieldset">
            <legend className="fieldset-legend gap-0">{label}
                {isRequired && <div className="text-error">*</div>}
            </legend>
            <input
                {...register(key, {required: isRequired, pattern: pattern, minLength: minLength})}
                disabled={isSubmitting}
                name={key}
                type="text" className="input input-primary input-md w-full validator" required={isRequired}
                placeholder={placeholder} minLength={minLength}
                pattern={pattern}/>
            <div className="validator-hint hidden">
                {validatorHint}
            </div>
        </fieldset>
    </>)

}
