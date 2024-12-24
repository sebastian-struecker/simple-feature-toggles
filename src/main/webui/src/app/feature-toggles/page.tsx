"use client"

import {FeatureToggle} from "@/src/types/feature-toggle";

const skeletonRow = (<tr>
    <td>
        <div className="skeleton h-7"></div>
    </td>
    <td>
        <div className="skeleton h-7"></div>
    </td>
    <td>
        <div className="skeleton h-7"></div>
    </td>
    <td>
        <div className="skeleton h-7"></div>
    </td>
    <td>
        <div className="skeleton h-7"></div>
    </td>
</tr>)

export default function Page() {
    const featureToggles: FeatureToggle[] = [];
    const loading = true;

    const displayRows = () => {
        if (loading) {
            return Array(6).fill(skeletonRow);
        }
        return featureToggles.map((featureToggle, index) => {
            <tr>
                <th>{index}</th>
                <td>{featureToggle.key}</td>
                <td>{featureToggle.name}</td>
                <td>{featureToggle.description}</td>
                <td>Dev</td>
            </tr>
        });
    };

    return (<>
        <div className="overflow-x-auto">
            <table className="table">
                <thead>
                    <tr>
                        <th></th>
                        <th>Key</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Context</th>
                    </tr>
                </thead>
                <tbody>
                    {displayRows()}
                </tbody>
            </table>
        </div>
    </>);
}
