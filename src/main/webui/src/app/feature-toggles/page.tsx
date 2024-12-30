"use client"

import {CreateFirstFeatureTogglePage} from "@/src/components/pages/create-first-feature-toggle-page";
import React, {useEffect, useState} from "react";
import {LoadingSpinner} from "@/src/components/molecules/loading-spinner";
import {AddFeatureToggleModal} from "@/src/components/organisms/add-feature-toggle-modal";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {FeatureToggleListItem} from "@/src/components/organisms/feature-toggle-list-item";
import {FaPlus} from "react-icons/fa";

export default function FeatureTogglesPage() {
    const {featureToggles, getAll} = useFeatureToggleStore((state) => state);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function awaitGetAll() {
            await getAll();
            setLoading(false);
        }

        awaitGetAll();
    }, [getAll])

    if (loading) {
        return (<LoadingSpinner/>)
    }

    return (<div className="p-6 flex justify-center">
        {featureToggles.length == 0 && <CreateFirstFeatureTogglePage/>}
        {featureToggles.length > 0 && <div className="w-5/6 flex flex-col">
            <div className="flex justify-between flex-row">
                <div className="text-xl font-semibold">
                    Feature Toggles
                </div>
                <button className="btn btn-primary"
                        onClick={() => document.getElementById('add_feature_toggle_modal')?.showModal()}>
                    <FaPlus /> Create
                </button>
            </div>

            <div className="overflow-x-auto flex justify-center">
                {featureToggles.map((featureToggle) => {
                    return (<FeatureToggleListItem featureToggle={featureToggle}
                                                   key={featureToggle.id + featureToggle.name}/>);
                })}
            </div>
        </div>}
        <AddFeatureToggleModal/>
    </div>);
}
