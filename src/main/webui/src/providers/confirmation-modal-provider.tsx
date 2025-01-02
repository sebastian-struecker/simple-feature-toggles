"use client";

import React from "react";

const modalId = "confirmation_modal"

export const useConfirmDialog = () => {
    const open = () => {
        document.getElementById(modalId)?.showModal();
    };
    return {open};
}

export default function ConfirmationModalProvider({children}: { children: React.ReactNode }) {
    const onClose = () => {
        document.getElementById(modalId)?.close();
    };
    return (<>
        {children}
        <dialog id={modalId} className="modal modal-bottom sm:modal-middle">
            <div className="modal-box">
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" type="reset"
                        onClick={onClose}>✕
                </button>
                <h3 className="text-center text-xl sm:text-xl font-semibold">
                    Confirm
                </h3>
                <div className="flex flex-col">
                    Confirm this please
                </div>
                <div className="modal-action flex justify-center">
                    <button className="btn btn-active btn-primary btn-block max-w-[200px]" type="submit">
                        Confirm
                    </button>
                    <button className="btn btn-outline btn-primary btn-block max-w-[200px]" type="reset"
                            onClick={onClose}>
                        Cancel
                    </button>
                </div>
            </div>
            <form method="dialog" className="modal-backdrop">
                <button onClick={onClose}>Close on background click
                </button>
            </form>
        </dialog>
    </>)
}
