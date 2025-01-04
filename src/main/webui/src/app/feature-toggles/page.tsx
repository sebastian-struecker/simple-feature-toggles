"use client"

import React, {useEffect} from "react";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {FaPlus} from "react-icons/fa";
import {UrlPath} from "@/src/constants/url-path";
import {useRouter} from "next/navigation";
import {AddFeatureToggleModal} from "@/src/components/organisms/add-feature-toggle-modal";

const modalId = "add_feature_toggle_modal";

export default function FeatureTogglesPage() {
    const router = useRouter();
    const {featureToggles, isLoading, getAll} = useFeatureToggleStore((state) => state);

    useEffect(() => {
        async function awaitGetAll() {
            await getAll();
        }

        awaitGetAll();
    }, [getAll])

    const renderSkeletonRows = () => {
        return (<tbody>
        {Array.from(Array(10).keys()).map(i => {
            return (<tr key={"skeleton-" + i}>
                <td>
                    <div className="skeleton h-9"></div>
                </td>
                <td>
                    <div className="skeleton h-9"></div>
                </td>
                <td>
                    <div className="skeleton h-9"></div>
                </td>
                <td>
                    <div className="skeleton h-9"></div>
                </td>
            </tr>);
        })}
        </tbody>);
    };

    const renderDataRows = () => {
        return (<tbody>
        {featureToggles.map((featureToggle) => {
            return (<tr key={featureToggle.key + featureToggle.id}
                        onClick={() => router.push(`/${UrlPath.featureToggles}/` + featureToggle.id)}
                        className="hover hover:bg-base-200 hover:cursor-pointer">
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
        </tbody>);
    }

    return (<div className="h-full min-h-96 p-6 flex justify-center">
        <div className="w-5/6 flex flex-col">
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
                    {isLoading && renderSkeletonRows()}
                    {!isLoading && renderDataRows()}
                </table>
            </div>
        </div>
        <AddFeatureToggleModal modalId={modalId}/>
    </div>);
}
