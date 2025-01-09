import React, {useEffect} from "react";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {Environment} from "@/src/types/environment";
import {EnvironmentActivation} from "@/src/types/environment-activation";
import {OverflowText} from "@/src/components/atoms/overflow-text";
import {Controller} from "react-hook-form";

type Inputs = {
    setValue: any; control: any; isSubmitting: boolean; value?: EnvironmentActivation[];
}

export function EnvironmentInputField({
                                          setValue, control, isSubmitting, value = undefined
                                      }: Inputs) {
    const {environments, getAll} = useEnvironmentStore((state) => state);

    useEffect(() => {
        async function awaitGetAll() {
            await getAll();
        }

        awaitGetAll();
    }, [getAll])

    useEffect(() => {
        setValue("environmentActivations", value);
    }, [value]);

    const isSelected = (value: EnvironmentActivation[], environment: Environment): boolean => {
        return value.filter(el => el.environmentKey == environment.key).length > 0;
    }

    const isActive = (value: EnvironmentActivation[], environment: Environment): boolean => {
        return value.filter(el => el.environmentKey == environment.key)
            .filter(el => el.isActive).length > 0;
    }

    return (<Controller
        control={control}
        name="environmentActivations"
        render={({field: {onChange, value}}) => (<>
            <fieldset className="fieldset max-h-[12rem] overflow-y-scroll">
                <legend className="fieldset-legend">Environments
                    ({value?.length})
                </legend>
                <div className="grid grid-cols-2 gap-4">
                    {environments.map(((environment, index) => {
                        return (<div key={environment.id + environment.key + index}
                                     className={`flex flex-row justify-between gap-2 rounded-box p-2 border ${isSelected(value || [], environment) ? `bg-primary border-secondary` : "border-base-200 hover:bg-base-200"}`}>
                            <div className="flex items-center">
                                <input type="checkbox"
                                       className={`checkbox ${value ? "checkbox-secondary" : ""}`}
                                       checked={isSelected(value || [], environment)} disabled={isSubmitting}
                                       onChange={(e) => {
                                           let activations: EnvironmentActivation[] = value
                                           if (e.target.checked) {
                                               activations.push({
                                                   environmentKey: environment.key, isActive: false
                                               });
                                           } else {
                                               activations = activations.filter((el) => el.environmentKey != environment.key)
                                           }
                                           onChange(activations);
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
                                       checked={isActive(value || [], environment)}
                                       disabled={!isSelected(value || [], environment) || isSubmitting}
                                       onChange={(e) => {
                                           const index = value.findIndex((obj: EnvironmentActivation) => obj.environmentKey == environment.key);
                                           value[index].isActive = e.target.checked
                                           onChange(value);
                                       }}/>
                                {isActive(value || [], environment) && <div
                                    className="text-xs font-semibold lowercase text-secondary">active</div>}
                                {!isActive(value || [], environment) && <div
                                    className="text-xs font-semibold opacity-60 lowercase">inactive</div>}
                            </div>
                        </div>);
                    }))}
                </div>
            </fieldset>
        </>)}
    />)

}
