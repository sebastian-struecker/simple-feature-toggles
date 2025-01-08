import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {ModalWithBottomActionsWrapper} from "@/src/components/molecules/modal-with-bottom-actions-wrapper";
import {UpdateFeatureToggleInputs} from "@/src/types/update-feature-toggle-inputs";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";

type Inputs = {
    visible: boolean; onClose: () => void;
}

export function EditFeatureToggleModal({visible, onClose}: Inputs) {
    const {
        register, handleSubmit, reset, formState: {isSubmitting},
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
                                           controls={{onSubmit: handleSubmit(onSubmit), onClose: handleClose, visible: visible}}>
        <div className="flex flex-col">
            {selected?.name}
        </div>
    </ModalWithBottomActionsWrapper>)

}
