import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {CreateFeatureToggleInputs} from "@/src/types/create-feature-toggle-inputs";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {EnvironmentInputField} from "@/src/components/molecules/environment-input-field";

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
                    <TextInputField label={"Key"} placeholder={"Enter a key"} example={"new_feature"} isRequired={true}
                                    pattern={UsedPatterns.key} formKey={"key"} register={register}
                                    error={errors.key} isSubmitting={isSubmitting}/>
                    <TextInputField label={"Name"} placeholder={"Enter a name"} example={"Some new feature"}
                                    isRequired={true}
                                    pattern={UsedPatterns.default} formKey={"name"} register={register}
                                    error={errors.name} isSubmitting={isSubmitting}/>
                    <label className="form-control">
                        <div className="label">
                            <span className="label-text">
                                Description
                            </span>
                        </div>
                        <textarea
                            {...register("description", {minLength: 1})} disabled={isSubmitting}
                            className={`textarea textarea-primary textarea-bordered textarea-md w-full ${errors.description ? "input-error" : ""}`}
                            placeholder="Enter a description"
                            name="description"
                        />
                        <div className="label">
                            <span className="label-text-alt"></span>
                            {errors.description?.type == "minLength" && (
                                <span className="label-text-alt text-error">Please enter a description</span>)}
                        </div>
                    </label>
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
