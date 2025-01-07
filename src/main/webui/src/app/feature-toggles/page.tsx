"use client"

import React, {useEffect, useState} from "react";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {FaPen, FaPlus, FaTrash} from "react-icons/fa";
import {AddFeatureToggleModal} from "@/src/components/organisms/add-feature-toggle-modal";
import {ConfirmationModal} from "@/src/components/organisms/confirmation-modal";
import {EditFeatureToggleModal} from "@/src/components/organisms/edit-feature-toggle-modal";
import {EnvironmentActivationStatusPill} from "@/src/components/molecules/environment-activation-status-pill";


export default function FeatureTogglesPage() {
    const {
        featureToggles, selected, setSelected, getAll, deleteById
    } = useFeatureToggleStore((state) => state);
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
        {featureToggles.map((element) => {
            return (<tr key={element.key + element.id}
                        className="hover hover:bg-base-200">
                <td className="w-1/5">
                    <div className="flex flex-col gap-3">
                        <span className="font-semibold">{element.name}</span>
                        <span className="badge badge-ghost badge-md">{element.key}</span>
                    </div>
                </td>
                <td className="w-1/4">
                    <div>{element.description}</div>
                </td>
                <td className="max-w-[16rem]">
                    <div className="grid grid-cols-3 gap-1">
                        {element.environmentActivations.length != 0 && element.environmentActivations.map((env) => {
                            const {environmentKey, isActive} = env;
                            return (
                                <EnvironmentActivationStatusPill key={environmentKey + isActive}
                                                                 environmentKey={environmentKey} isActive={isActive}/>)
                        })}
                    </div>
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
                    Feature Toggles
                </div>
                <button className="btn btn-primary"
                        onClick={() => {
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
                        <th>Description</th>
                        <th>Activation</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    {renderDataRows()}
                </table>
                {(!isLoading && featureToggles.length == 0) && <div className="flex justify-center w-full">
                    <div className="text-l font-bold">No elements</div>
                </div>}
            </div>
        </div>
        <AddFeatureToggleModal visible={addModalVisible} onClose={() => {
            setAddModalVisible(false)
        }}/>
        <EditFeatureToggleModal visible={editModalVisible} onClose={() => {
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
