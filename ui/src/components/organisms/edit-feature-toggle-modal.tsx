import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {ModalWithBottomActionsWrapper} from "@/src/components/molecules/modal-with-bottom-actions-wrapper";
import {UpdateFeatureToggleInputs} from "@/src/types/update-feature-toggle-inputs";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {EnvironmentInputField} from "@/src/components/molecules/environment-input-field";
import {TextInputArea} from "@/src/components/molecules/text-input-area";
import {TextInputOnlyDisplay} from "@/src/components/molecules/text-input-only-display";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function EditFeatureToggleModal({visible, onClose}: Inputs) {
    const {
        register, control, handleSubmit, setValue, reset, formState: {isSubmitting},
    } = useForm<UpdateFeatureToggleInputs>();
    const {selected, update} = useFeatureToggleStore((state) => state);

    const onSubmit: SubmitHandler<UpdateFeatureToggleInputs> = async (values: UpdateFeatureToggleInputs) => {
        update(values);
        handleClose();
    };

    const handleClose = () => {
        reset();
        onClose();
    };

    return (<ModalWithBottomActionsWrapper labels={{title: "Edit Feature Toggle", actionButtonLabel: "Save"}}
                                           controls={{
                                               onSubmit: handleSubmit(onSubmit), onClose: handleClose, visible: visible
                                           }}>
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
                            key: "name", register, setValue: setValue, submitting: isSubmitting, value: selected?.name
                        }}
                        validation={{
                            validatorHint: "Some new api key",
                            minLength: 1,
                            pattern: UsedPatterns.default,
                            isRequired: true
                        }}
        />
        <TextInputArea label={"Description"} placeholder={"Enter a description"}
                       control={{
                           key: "description",
                           register: register,
                           setValue: setValue,
                           isSubmitting: isSubmitting,
                           value: selected?.description
                       }}
                       validation={{
                           validatorHint: "Enter a description", minLength: 1, isRequired: false
                       }}
        />
        <EnvironmentInputField setValue={setValue} control={control} isSubmitting={isSubmitting}
                               value={selected?.environmentActivations}/>
    </ModalWithBottomActionsWrapper>)

}
