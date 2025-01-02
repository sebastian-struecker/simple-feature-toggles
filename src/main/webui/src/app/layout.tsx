import type {Metadata} from "next";
import {Inter} from "next/font/google";
import "./globals.css";
import {auth} from "@/auth";
import ProviderWrapper from "@/src/providers/provider-wrapper";
import React from "react";
import {NavigationBar} from "@/src/components/organisms/navigation-bar";
import {Toaster} from "react-hot-toast";

const inter = Inter({subsets: ["latin"]});

export const metadata: Metadata = {
    title: "Feature Toggles", description: "", icons: "/icon.ico"
};

export default async function RootLayout({
                                             children,
                                         }: Readonly<{
    children: React.ReactNode;
}>) {
    const session = await auth();
    return (<html lang="en">
    <body className={"h-full light text-foreground bg-background" + inter}>
    <ProviderWrapper session={session}>
        <NavigationBar/>
        <Toaster position="top-right" toastOptions={{duration: 4000}}/>
        {children}
    </ProviderWrapper>
    </body>
    </html>);
}
