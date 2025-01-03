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
                    Create an Environment
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
