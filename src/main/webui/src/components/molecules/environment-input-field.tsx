import React, {useEffect} from "react";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {Controller} from "react-hook-form";

type Inputs = {
    label: string; isRequired: boolean; formKey: string; control: any, register: any; error: any; isSubmitting: boolean;
}

export function EnvironmentInputField({
                                          label, isRequired, formKey, control, register, error, isSubmitting
                                      }: Inputs) {
    const {environments, getAll} = useEnvironmentStore((state) => state);

    useEffect(() => {
        async function awaitGetAll() {
            await getAll();
        }

        awaitGetAll();
    }, [getAll])

    return (<label className="form-control">
        <div className="label">
                            <span className="label-text">
                                {label}
                                {isRequired && <span className="text-error">*</span>}
                            </span>
        </div>
        <Controller
            control={control}
            name="multipleSelect"
            render={({field}) => {
                return (<>
                    {environments.map((env) => (<>{env.name}</>))}
                </>);
            }}
        />
        <div className="label">
            <span className="label-text-alt"></span>
            {error && (<span
                className="label-text-alt text-error">Please enter a valid {label.toLowerCase()}</span>)}
        </div>
    </label>)

}
