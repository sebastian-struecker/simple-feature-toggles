import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {CreateEnvironmentInputs} from "@/src/types/create-environment-inputs";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {ModalWithBottomActionsWrapper} from "@/src/components/molecules/modal-with-bottom-actions-wrapper";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function AddEnvironmentModal({visible, onClose}: Inputs) {
    const {
        register, handleSubmit, setValue, reset, formState: {isSubmitting},
    } = useForm<CreateEnvironmentInputs>();
    const {create} = useEnvironmentStore((state) => state);

    const onSubmit: SubmitHandler<CreateEnvironmentInputs> = async (values: CreateEnvironmentInputs) => {
        create(values);
        handleClose();
    };

    const handleClose = () => {
        reset();
        onClose();
    };

    return (<ModalWithBottomActionsWrapper labels={{title: "Create an Environment", actionButtonLabel: "Create"}}
                                           controls={{
                                               onSubmit: handleSubmit(onSubmit),
                                               onClose: handleClose,
                                               visible: visible
                                           }}>
        <TextInputField label={"Key"} placeholder={"Enter a key"}
                        control={{
                            key: "key", register: register, setValue: setValue, submitting: isSubmitting
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
                            key: "name", register: register, setValue: setValue, submitting: isSubmitting
                        }}
                        validation={{
                            validatorHint: "Enter a valid name",
                            minLength: 1,
                            pattern: UsedPatterns.default,
                            isRequired: true
                        }}
        />
    </ModalWithBottomActionsWrapper>)

}
