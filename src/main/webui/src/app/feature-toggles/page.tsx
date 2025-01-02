"use client"

import React, {useEffect, useState} from "react";
import {LoadingSpinner} from "@/src/components/molecules/loading-spinner";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {FaPen, FaPlus, FaTrash} from "react-icons/fa";
import {CreateFirstTemplate} from "@/src/components/organisms/create-first-template";
import {UrlPath} from "@/src/constants/url-path";
import {useRouter} from "next/navigation";
import {AddFeatureToggleModal} from "@/src/components/organisms/add-feature-toggle-modal";

const modalId = "add_feature_toggle_modal";

export default function FeatureTogglesPage() {
    const router = useRouter();
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

    return (<div className="h-full min-h-96 p-6 flex justify-center">
        {featureToggles?.length == 0 && <CreateFirstTemplate elementName={"Feature Toggle"} modalId={modalId}/>}
        {featureToggles?.length > 0 && <div className="w-5/6 flex flex-col">
            <div className="flex justify-between flex-row">
                <div className="text-xl font-semibold">
                    Feature Toggles
                </div>
                <button className="btn btn-primary"
                        onClick={() => document.getElementById(modalId)?.showModal()}>
                    <FaPlus/> Create
                </button>
            </div>
            <div className="overflow-x-auto">
                <table className="table table-lg">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Activation</th>
                    </tr>
                    </thead>
                    <tbody>
                    {featureToggles.map((featureToggle) => {
                        return (<tr key={featureToggle.key + featureToggle.id}
                                    onClick={() => router.push(`/${UrlPath.featureToggles}/` + featureToggle.id)}
                                    className="hover hover:cursor-pointer">
                            <td className="w-1/5">
                                <div className="flex flex-col gap-3">
                                    <span className="font-semibold">{featureToggle.name}</span>
                                    <span className="badge badge-ghost badge-md">{featureToggle.key}</span>
                                </div>
                            </td>
                            <td className="w-1/4">
                                <div>{featureToggle.description}</div>
                            </td>
                            <td>
                                <div>dev</div>
                            </td>
                        </tr>);
                    })}
                    </tbody>
                </table>
            </div>
        </div>}
        <AddFeatureToggleModal modalId={modalId}/>
    </div>);
}
