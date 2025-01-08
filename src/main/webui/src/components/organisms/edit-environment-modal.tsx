import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {UpdateEnvironmentInputs} from "@/src/types/update-environment-inputs";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {ModalWrapper} from "@/src/components/atoms/modal-wrapper";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function EditEnvironmentModal({visible, onClose}: Inputs) {
    const {
        register, handleSubmit, reset, formState: {isSubmitting},
    } = useForm<UpdateEnvironmentInputs>();
    const {selected, update} = useEnvironmentStore((state) => state);

    const onSubmit: SubmitHandler<UpdateEnvironmentInputs> = async (values: UpdateEnvironmentInputs) => {
        update(values);
        handleClose();
    };

    const handleClose = () => {
        reset();
        onClose();
    };

    return (<ModalWrapper labels={{title: "Create an Environment"}}
                          controls={{onClose: handleClose, visible: visible}}>
        <TextInputField label={"Key"} placeholder={"Enter a key"}
                        control={{
                            key: "key", register: register, isSubmitting: isSubmitting
                        }}
                        validation={{
                            validatorHint: "Enter a valid key",
                            minLength: 3,
                            pattern: UsedPatterns.key,
                            isRequired: true
                        }}
        />
        <TextInputField label={"Name"} placeholder={"Enter a name"}
                        control={{
                            key: "name", register: register, isSubmitting: isSubmitting
                        }}
                        validation={{
                            validatorHint: "Enter a valid name",
                            minLength: 1,
                            pattern: UsedPatterns.default,
                            isRequired: true
                        }}
        />
    </ModalWrapper>)
}
