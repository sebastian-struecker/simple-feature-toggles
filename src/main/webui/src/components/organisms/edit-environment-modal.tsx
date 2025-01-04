import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {ModalWrapper} from "@/src/components/molecules/modal-wrapper";
import {UpdateEnvironmentInputs} from "@/src/types/update-environment-inputs";

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

    return (<ModalWrapper labels={{title: "Edit Environment", actionButtonLabel: "Save"}}
                          controls={{onSubmit: handleSubmit(onSubmit), onClose: handleClose, visible: visible}}>
        <div className="flex flex-col">
            {selected?.name}
            {selected?.key}
        </div>
    </ModalWrapper>)

}
