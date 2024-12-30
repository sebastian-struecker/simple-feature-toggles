"use client"

import React from "react";
import {useSession} from "next-auth/react";
import {usePathname} from "next/navigation";
import Image from "next/image";
import {HiDotsHorizontal} from "react-icons/hi";
import ICON from "@/public/icon.svg";
import {signOutAction} from "@/src/actions/auth";

export function NavigationBar() {
    const session = useSession();
    const pathname = usePathname();

    return (<div className="navbar bg-base-100">
        <div className="navbar-start">
            <button className="btn btn-ghost">
                <div className="flex items-center gap-2">
                    <Image src={ICON} alt={"icon"} className="h-12 w-12"/>
                    <p>simple-feature-toggles</p>
                </div>
            </button>
        </div>
        <div className="navbar-center">
            <ul className="menu menu-horizontal gap-2">
                <li>
                    <a className={`${pathname == "/" ? "btn btn-primary" : "btn btn-ghost"}`} href={"/"}>Home</a>
                </li>
                <li>
                    <a className={`${pathname.includes("feature-toggles") ? "btn btn-primary" : "btn btn-ghost"}`}
                       href={"/feature-toggles"}>Feature-Toggles</a>
                </li>
            </ul>
        </div>
        <div className="navbar-end gap-2">
            <span className="badge badge-accent">Version 1.0.0</span>
            <div className="dropdown dropdown-end">
                <div tabIndex={0} role="button" className="btn btn-ghost btn-circle">
                    <HiDotsHorizontal className="h-6 w-6"/>
                </div>
                <ul
                    tabIndex={0}
                    className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
                    <li><a>Api-Keys</a></li>
                    <li><a onClick={() => {signOutAction()}}>Logout</a></li>
                </ul>
            </div>
        </div>
    </div>)

}
