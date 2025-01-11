import React, {useEffect} from "react";

export const UsedPatterns = {
    key: "(([a-z])+(_[a-z])*)+", default: ".*"
}

type Inputs = {
    label: string; placeholder: string; control: Control; validation: Validation;
}

type Control = {
    key: string; register: any; setValue: any; submitting: boolean; disabled?: boolean; value?: string;
}

type Validation = {
    minLength: number; pattern?: string; isRequired: boolean; validatorHint: string;
}

export function TextInputField({
                                   label = "",
                                   placeholder = "",
                                   control: {key, register, setValue, submitting, disabled = false, value = undefined},
                                   validation: {
                                       pattern = UsedPatterns.default,
                                       isRequired = false,
                                       minLength = 0,
                                       validatorHint = ""
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
                {...register(key, {required: isRequired, pattern: pattern, minLength: minLength})}
                disabled={submitting || disabled}
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
