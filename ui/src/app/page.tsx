"use client"

import React, {useEffect} from 'react'
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";
import {useApiKeyStore} from "@/src/providers/api-key-store-provider";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {useUser} from "@/src/utils/useUser";

export default function Page() {
    const {user, role} = useUser();
    const {environments, getAll: getAllEnvironments} = useEnvironmentStore((state) => state);
    const {apiKeys, getAll: getAllApiKeys} = useApiKeyStore((state) => state);
    const {featureToggles, getAll: getAllFeatureToggles} = useFeatureToggleStore((state) => state);

    useEffect(() => {
        async function awaitGetAll() {
            await getAllFeatureToggles();
        }

        awaitGetAll();
    }, [getAllFeatureToggles])

    useEffect(() => {
        async function awaitGetAll() {
            await getAllApiKeys();
        }

        awaitGetAll();
    }, [getAllApiKeys])

    useEffect(() => {
        async function awaitGetAll() {
            await getAllEnvironments();
        }

        awaitGetAll();
    }, [getAllEnvironments])

    return (<>
        <div className="hero h-full min-h-96">
            <div className="hero-content flex-col text-center">
                <h1 className="text-5xl font-bold flex flex-row">
                    <div>
                        Hey {user?.name}
                    </div>
                    <span className="badge badge-md badge-accent">{role()}</span>
                </h1>
                <div className="stats shadow border border-base-200">
                    <div className="stat">
                        <div className="stat-title font-semibold">Feature-Toggles</div>
                        <div className="stat-value text-primary">{featureToggles.length}</div>
                    </div>

                    <div className="stat">
                        <div className="stat-title font-semibold">Api-Keys</div>
                        <div className="stat-value text-primary">{apiKeys.length}</div>
                    </div>

                    <div className="stat">
                        <div className="stat-title font-semibold">Environments</div>
                        <div className="stat-value text-primary">{environments.length}</div>
                    </div>
                </div>
            </div>
        </div>
    </>);
}
