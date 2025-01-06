import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useApiKeyStore} from "@/src/providers/api-key-store-provider";
import {CreateApiKeyInputs} from "@/src/types/create-api-key-inputs";
import {TextInputField, UsedPatterns} from "@/src/components/molecules/text-input-field";
import {EnvironmentInputField} from "@/src/components/molecules/environment-input-field";
import {ModalWrapper} from "@/src/components/molecules/modal-wrapper";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function AddApiKeyModal({visible, onClose}: Inputs) {
    const {
        control, register, handleSubmit, reset, setValue, formState: {errors, isSubmitting},
    } = useForm<CreateApiKeyInputs>({defaultValues: {environmentActivation: new Map()}});
    const {create} = useApiKeyStore((state) => state);

    const onSubmit: SubmitHandler<CreateApiKeyInputs> = async (values: CreateApiKeyInputs) => {
        create(values);
        onClose();
    };

    const handleClose = () => {
        reset();
        onClose();
    };

    return (<ModalWrapper labels={{title: "Create an Api Key", actionButtonLabel: "Create"}}
                          controls={{onSubmit: handleSubmit(onSubmit), onClose: handleClose, visible: visible}}>
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
                               formKey={"environment"} setValue={setValue}
                               register={register} error={errors.environmentActivation}
                               isSubmitting={isSubmitting}/>
    </ModalWrapper>)

}
