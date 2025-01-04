"use client"

import React, {useEffect, useState} from 'react'
import {FaPen, FaPlus, FaTrash} from "react-icons/fa";
import {AddApiKeyModal} from "@/src/components/organisms/add-api-key-modal";
import {useApiKeyStore} from "@/src/providers/api-key-store-provider";
import {ConfirmationModal} from "@/src/components/organisms/confirmation-modal";
import {EditApiKeyModal} from "@/src/components/organisms/edit-api-key-modal";


export default function ApiKeysPage() {
    const {apiKeys, selected, setSelected, isLoading, getAll, deleteById} = useApiKeyStore((state) => state);
    const [addModalVisible, setAddModalVisible] = useState(false);
    const [editModalVisible, setEditModalVisible] = useState(false);
    const [confirmModalVisible, setConfirmModalVisible] = useState(false);

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
        {apiKeys.map((element) => {
            return (<tr key={element.name + element.id}
                        className="hover hover:bg-base-200 hover:cursor-pointer">
                <td className="w-1/5">
                    <div>{element.name}</div>
                </td>
                <td className="w-1/4">
                    <div>{element.secret}</div>
                </td>
                <td>
                    <div>dev</div>
                </td>
                <td className="max-w-0.5">
                    <div className="flex gap-2">
                        <div className="lg:tooltip" data-tip="Edit">
                            <button className="btn btn-soft btn-secondary" onClick={() => {
                                setSelected(element);
                                setEditModalVisible(true);
                            }}><FaPen/></button>
                        </div>
                        <div className="lg:tooltip" data-tip="Remove">
                            <button className="btn btn-soft btn-secondary" onClick={() => {
                                setSelected(element);
                                setConfirmModalVisible(true);
                            }}><FaTrash/></button>
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
                    Api Keys
                </div>
                <button className="btn btn-primary" onClick={() => {
                    setAddModalVisible(true)
                }}>
                    <FaPlus/> Create
                </button>
            </div>
            <div className="overflow-x-auto flex justify-center flex-col">
                <table className="table table-lg">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Secret</th>
                        <th>Activation</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    {isLoading && renderSkeletonRows()}
                    {!isLoading && renderDataRows()}
                </table>
                {apiKeys.length == 0 && <div className="flex justify-center w-full">
                    <div className="text-l font-bold">No elements</div>
                </div>}
            </div>
        </div>
        <AddApiKeyModal visible={addModalVisible} onClose={() => {
            setAddModalVisible(false);
        }}/>
        <EditApiKeyModal visible={editModalVisible} onClose={() => {
            setEditModalVisible(false);
        }}/>
        <ConfirmationModal
            labels={{
                title: "Confirm delete",
                description: "Are you sure you want to " + selected?.name + " ?",
                actionButtonLabel: "Delete"
            }}
            controls={{
                onConfirm: () => {
                    if (selected) {
                        deleteById(selected.id);
                    }
                    setConfirmModalVisible(false);
                }, onClose: () => {
                    setConfirmModalVisible(false);
                }, visible: confirmModalVisible
            }}/>
    </div>);
}
