"use client"

import React, {useEffect, useState} from 'react'
import {FaPen, FaPlus, FaTrash} from "react-icons/fa";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {AddEnvironmentModal} from "@/src/components/organisms/add-environment-modal";
import {EditEnvironmentModal} from "@/src/components/organisms/edit-environment-modal";
import {ConfirmationModal} from "@/src/components/organisms/confirmation-modal";


export default function EnvironmentsPage() {
    const {
        environments, selected, setSelected, getAll, deleteById
    } = useEnvironmentStore((state) => state);
    const [addModalVisible, setAddModalVisible] = useState(false);
    const [editModalVisible, setEditModalVisible] = useState(false);
    const [confirmModalVisible, setConfirmModalVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        async function awaitGetAll() {
            await getAll();
            setIsLoading(false);
        }

        awaitGetAll();
    }, [getAll])

    const renderDataRows = () => {
        return (<tbody>
        {environments?.map((element) => {
            return (<tr key={element.name + element.id}
                        className="hover hover:cursor-pointer hover:bg-base-200"
                        onClick={() => {
                            setSelected(element);
                            setEditModalVisible(true);
                        }}>
                <td>
                    <div>{element.key}</div>
                </td>
                <td>
                    <div>{element.name}</div>
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
                    Environments
                </div>
                <button className="btn btn-primary"
                        onClick={() => {
                            setAddModalVisible(true);
                        }}>
                    <FaPlus/> Create
                </button>
            </div>
            <div className="overflow-x-auto flex justify-center flex-col">
                <table className="table table-lg">
                    <thead>
                    <tr>
                        <th>Key</th>
                        <th>Name</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    {renderDataRows()}
                </table>
                {(!isLoading && environments.length == 0) && <div className="flex justify-center w-full">
                    <div className="text-l font-bold">No elements</div>
                </div>}
            </div>
        </div>
        <AddEnvironmentModal visible={addModalVisible} onClose={() => {
            setAddModalVisible(false);
        }}/>
        <EditEnvironmentModal visible={editModalVisible} onClose={() => {
            setEditModalVisible(false);
        }}/>
        <ConfirmationModal
            labels={{
                title: "Confirm delete",
                description: "Are you sure you want to delete: '" + selected?.name + "'?",
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
