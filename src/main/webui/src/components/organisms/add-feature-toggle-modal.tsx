import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {CreateFeatureToggleInputs} from "@/src/types/create-feature-toggle-inputs";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {EnvironmentInputField} from "@/src/components/molecules/environment-input-field";
import {TextInputArea} from "@/src/components/molecules/text-input-area";

type Inputs = {
    modalId: string;
}

export function AddFeatureToggleModal({modalId}: Inputs) {
    const {
        control, register, handleSubmit, reset, formState: {errors, isSubmitting},
    } = useForm<CreateFeatureToggleInputs>({defaultValues: {environmentActivation: new Map(), description: ""}});
    const {create} = useFeatureToggleStore((state) => state);

    const onSubmit: SubmitHandler<CreateFeatureToggleInputs> = async (values: CreateFeatureToggleInputs) => {
        create(values);
        reset();
        document.getElementById(modalId)?.close();
    };

    return (<dialog id={modalId} className="modal modal-bottom sm:modal-middle">
        <div className="modal-box">
            <form method="dialog" className="flex flex-col gap-3" onSubmit={handleSubmit(onSubmit)}>
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" type="reset" onClick={() => {
                    reset();
                    document.getElementById(modalId)?.close();
                }}>✕
                </button>
                <h3 className="text-center text-xl sm:text-xl font-semibold">
                    Create a Feature Toggle
                </h3>
                <div className="flex flex-col">
                    <TextInputField label={"Key"} placeholder={"Enter a key"}
                                    control={{key: "key", register: register, isSubmitting: isSubmitting}}
                                    validation={{
                                        validatorHint: "Enter a valid key",
                                        minLength: 3,
                                        pattern: UsedPatterns.key,
                                        isRequired: true
                                    }}
                    />
                    <TextInputField label={"Name"} placeholder={"Enter a name"}
                                    control={{key: "name", register: register, isSubmitting: isSubmitting}}
                                    validation={{
                                        validatorHint: "Enter a valid name",
                                        minLength: 1,
                                        pattern: UsedPatterns.default,
                                        isRequired: true
                                    }}
                    />
                    <TextInputArea label={"Description"} placeholder={"Enter a description"}
                                   control={{key: "description", register: register, isSubmitting: isSubmitting}}
                                   validation={{
                                       validatorHint: "Enter a description", minLength: 1, isRequired: false
                                   }}
                    />
                    <EnvironmentInputField label={"Environment"} control={control} isRequired={false}
                                           formKey={"environment"}
                                           register={register} error={errors.environmentActivation}
                                           isSubmitting={isSubmitting}/>
                </div>
                <div className="modal-action flex justify-center">
                    <button className="btn btn-active btn-primary btn-block max-w-[200px]" type="submit">
                        Create
                    </button>
                    <button className="btn btn-outline btn-primary btn-block max-w-[200px]" type="reset"
                            onClick={() => {
                                reset();
                                document.getElementById(modalId)?.close();
                            }}>
                        Cancel
                    </button>
                </div>
            </form>
        </div>
        <form method="dialog" className="modal-backdrop">
            <button onClick={() => {
                reset();
            }}>Close on background click
            </button>
        </form>
    </dialog>)

}
