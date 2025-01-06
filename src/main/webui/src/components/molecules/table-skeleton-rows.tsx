import React from "react";

type Inputs = {
    rows: number
    columns: number;
}

export function TableSkeletonRows({rows, columns}: Inputs) {
    return (<tbody>
    {Array.from(Array(rows).keys()).map(i => {
        return (<tr key={"skeleton-" + i}>
            {Array.from(Array(columns).keys()).map(j => {
                return (<td key={"skeleton-" + i + j}>
                    <div className="skeleton h-9"></div>
                </td>)
            })}
        </tr>);
    })}
    </tbody>)
}
