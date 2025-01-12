"use client"

import React from "react";
import {usePathname, useRouter} from "next/navigation";
import Image from "next/image";
import {HiDotsHorizontal} from "react-icons/hi";
import ICON from "@/public/icon.svg";
import {signOutAction} from "@/src/actions/auth";
import {UrlPath} from "@/src/constants/url-path";
import {useUser} from "@/src/utils/useUser";

export function NavigationBar() {
    const pathname = usePathname();
    const router = useRouter();
    const {role} = useUser();

    return (<div className="navbar bg-base-100 shadow-sm">
        <div className="navbar-start">
            <div className="dropdown">
                <div tabIndex={0} role="button" className="btn btn-ghost lg:hidden">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="h-5 w-5"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth="2"
                            d="M4 6h16M4 12h8m-8 6h16"/>
                    </svg>
                </div>
                <ul
                    tabIndex={0}
                    className="menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow gap-0.5">
                    <li>
                        <a className={`${pathname.includes(UrlPath.featureToggles) ? "btn btn-primary" : "btn btn-ghost"}`}
                           href={`/${UrlPath.featureToggles}`}>Feature-Toggles</a>
                    </li>
                    <li>
                        <a className={`${pathname.includes(UrlPath.apiKeys) ? "btn btn-primary" : "btn btn-ghost"}`}
                           href={`/${UrlPath.apiKeys}`}>Api-Keys</a>
                    </li>
                    <li>
                        <a className={`${pathname.includes(UrlPath.environments) ? "btn btn-primary" : "btn btn-ghost"}`}
                           href={`/${UrlPath.environments}`}>Environments</a>
                    </li>
                </ul>
            </div>
            <a href={"/"}>
                <div className="flex items-center gap-0.5">
                    <Image src={ICON} alt={"icon"} className="h-12 w-12"/>
                    <p className="font-semibold">simple-feature-toggles</p>
                </div>
            </a>
        </div>
        <div className="navbar-center hidden lg:flex">
            <ul className="menu menu-horizontal gap-0.5">
                <li>
                    <a className={`${pathname.includes(UrlPath.featureToggles) ? "btn btn-primary" : "btn btn-ghost"}`}
                       href={`/${UrlPath.featureToggles}`}>Feature-Toggles</a>
                </li>
                <li>
                    <a className={`${pathname.includes(UrlPath.apiKeys) ? "btn btn-primary" : "btn btn-ghost"}`}
                       href={`/${UrlPath.apiKeys}`}>Api-Keys</a>
                </li>
                <li>
                    <a className={`${pathname.includes(UrlPath.environments) ? "btn btn-primary" : "btn btn-ghost"}`}
                       href={`/${UrlPath.environments}`}>Environments</a>
                </li>
            </ul>
        </div>
        <div className="navbar-end gap-2">
            <div className="dropdown dropdown-end">
                <div tabIndex={0} role="button" className="btn btn-ghost btn-circle">
                    <HiDotsHorizontal className="h-6 w-6"/>
                </div>
                <ul
                    tabIndex={0}
                    className="menu menu-md dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
                    <li><a onClick={() => {
                        signOutAction();
                        router.push("/api/auth/signin");
                    }}>Logout</a></li>
                    <li className="menu-title">Role: {role()}</li>
                    <li className="menu-title">Version: 1.0.0</li>
                </ul>
            </div>
        </div>
    </div>)

}
