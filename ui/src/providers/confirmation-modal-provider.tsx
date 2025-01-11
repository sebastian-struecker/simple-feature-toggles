"use client";

import React from "react";
import {ModalWithBottomActionsWrapper} from "@/src/components/molecules/modal-with-bottom-actions-wrapper";
import {ConfirmationModal} from "@/src/components/organisms/confirmation-modal";

const modalId = "confirmation_modal"

export const useConfirmDialog = () => {
    const open = () => {
        // document.getElementById(modalId)?.showModal();
    };
    return {open};
}

export default function ConfirmationModalProvider({children}: { children: React.ReactNode }) {
    const onClose = () => {
        // document.getElementById(modalId)?.close();
    };
    return (<>
        {children}
        {/*<ModalWithBottomActionsWrapper labels={{title: title, actionButtonLabel: actionButtonLabel}}*/}
        {/*                               controls={{onSubmit: onConfirm, onClose: handleClose, visible: visible}}>*/}
        {/*    <div className="w-full flex justify-center">{description}</div>*/}
        {/*</ModalWithBottomActionsWrapper>*/}

        {/*<ConfirmationModal*/}
        {/*    labels={{*/}
        {/*        title: "Confirm delete",*/}
        {/*        description: "Are you sure you want to delete: '" + selected?.name + "'?",*/}
        {/*        actionButtonLabel: "Delete"*/}
        {/*    }}*/}
        {/*    controls={{*/}
        {/*        onConfirm: () => {*/}
        {/*            if (selected) {*/}
        {/*                deleteById(selected.id);*/}
        {/*            }*/}
        {/*            setConfirmModalVisible(false);*/}
        {/*        }, onClose: () => {*/}
        {/*            setConfirmModalVisible(false);*/}
        {/*        }, visible: confirmModalVisible*/}
        {/*    }}/>*/}
    </>)
}
