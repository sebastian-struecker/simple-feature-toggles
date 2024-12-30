import {signInAction} from "@/src/actions/auth";

export function LoadingSpinner() {
    return (<div className="flex justify-center min-h-[30vh]"><span
        className="loading loading-spinner loading-lg text-primary"></span></div>)
}
