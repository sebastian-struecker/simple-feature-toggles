'use client'

import {useRouter} from "next/navigation";
import {useEffect} from "react";

export default function Error({
                                              error,
                                              reset,
                                          }: {
    error: Error & { digest?: string }
    reset: () => void
}) {
    const router = useRouter();
    useEffect(() => {
        router.push("/");
    }, [router]);
    return (<></>)
}
