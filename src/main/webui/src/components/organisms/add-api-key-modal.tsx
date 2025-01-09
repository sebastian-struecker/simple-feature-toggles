import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useApiKeyStore} from "@/src/providers/api-key-store-provider";
import {CreateApiKeyInputs} from "@/src/types/create-api-key-inputs";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {EnvironmentInputField} from "@/src/components/molecules/environment-input-field";
import {ModalWithBottomActionsWrapper} from "@/src/components/molecules/modal-with-bottom-actions-wrapper";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function AddApiKeyModal({visible, onClose}: Inputs) {
    const {
        register, control, handleSubmit, reset, setValue, formState: {isSubmitting},
    } = useForm<CreateApiKeyInputs>({defaultValues: {environmentActivations: []}});
    const {create} = useApiKeyStore((state) => state);

    const onSubmit: SubmitHandler<CreateApiKeyInputs> = async (values: CreateApiKeyInputs) => {
        create(values);
        reset();
        onClose();
    };

    const handleClose = () => {
        reset();
        onClose();
    };

    return (<ModalWithBottomActionsWrapper labels={{title: "Create an Api Key", actionButtonLabel: "Create"}}
                                           controls={{
                                               onSubmit: handleSubmit(onSubmit),
                                               onClose: handleClose,
                                               visible: visible
                                           }}>
        <TextInputField label={"Name"} placeholder={"Enter a name"}
                        control={{key: "name", register, setValue: setValue, submitting: isSubmitting}}
                        validation={{
                            validatorHint: "Some new api key",
                            minLength: 1,
                            pattern: UsedPatterns.default,
                            isRequired: true
                        }}
        />
        <EnvironmentInputField setValue={setValue} control={control} isSubmitting={isSubmitting}/>
    </ModalWithBottomActionsWrapper>)

}
