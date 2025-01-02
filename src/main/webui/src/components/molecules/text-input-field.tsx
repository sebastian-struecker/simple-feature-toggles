import React from "react";

export const UsedPatterns = {
    key: /^[a-z_]*[a-z]$/, default: /^.*$/
}

type Inputs = {
    label: string;
    placeholder: string;
    example: string;
    isRequired: boolean;
    pattern: RegExp;
    formKey: string;
    register: any;
    error: any;
    isSubmitting: boolean;
}

export function TextInputField({
                                   label,
                                   placeholder,
                                   example,
                                   isRequired,
                                   pattern = UsedPatterns.default,
                                   formKey,
                                   register,
                                   error,
                                   isSubmitting
                               }: Inputs) {
    return (<label className="form-control">
        <div className="label">
                            <span className="label-text">
                                {label}
                                {isRequired && <span className="text-error">*</span>}
                            </span>
        </div>
        <input
            {...register(formKey, {required: isRequired, pattern: pattern})} disabled={isSubmitting}
            type="text"
            name={formKey}
            placeholder={placeholder}
            className={`input input-bordered input-primary input-md w-full ${error ? "input-error" : ""}`}
        />
        <div className="label">
            <span className="label-text-alt"></span>
            {error && (<span
                className="label-text-alt text-error">Please enter a valid {label.toLowerCase()} like: {example}</span>)}
        </div>
    </label>)

}
