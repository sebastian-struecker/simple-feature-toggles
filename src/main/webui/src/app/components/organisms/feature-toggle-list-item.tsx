"use client"

import React from "react";
import {TableCell} from "@nextui-org/react";
import {useSession} from "next-auth/react";
import {usePathname} from "next/navigation";

export function FeatureToggleListItem() {
    const session = useSession();
    const pathname = usePathname();
    return (<>
        <TableCell>Tony Reichert</TableCell>
        <TableCell>CEO</TableCell>
        <TableCell>Active</TableCell>
    </>);
}
