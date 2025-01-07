import React, {useEffect, useState} from "react";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {Environment} from "@/src/types/environment";
import {EnvironmentActivation} from "@/src/types/environment-activation";
import {OverflowText} from "@/src/components/atoms/overflow-text";

type Inputs = {
    setValue: any; isSubmitting: boolean; isSubmitSuccessful: boolean;
}

type EnvironmentField = {
    environment: Environment; isSelected: boolean; isActive: boolean
}

export function EnvironmentInputField({
                                          setValue, isSubmitting, isSubmitSuccessful
                                      }: Inputs) {
    const {getAll} = useEnvironmentStore((state) => state);
    const [environmentActivations, setEnvironmentActivations] = useState<EnvironmentField[]>([]);

    useEffect(() => {
        async function awaitGetAll() {
            const environments = await getAll();
            mapEnvironmentActivations(environments);
        }

        awaitGetAll();
    }, [getAll])

    useEffect(() => {
        async function awaitGetAll() {
            const environments = await getAll();
            mapEnvironmentActivations(environments);
        }

        if (isSubmitSuccessful) {
            awaitGetAll();
        }
    }, [isSubmitSuccessful])

    const mapEnvironmentActivations = (environments: Environment[]) => {
        const list: EnvironmentField[] = [];
        environments.map(environment => {
            list.push({environment: environment, isSelected: false, isActive: false});
        });
        list.sort(environmentActivationSort);
        setEnvironmentActivations(list);
    }

    const onEnvironmentActivationChange = (updated: EnvironmentField) => {
        setEnvironmentActivations(prev => {
            const prevFiltered = [...prev].filter(value => value.environment != updated.environment);
            const nextValue = [updated, ...prevFiltered];
            nextValue.sort(environmentActivationSort);
            setFormValue(nextValue);
            return nextValue;
        });
    }

    const environmentActivationSort = (a: EnvironmentField, b: EnvironmentField) => {
        if (a.isSelected && !b.isSelected) {
            return -1;
        } else if (!a.isSelected && b.isSelected) {
            return 1;
        }
        return a.environment.key > b.environment.key ? 1 : -1;
    }

    const setFormValue = (values: EnvironmentField[]) => {
        const environmentActivations: EnvironmentActivation[] = [];
        values.forEach((value) => {
            if (value.isSelected) {
                environmentActivations.push({
                    environmentKey: value.environment.key, isActive: value.isActive
                } as EnvironmentActivation)
            }
        })
        setValue("environmentActivations", environmentActivations);
    }

    return (<>
        <fieldset className="fieldset max-h-[12rem] overflow-y-scroll">
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
                                   checked={isSelected} disabled={isSubmitting}
                                   onChange={(e) => {
                                       onEnvironmentActivationChange({
                                           ...environmentActivation, isSelected: e.target.checked
                                       })
                                   }}/>
                        </div>
                        <div className="flex flex-col justify-center flex-grow">
                            <OverflowText text={environment.name} length={15}/>
                            <div className="text-xs font-semibold opacity-60">
                                <OverflowText text={environment.key} length={15}/>
                            </div>
                        </div>
                        <div className="flex flex-col items-center">
                            <input type="checkbox" className="toggle toggle-secondary"
                                   checked={isActive} disabled={isSubmitting}
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
