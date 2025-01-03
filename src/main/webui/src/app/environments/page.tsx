"use client"

import React, {useEffect} from 'react'
import {FaPen, FaPlus, FaTrash} from "react-icons/fa";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {AddEnvironmentModal} from "@/src/components/organisms/add-environment-modal";

const modalId = "add_environment_modal";

export default function EnvironmentsPage() {
    const {environments, hasHydrated, getAll} = useEnvironmentStore((state) => state);

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
            </tr>);
        })}
        </tbody>);
    };

    const renderDataRows = () => {
        return (<tbody>
        {environments?.map((environment) => {
            return (<tr key={environment.name + environment.id}
                        className="hover">
                <td>
                    <div>{environment.key}</div>
                </td>
                <td>
                    <div>{environment.name}</div>
                </td>
                <td className="max-w-0.5">
                    <div className="flex gap-2">
                        <div className="lg:tooltip" data-tip="Edit">
                            <button className="btn btn-soft btn-secondary"><FaPen/></button>
                        </div>
                        <div className="lg:tooltip" data-tip="Remove">
                            <button className="btn btn-soft btn-secondary"><FaTrash/></button>
                        </div>
                    </div>
                </td>
            </tr>);
        })}
        </tbody>);
    }

    return (<div className="h-full min-h-96 p-6 flex justify-center">
        <div className="w-5/6 flex flex-col">
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
                        <th>Actions</th>
                    </tr>
                    </thead>
                    {hasHydrated && renderSkeletonRows()}
                    {renderDataRows()}
                </table>
            </div>
        </div>
        <AddEnvironmentModal modalId={modalId}/>
    </div>);
}
