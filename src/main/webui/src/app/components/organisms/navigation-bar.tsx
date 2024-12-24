"use client"

import React from "react";
import {useSession} from "next-auth/react";
import {usePathname} from "next/navigation";
import Image from "next/image";
import {IoSettingsSharp} from "react-icons/io5";
import ICON from "@/public/icon.svg";

export function NavigationBar() {
    const session = useSession();
    const pathname = usePathname();
    return (<div className="navbar">
        <div className="navbar-start">
            <button className="btn btn-ghost">
                <div className="flex items-center gap-2">
                    <Image src={ICON} alt={"icon"} className="h-12 w-12"/>
                    <p>simple-feature-toggles</p>
                    <span className="badge badge-accent">Version 1.0.0</span>
                </div>
            </button>
        </div>
        <div className="navbar-center">
            <ul className="menu menu-horizontal gap-3">
                <li>
                    <a className={`${pathname == "/" ? "btn btn-primary" : "btn btn-ghost"}`} href={"/"}>Home</a>
                </li>
                <li>
                    <a className={`${pathname == "/feature-toggles" ? "btn btn-primary" : "btn btn-ghost"}`}
                       href={"/feature-toggles"}>Feature-Toggles</a>
                </li>
            </ul>
        </div>
        <div className="navbar-end">
            <div className="dropdown dropdown-end">
                <div tabIndex={0} role="button" className="btn btn-ghost btn-circle">
                    <IoSettingsSharp className="h-6 w-6" />
                </div>
                <ul
                    tabIndex={0}
                    className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
                    <li><a>Settings</a></li>
                    <li><a>Logout</a></li>
                </ul>
            </div>
        </div>
    </div>)

}
