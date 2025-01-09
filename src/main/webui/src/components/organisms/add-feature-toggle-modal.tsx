import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {CreateFeatureToggleInputs} from "@/src/types/create-feature-toggle-inputs";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {EnvironmentInputField} from "@/src/components/molecules/environment-input-field";
import {TextInputArea} from "@/src/components/molecules/text-input-area";
import {ModalWithBottomActionsWrapper} from "@/src/components/molecules/modal-with-bottom-actions-wrapper";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function AddFeatureToggleModal({visible, onClose}: Inputs) {
    const {
        register, control, handleSubmit, reset, setValue, formState: {isSubmitting},
    } = useForm<CreateFeatureToggleInputs>({defaultValues: {environmentActivations: [], description: ""}});
    const {create} = useFeatureToggleStore((state) => state);

    const onSubmit: SubmitHandler<CreateFeatureToggleInputs> = async (values: CreateFeatureToggleInputs) => {
        create(values);
        reset();
        onClose();
    };

    const handleClose = () => {
        reset();
        onClose();
    };

    return (<ModalWithBottomActionsWrapper labels={{title: "Create a Feature Toggle", actionButtonLabel: "Create"}}
                                           controls={{
                                               onSubmit: handleSubmit(onSubmit),
                                               onClose: handleClose,
                                               visible: visible
                                           }}>
        <TextInputField label={"Key"} placeholder={"Enter a key"}
                        control={{key: "key", register: register, setValue: setValue, submitting: isSubmitting}}
                        validation={{
                            validatorHint: "Enter a valid key",
                            minLength: 3,
                            pattern: UsedPatterns.key,
                            isRequired: true
                        }}
        />
        <TextInputField label={"Name"} placeholder={"Enter a name"}
                        control={{key: "name", register: register, setValue: setValue, submitting: isSubmitting}}
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
        <EnvironmentInputField setValue={setValue} control={control} isSubmitting={isSubmitting}/>
    </ModalWithBottomActionsWrapper>)

}
