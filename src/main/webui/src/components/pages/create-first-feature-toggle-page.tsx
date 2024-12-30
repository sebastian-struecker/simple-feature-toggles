import React from "react";
import {AddFeatureToggleModal} from "@/src/components/organisms/add-feature-toggle-modal";

export function CreateFirstFeatureTogglePage() {
    return (<>
        <div className="hero bg-base-100 min-h-[80vh]">
            <div className="hero-content text-center">
                <div className="max-w-md">
                    <h1 className="text-5xl font-bold">Pretty empty here</h1>
                    <p className="py-6">
                        Start by creating your first feature toggle
                    </p>
                    <button className="btn btn-primary"
                            onClick={() => document.getElementById('add_feature_toggle_modal')?.showModal()}>Start here
                    </button>
                </div>
            </div>
        </div>
        <AddFeatureToggleModal/>
    </>)
}
