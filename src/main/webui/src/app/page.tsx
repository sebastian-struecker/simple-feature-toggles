"use client"

import React, {useEffect} from 'react'
import {useEnvironmentStore} from "@/src/providers/environment-store-provider";

export default function Page() {
    const {getAll} = useEnvironmentStore((state) => state);

    useEffect(() => {
        async function awaitGetAll() {
            await getAll();
        }

        awaitGetAll();
    }, [getAll])

    return (<>
        <div className="h-full min-h-96">
            Hello
        </div>
    </>);
}
