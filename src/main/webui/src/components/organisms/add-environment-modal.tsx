import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useApiKeyStore} from "@/src/providers/api-key-store-provider";
import {CreateApiKeyInputs} from "@/src/types/create-api-key-inputs";
import {CreateEnvironmentInputs} from "@/src/types/create-environment-inputs";

type Inputs = {
    modalId: string;
}

export function AddEnvironmentModal({modalId}: Inputs) {
    const {
        register, handleSubmit, reset, formState: {errors, isSubmitting},
    } = useForm<CreateApiKeyInputs>();
    const {create} = useApiKeyStore((state) => state);

    const onSubmit: SubmitHandler<CreateEnvironmentInputs> = async (values: CreateEnvironmentInputs) => {
        create(values);
        reset();
        document.getElementById(modalId)?.close();
    };

    return (<dialog id={modalId} className="modal modal-bottom sm:modal-middle">
        <div className="modal-box">
            <form method="dialog" className="flex flex-col gap-3" onSubmit={handleSubmit(onSubmit)}>
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" type="reset" onClick={() => {
                    reset();
                    document.getElementById(modalId)?.close();
                }}>✕
                </button>
                <h3 className="text-center text-xl sm:text-xl font-semibold">
                    Create an Environment
                </h3>
                <div className="flex flex-col">
                    <label className="form-control">
                        <div className="label">
                            <span className="label-text">
                                Name
                                <span className="text-error">*</span>
                            </span>
                        </div>
                        <input
                            {...register("name", {required: true})} disabled={isSubmitting}
                            type="text"
                            name="name"
                            placeholder="Enter a name"
                            className={`input input-bordered input-primary input-md w-full ${errors.name ? "input-error" : ""}`}
                        />
                        <div className="label">
                            <span className="label-text-alt"></span>
                            {errors.name?.type == "required" && (
                                <span className="label-text-alt text-error">Please enter a valid name</span>)}
                        </div>
                    </label>
                </div>
                <div className="modal-action flex justify-center">
                    <button className="btn btn-active btn-primary btn-block max-w-[200px]" type="submit">
                        Create
                    </button>
                    <button className="btn btn-outline btn-primary btn-block max-w-[200px]" type="reset"
                            onClick={() => {
                                reset();
                                document.getElementById(modalId)?.close();
                            }}>
                        Cancel
                    </button>
                </div>
            </form>
        </div>
        <form method="dialog" className="modal-backdrop">
            <button onClick={() => {
                reset();
            }}>Close on background click
            </button>
        </form>
    </dialog>)

}
