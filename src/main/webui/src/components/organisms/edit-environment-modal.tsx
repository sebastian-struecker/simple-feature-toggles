import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {UpdateEnvironmentInputs} from "@/src/types/update-environment-inputs";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {ModalWithBottomActionsWrapper} from "@/src/components/molecules/modal-with-bottom-actions-wrapper";
import {TextInputOnlyDisplay} from "@/src/components/molecules/text-input-only-display";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function EditEnvironmentModal({visible, onClose}: Inputs) {
    const {
        register, handleSubmit, setValue, reset, formState: {isSubmitting},
    } = useForm<UpdateEnvironmentInputs>();
    const {selected, update} = useEnvironmentStore((state) => state);

    const onSubmit: SubmitHandler<UpdateEnvironmentInputs> = async (values: UpdateEnvironmentInputs) => {
        if (selected) {
            update(selected.id, values);
        }
        handleClose();
    };

    const handleClose = () => {
        reset();
        setValue("name", selected?.name);
        onClose();
    };

    return (<ModalWithBottomActionsWrapper
        labels={{title: "Edit Environment: " + selected?.name, actionButtonLabel: "Save"}}
        controls={{onClose: handleClose, onSubmit: handleSubmit(onSubmit), visible: visible}}>
        <TextInputOnlyDisplay label={"Key"}
                              control={{
                                  key: "key", setValue: setValue, disabled: true, value: selected?.key
                              }}
                              validation={{
                                  isRequired: true
                              }}
        />
        <TextInputField label={"Name"} placeholder={"Enter a name"}
                        control={{
                            key: "name",
                            register: register,
                            setValue: setValue,
                            submitting: isSubmitting,
                            value: selected?.name
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
