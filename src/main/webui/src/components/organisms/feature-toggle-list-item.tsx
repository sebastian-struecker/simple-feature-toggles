"use client"

import React from "react";
import {FeatureToggle} from "@/src/types/feature-toggle";
import {useRouter} from "next/navigation";

export function FeatureToggleListItem({featureToggle}: { featureToggle: FeatureToggle }) {
    const router = useRouter();

    return (<div onClick={() => router.push("/feature-toggles/" + featureToggle.id)}>
        {featureToggle.name}
    </div>)

}
