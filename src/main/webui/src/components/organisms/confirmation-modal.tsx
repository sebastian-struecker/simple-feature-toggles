import React from "react";
import {ModalWrapper} from "@/src/components/molecules/modal-wrapper";


type Inputs = {
    labels: Labels; controls: Controls;
}

type Labels = {
    title: string; description: string; actionButtonLabel: string;
}

type Controls = {
    visible: boolean; onConfirm: () => void; onClose: () => void;
}

export function ConfirmationModal({
                                      labels: {title, description, actionButtonLabel},
                                      controls: {onConfirm, onClose, visible},
                                  }: Inputs) {
    const handleClose = () => {
        onClose();
    };

    return (<ModalWrapper labels={{title: title, actionButtonLabel: actionButtonLabel}}
                          controls={{onSubmit: onConfirm, onClose: handleClose, visible: visible}}>
        <div className="w-full flex justify-center">{description}</div>
    </ModalWrapper>)

}
