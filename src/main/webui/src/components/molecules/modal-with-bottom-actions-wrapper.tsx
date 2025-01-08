import React from "react";
import {ModalWrapper} from "@/src/components/atoms/modal-wrapper";

type Inputs = {
    labels: Labels; controls: Controls; children: React.ReactNode;
}

type Labels = {
    title: string; actionButtonLabel: string;
}

type Controls = {
    onSubmit: (value: any) => any; onClose: () => void; visible: boolean;
}

export function ModalWithBottomActionsWrapper({
                                                  labels: {title, actionButtonLabel},
                                                  controls: {onSubmit, onClose, visible},
                                                  children
                                              }: Inputs) {
    const handleClose = () => {
        onClose();
    };

    return (<ModalWrapper labels={{title}} controls={{onClose, visible}}>
        <form method="dialog" className="flex flex-col gap-3" onSubmit={onSubmit}>
            <div className="flex flex-col">
                {children}
            </div>
            <div className="modal-action flex justify-center">
                <button className="btn btn-primary btn-block max-w-[12rem]" type="submit">
                    {actionButtonLabel}
                </button>
                <button className="btn btn-outline btn-primary btn-block max-w-[12rem]" type="reset"
                        onClick={handleClose}>
                    Cancel
                </button>
            </div>
        </form>
    </ModalWrapper>)

}
