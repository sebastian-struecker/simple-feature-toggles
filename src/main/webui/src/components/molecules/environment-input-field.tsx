import React, {useEffect, useState} from "react";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {Environment} from "@/src/types/environment";

type Inputs = {
    setValue: any;
    label: string;
    isRequired: boolean;
    formKey: string;
    control: any,
    register: any;
    error: any;
    isSubmitting: boolean;
}

type EnvironmentActivation = {
    environment: Environment; isSelected: boolean; isActive: boolean
}

export function EnvironmentInputField({
                                          setValue, label, isRequired, formKey, control, register, error, isSubmitting
                                      }: Inputs) {
    const {environments, getAll} = useEnvironmentStore((state) => state);
    const [environmentActivations, setEnvironmentActivations] = useState<EnvironmentActivation[]>([]);

    useEffect(() => {
        async function awaitGetAll() {
            const res = await getAll();
            const list: EnvironmentActivation[] = [];
            res.map(environment => {
                list.push({environment: environment, isSelected: false, isActive: false});
            });
            setEnvironmentActivations(list);
        }

        awaitGetAll();
    }, [getAll])

    const onEnvironmentActivationChange = (updated: EnvironmentActivation) => {
        setEnvironmentActivations(prev => {
            const prevFiltered = [...prev].filter(value => value.environment != updated.environment);
            const nextValue = [...prevFiltered, updated];
            setFormValue(nextValue);
            return nextValue;
        });
    }

    const setFormValue = (value: EnvironmentActivation[]) => {
        const map = new Map<string, boolean>;
        value.forEach((environmentActivation) => {
            if (environmentActivation.isSelected) {
                map.set(environmentActivation.environment.key, environmentActivation.isActive);
            }
        })
        setValue("environmentActivation", map);
    }

    return (<>
        <fieldset className="fieldset">
            <legend className="fieldset-legend">Environments
                ({environmentActivations.filter(value => value.isSelected).length})
            </legend>
            <div className="grid grid-cols-2 gap-4">
                {environmentActivations.map(((environmentActivation, index) => {
                    const {environment, isSelected, isActive} = environmentActivation;
                    return (<div key={environment.id + environment.key + index}
                                 className={`flex flex-row justify-between gap-2 rounded-box p-2 border ${isSelected ? `bg-primary border-primary` : "border-base-200 hover:bg-base-200"}`}>
                        <div className="flex items-center">
                            <input type="checkbox" className={`checkbox ${isSelected ? "checkbox-secondary" : ""}`}
                                   checked={isSelected}
                                   onChange={(e) => {
                                       onEnvironmentActivationChange({
                                           ...environmentActivation, isSelected: e.target.checked
                                       })
                                   }}/>
                        </div>
                        <div className="flex flex-col justify-center flex-grow">
                            <div>{environment.name}</div>
                            <div className="text-xs font-semibold opacity-60">{environment.key}</div>
                        </div>
                        <div className="flex flex-col items-center">
                            <input type="checkbox" className="toggle toggle-secondary"
                                   checked={isActive}
                                   onChange={(e) => {
                                       onEnvironmentActivationChange({
                                           ...environmentActivation, isActive: e.target.checked
                                       })
                                   }}/>
                            {isActive && <div
                                className="text-xs font-semibold lowercase text-secondary">active</div>}
                            {!isActive && <div
                                className="text-xs font-semibold opacity-60 lowercase">inactive</div>}
                        </div>
                    </div>);
                }))}
            </div>
        </fieldset>
    </>)

}
