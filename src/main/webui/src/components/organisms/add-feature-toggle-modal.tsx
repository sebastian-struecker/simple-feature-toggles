import React from "react";
import {SubmitHandler, useForm} from "react-hook-form";
import {useFeatureToggleStore} from "@/src/providers/feature-toggle-store-provider";

type AddFeatureToggleInputs = {
    key: string; name: string; description: string;
};

export function AddFeatureToggleModal() {
    const {
        register, handleSubmit, reset, formState: {errors, isSubmitting},
    } = useForm<AddFeatureToggleInputs>();
    const {create} = useFeatureToggleStore((state) => state);

    const onSubmit: SubmitHandler<AddFeatureToggleInputs> = async (values: AddFeatureToggleInputs) => {
        create(values);
        reset();
        document.getElementById('add_feature_toggle_modal')?.close();
    };

    return (<dialog id="add_feature_toggle_modal" className="modal modal-bottom sm:modal-middle">
        <div className="modal-box">
            <form method="dialog" className="flex flex-col gap-3" onSubmit={handleSubmit(onSubmit)}>
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2" type="reset">✕</button>
                <h3 className="text-center text-xl sm:text-xl font-semibold">
                    Create Feature Toggle
                </h3>
                <div className="flex flex-col">
                    <label className="form-control">
                        <div className="label">
                            <span className="label-text">
                                Key
                                <span className="text-error">*</span>
                            </span>
                        </div>
                        <input
                            {...register("key", {required: true, pattern: /^[a-z_]*[a-z]$/})}
                            disabled={isSubmitting}
                            type="text"
                            name="key"
                            placeholder="Enter a key"
                            className={`input input-primary input-md w-full ${errors.key ? "input-error" : ""}`}
                        />
                        <div className="label">
                            <span className="label-text-alt"></span>
                            {errors.key?.type == "pattern" && (
                                <span className="label-text-alt text-error">Example of a valid key: A_NEW_KEY</span>)}
                            {errors.key?.type == "required" && (
                                <span className="label-text-alt text-error">Please enter a valid key</span>)}
                        </div>
                    </label>
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
                    <label className="form-control">
                        <div className="label">
                            <span className="label-text">
                                Description
                            </span>
                        </div>
                        <textarea
                            {...register("description", {minLength: 1})} disabled={isSubmitting}
                            className={`textarea textarea-primary textarea-bordered textarea-md w-full ${errors.description ? "input-error" : ""}`}
                            placeholder="Enter a description"
                            name="description"
                        />
                        <div className="label">
                            <span className="label-text-alt"></span>
                            {errors.description?.type == "minLength" && (
                                <span className="label-text-alt text-error">Please enter a description</span>)}
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
                                document.getElementById('add_feature_toggle_modal')?.close();
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
