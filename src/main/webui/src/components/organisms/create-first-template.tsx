import React, {useEffect} from "react";
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {UrlPath} from "@/src/constants/url-path";
import {FaPen, FaPlus, FaTrash} from "react-icons/fa";
import {AddEnvironmentModal} from "@/src/components/organisms/add-environment-modal";

type CreateFirstTemplateInputs = {
    elementName: string; modalId: string; environmentNotice?: boolean;
}

export function CreateFirstTemplate({elementName, modalId, environmentNotice = true}: CreateFirstTemplateInputs) {

    const {environments, getAll} = useEnvironmentStore((state) => state);

    useEffect(() => {
        async function awaitGetAll() {
            await getAll();
        }

        if (environmentNotice) {
            awaitGetAll();
        }
    }, [environmentNotice, getAll])

    if (environmentNotice && environments.length == 0) {
        return (<>
            <div className="hero bg-base-100 min-h-[80vh]">
                <div className="hero-content text-center">
                    <div className="max-w-md gap-2">
                        <h1 className="text-2xl font-bold pb-6">Start by creating an environment</h1>
                        <a className="btn btn-primary" href={`/${UrlPath.environments}`}>Start here</a>
                    </div>
                </div>
            </div>
        </>);
    }

    return (<>
        <div className="h-full min-h-96 p-6">
            <div className="flex flex-col">
                <div className="text-xl font-semibold">
                    Environments
                </div>
                <div className="hero bg-base-100 min-h-[80vh]">
                    <div className="hero-content text-center">
                        <div className="max-w-md">
                            <h1 className="text-4xl font-bold">Pretty empty here</h1>
                            <p className="py-6">
                                Start by creating your first <b>{elementName}!</b>
                            </p>
                            <button className="btn btn-primary"
                                    onClick={() => document.getElementById(modalId)?.showModal()}>Start here
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </>);
}
