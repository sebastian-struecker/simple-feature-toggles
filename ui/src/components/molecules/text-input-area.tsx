import React from "react";

type Inputs = {
    label: string; placeholder: string; control: Control; validation: Validation;
}

type Control = {
    key: string; register: any; isSubmitting: boolean;
}

type Validation = {
    minLength: number; isRequired: boolean; validatorHint: string;
}

export function TextInputArea({
                                  label = "", placeholder = "", control: {key, register, isSubmitting}, validation: {
        isRequired = false, minLength = 0, validatorHint = ""
    }
                              }: Inputs) {
    return (<>
        <fieldset className="fieldset">
            <legend className="fieldset-legend gap-0">{label}
                {isRequired && <div className="text-error">*</div>}
            </legend>
            <textarea
                {...register(key, {required: isRequired, minLength: minLength})}
                disabled={isSubmitting}
                className={`textarea textarea-primary textarea-md w-full validator`}
                placeholder={placeholder} minLength={minLength} required={isRequired}
                name={key}
            />
            <div className="validator-hint hidden">
                {validatorHint}
            </div>
        </fieldset>
    </>)

}
