import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {ModalWrapper} from "@/src/components/molecules/modal-wrapper";
import {UpdateApiKeyInputs} from "@/src/types/update-api-key-inputs";
import {useApiKeyStore} from "@/src/providers/api-key-store-provider";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function EditApiKeyModal({visible, onClose}: Inputs) {
    const {
        register, handleSubmit, reset, formState: {isSubmitting},
    } = useForm<UpdateApiKeyInputs>();
    const {selected, update} = useApiKeyStore((state) => state);

    const onSubmit: SubmitHandler<UpdateApiKeyInputs> = async (values: UpdateApiKeyInputs) => {
        update(values);
        handleClose();
    };

    const handleClose = () => {
        reset();
        onClose();
    };

    return (<ModalWrapper labels={{title: "Edit Api Key", actionButtonLabel: "Save"}}
                          controls={{onSubmit: handleSubmit(onSubmit), onClose: handleClose, visible: visible}}>
        <div className="flex flex-col">
            {selected?.name}
        </div>
    </ModalWrapper>)

}
