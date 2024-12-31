import React from "react";

type CreateFirstTemplateInputs = {
    elementName: string;
    modalId: string;
}

export function CreateFirstTemplate({elementName, modalId}: CreateFirstTemplateInputs) {
    return (<>
        <div className="hero bg-base-100 min-h-[80vh]">
            <div className="hero-content text-center">
                <div className="max-w-md">
                    <h1 className="text-4xl font-bold">Pretty empty here</h1>
                    <p className="py-6">
                        Start by creating your first <b>{elementName}!</b>
                    </p>
                    <button className="btn btn-primary"
                            onClick={() => document.getElementById(modalId)?.showModal()}>Start here
                    </button>
                </div>
            </div>
        </div>
    </>)
}
