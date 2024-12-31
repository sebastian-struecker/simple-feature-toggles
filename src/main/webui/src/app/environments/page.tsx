"use client"

import React, {useEffect, useState} from 'react'
import {LoadingSpinner} from "@/src/components/molecules/loading-spinner";
import {CreateFirstTemplate} from "@/src/components/organisms/create-first-template";
import {FaPlus} from "react-icons/fa";
import {AddApiKeyModal} from "@/src/components/organisms/add-api-key-modal";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {UrlPath} from "@/src/constants/url-path";
import {useRouter} from "next/navigation";

const modalId = "add_environment_modal";

export default function EnvironmentsPage() {
    const router = useRouter();
    const {environments, getAll} = useEnvironmentStore((state) => state);
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
        {environments.length == 0 && <CreateFirstTemplate elementName={"Environment"} modalId={modalId}/>}
        {environments.length > 0 && <div className="w-5/6 flex flex-col">
            <div className="flex justify-between flex-row">
                <div className="text-xl font-semibold">
                    Environments
                </div>
                <button className="btn btn-primary"
                        onClick={() => document.getElementById(modalId)?.showModal()}>
                    <FaPlus/> Create
                </button>
            </div>
            <div className="overflow-x-auto flex justify-center">
                <table className="table table-lg">
                    <thead>
                    <tr>
                        <th>Key</th>
                        <th>Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {environments.map((environment) => {
                        return (<tr key={environment.name + environment.id}
                                    onClick={() => router.push(`/${UrlPath.environments}/` + environment.id)}
                                    className="hover hover:cursor-pointer">
                            <td>
                                <div>{environment.key}</div>
                            </td>
                            <td>
                                <div>{environment.name}</div>
                            </td>
                        </tr>);
                    })}
                    </tbody>
                </table>
            </div>
        </div>}
        <AddApiKeyModal modalId={modalId}/>
    </div>);
}
