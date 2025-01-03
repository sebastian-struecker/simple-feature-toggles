"use client"

import React, {useEffect, useState} from 'react'
import {FaPlus} from "react-icons/fa";
import {AddApiKeyModal} from "@/src/components/organisms/add-api-key-modal";
import {useApiKeyStore} from "@/src/providers/api-key-store-provider";
import {UrlPath} from "@/src/constants/url-path";
import {useRouter} from "next/navigation";

const modalId = "add_api_key_modal";

export default function ApiKeysPage() {
    const router = useRouter();
    const {apiKeys, getAll} = useApiKeyStore((state) => state);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function awaitGetAll() {
            await getAll();
            setLoading(false);
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
            </tr>);
        })}
        </tbody>);
    };

    const renderDataRows = () => {
        return (<tbody>
        {apiKeys.map((apiKey) => {
            return (<tr key={apiKey.name + apiKey.id}
                        onClick={() => router.push(`/${UrlPath.apiKeys}/` + apiKey.id)}
                        className="hover hover:cursor-pointer">
                <td className="w-1/5">
                    <div>{apiKey.name}</div>
                </td>
                <td className="w-1/4">
                    <div>{apiKey.secret}</div>
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
                    Api Keys
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
                        <th>Name</th>
                        <th>Secret</th>
                        <th>Activation</th>
                    </tr>
                    </thead>
                    {loading && renderSkeletonRows()}
                    {renderDataRows()}
                </table>
            </div>
        </div>
        <AddApiKeyModal modalId={modalId}/>
    </div>);
}
