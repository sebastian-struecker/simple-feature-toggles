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

    return (<>
        <fieldset className="fieldset">
            <legend className="fieldset-legend">{label}
            </legend>


            <Controller
                control={control}
                name="multipleSelect"
                render={({field}) => {
                    return (<>
                        {environments.map((env) => (<>
                            <label className="fieldset-label font-black text-black font-normal">
                                <input type="checkbox" defaultChecked className="checkbox"/>
                                {env.name}
                            </label>
                        </>))}
                    </>);
                }}
            />
        </fieldset>
    </>)

}
