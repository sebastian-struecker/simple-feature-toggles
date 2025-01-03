import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useApiKeyStore} from "@/src/providers/api-key-store-provider";
import {CreateApiKeyInputs} from "@/src/types/create-api-key-inputs";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {EnvironmentInputField} from "@/src/components/molecules/environment-input-field";

type Inputs = {
    modalId: string;
}

export function AddApiKeyModal({modalId}: Inputs) {
    const {
        control, register, handleSubmit, reset, formState: {errors, isSubmitting},
    } = useForm<CreateApiKeyInputs>({defaultValues: {environmentActivation: new Map()}});
    const {create} = useApiKeyStore((state) => state);

    const onSubmit: SubmitHandler<CreateApiKeyInputs> = async (values: CreateApiKeyInputs) => {
        create(values);
        onClose();
    };

    const onClose = () => {
        reset();
        document.getElementById(modalId)?.close();
    };

    return (<dialog id={modalId} className="modal modal-bottom sm:modal-middle">
        <div className="modal-box">
            <form method="dialog" className="flex flex-col gap-3" onSubmit={handleSubmit(onSubmit)}>
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" type="reset"
                        onClick={onClose}>✕
                </button>
                <h3 className="text-center text-xl sm:text-xl font-semibold">
                    Create an Api Key
                </h3>
                <div className="flex flex-col">
                    <TextInputField label={"Name"} placeholder={"Enter a name"}
                                    control={{key: "name", register, isSubmitting}}
                                    validation={{
                                        validatorHint: "Some new api key",
                                        minLength: 1,
                                        pattern: UsedPatterns.default,
                                        isRequired: true
                                    }}
                    />
                    <EnvironmentInputField label={"Environment"} control={control} isRequired={false}
                                           formKey={"environment"}
                                           register={register} error={errors.environmentActivation}
                                           isSubmitting={isSubmitting}/>
                </div>
                <div className="modal-action flex justify-center">
                    <button className="btn btn-primary btn-block max-w-[12rem]" type="submit">
                        Create
                    </button>
                    <button className="btn btn-outline btn-primary btn-block max-w-[12rem]" type="reset"
                            onClick={onClose}>
                        Cancel
                    </button>
                </div>
            </form>
        </div>
        <form method="dialog" className="modal-backdrop">
            <button onClick={onClose}>Close on background click
            </button>
        </form>
    </dialog>)

}
