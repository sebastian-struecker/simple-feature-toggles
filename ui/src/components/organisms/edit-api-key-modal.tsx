import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {ModalWithBottomActionsWrapper} from "@/src/components/molecules/modal-with-bottom-actions-wrapper";
import {UpdateApiKeyInputs} from "@/src/types/update-api-key-inputs";
import {useApiKeyStore} from "@/src/providers/api-key-store-provider";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {EnvironmentInputField} from "@/src/components/molecules/environment-input-field";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function EditApiKeyModal({visible, onClose}: Inputs) {
    const {
        register, control, handleSubmit, setValue, reset, formState: {isSubmitting},
    } = useForm<UpdateApiKeyInputs>();
    const {selected, update} = useApiKeyStore((state) => state);

    const onSubmit: SubmitHandler<UpdateApiKeyInputs> = async (values: UpdateApiKeyInputs) => {
        if (selected) {
            update(selected.id, values);
        }
        handleClose();
    };

    const handleClose = () => {
        reset();
        onClose();
    };

    return (<ModalWithBottomActionsWrapper labels={{title: "Edit Api Key:", actionButtonLabel: "Save"}}
                                           controls={{
                                               onSubmit: handleSubmit(onSubmit), onClose: handleClose, visible: visible
                                           }}>
        <TextInputField label={"Name"} placeholder={"Enter a name"}
                        control={{
                            key: "name", register, setValue: setValue, submitting: isSubmitting, value: selected?.name
                        }}
                        validation={{
                            validatorHint: "Some new api key",
                            minLength: 1,
                            pattern: UsedPatterns.default,
                            isRequired: true
                        }}
        />
        <EnvironmentInputField setValue={setValue} control={control} isSubmitting={isSubmitting}
                               value={selected?.environmentActivations}/>
    </ModalWithBottomActionsWrapper>)

}
