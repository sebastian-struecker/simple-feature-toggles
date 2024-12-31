'use client'

import {useEffect, useState} from "react";
import {LoadingSpinner} from "@/src/components/molecules/loading-spinner";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";
import {FeatureToggle} from "@/src/types/feature-toggle";
import {MdDelete} from "react-icons/md";
import {UrlPath} from "@/src/constants/url-path";

export default function FeatureToggleDetailPage({params}: {
    params: Promise<{ slug: string }>
}) {
    const {getById} = useFeatureToggleStore((state) => state);
    const [featureToggle, setFeatureToggle] = useState<FeatureToggle | undefined>(undefined);
    const [loading, setLoading] = useState(true);
    const deleteAction = () => {
        console.log();
    };

    useEffect(() => {
        async function awaitGetById() {
            const {slug} = await params;
            const response = await getById(Number(slug));
            setFeatureToggle(response);
            setLoading(false);
        }

        awaitGetById();
    }, [getById, params])


    if (loading) {
        return (<LoadingSpinner/>)
    }

    return (<div className="p-6 h-full min-h-96">
        <div className="breadcrumbs text-sm">
            <ul>
                <li><a href={`/${UrlPath.featureToggles}`}>Feature-Toggles</a></li>
                <li>{featureToggle?.name}</li>
            </ul>
        </div>
        <div className="hero">
            <div className="hero-content text-center">
                <div className="max-w-md">
                    <h1 className="text-5xl font-bold">{featureToggle?.name}</h1>
                    <p className="py-6">
                        Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem
                        quasi. In deleniti eaque aut repudiandae et a id nisi.
                    </p>
                    <button className="btn btn-primary">Get Started</button>
                </div>
            </div>
        </div>
        <div className="lg:tooltip" data-tip="Delete">
            <button onClick={deleteAction} className="btn btn-ghost">
                <MdDelete className="fill-secondary h-7 w-7"/>
            </button>
        </div>
    </div>);
}
