import React, {RefObject, useEffect, useRef} from "react";

type Inputs = {
    labels: Labels; controls: Controls; children: React.ReactNode;
}

type Labels = {
    title: string;
}

type Controls = {
    onClose: () => void; visible: boolean;
}

export function ModalWrapper({
                                 labels: {title}, controls: {onClose, visible}, children
                             }: Inputs) {
    const modalRef = useRef(null);

    useEffect(() => {
        if (!modalRef.current) {
            return;
        }
        visible ? (modalRef.current as HTMLDialogElement).showModal() : (modalRef.current as HTMLDialogElement).close();
    }, [visible]);

    const handleClose = () => {
        if (!modalRef.current) {
            return;
        }
        onClose();
        (modalRef.current as HTMLDialogElement).close();
    };

    return (<dialog ref={modalRef} onClose={onClose} className="modal modal-bottom sm:modal-middle">
        <div className="modal-box">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" type="reset"
                    onClick={handleClose}>✕
            </button>
            <h3 className="text-center text-xl sm:text-xl font-semibold">
                {title}
            </h3>
            <div className="flex flex-col">
                {children}
            </div>
        </div>
        <form method="dialog" className="modal-backdrop">
            <button onClick={handleClose}>Close on background click
            </button>
        </form>
    </dialog>)

}
