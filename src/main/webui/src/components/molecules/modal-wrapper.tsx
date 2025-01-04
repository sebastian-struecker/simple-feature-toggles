import React, {useEffect, useRef} from "react";

type Inputs = {
    labels: Labels; controls: Controls; children: React.ReactNode;
}

type Labels = {
    title: string; actionButtonLabel: string;
}

type Controls = {
    onSubmit: (value: any) => any; onClose: () => void; visible: boolean;
}

export function ModalWrapper({
                                 labels: {title, actionButtonLabel}, controls: {onSubmit, onClose, visible}, children
                             }: Inputs) {
    const modalRef = useRef(null);

    useEffect(() => {
        if (!modalRef.current) {
            return;
        }
        visible ? modalRef.current.showModal() : modalRef.current.close();
    }, [visible]);

    const handleClose = () => {
        if (!modalRef.current) {
            return;
        }
        onClose();
        modalRef.current.close();
    };

    return (<dialog ref={modalRef} className="modal modal-bottom sm:modal-middle">
        <div className="modal-box">
            <form method="dialog" className="flex flex-col gap-3" onSubmit={onSubmit}>
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" type="reset"
                        onClick={handleClose}>✕
                </button>
                <h3 className="text-center text-xl sm:text-xl font-semibold">
                    {title}
                </h3>
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
        </div>
        <form method="dialog" className="modal-backdrop">
            <button onClick={handleClose}>Close on background click
            </button>
        </form>
    </dialog>)

}
