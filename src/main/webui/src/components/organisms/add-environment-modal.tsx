import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {CreateEnvironmentInputs} from "@/src/types/create-environment-inputs";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";

type Inputs = {
    modalId: string;
}

export function AddEnvironmentModal({modalId}: Inputs) {
    const {
        register, handleSubmit, reset, formState: {errors, isSubmitting},
    } = useForm<CreateEnvironmentInputs>();
    const {create} = useEnvironmentStore((state) => state);

    const onSubmit: SubmitHandler<CreateEnvironmentInputs> = async (values: CreateEnvironmentInputs) => {
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
                    Create an Environment
                </h3>
                <div className="flex flex-col">
                    <TextInputField label={"Key"} placeholder={"Enter a key"} example={"dev_stage"} isRequired={true}
                                    pattern={UsedPatterns.key} formKey={"key"} register={register}
                                    error={errors.key} isSubmitting={isSubmitting}/>
                    <TextInputField label={"Name"} placeholder={"Enter a name"} example={"Development Stage"}
                                    isRequired={true}
                                    pattern={UsedPatterns.default} formKey={"name"} register={register}
                                    error={errors.name} isSubmitting={isSubmitting}/>
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
