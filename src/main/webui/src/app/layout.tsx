import type {Metadata} from "next";
import {Inter} from "next/font/google";
import "./globals.css";
import {auth} from "@/auth";
import Providers from "@/src/app/providers";
import React from "react";
import {NavigationBar} from "@/src/app/components/organisms/navigation-bar";

const inter = Inter({subsets: ["latin"]});

export const metadata: Metadata = {
    title: "Feature Toggles", description: "",
};

export default async function RootLayout({
                                             children,
                                         }: Readonly<{
    children: React.ReactNode;
}>) {
    const session = await auth();
    return (<html lang="en" data-theme="emerald">
        <body className="h-full light text-foreground bg-background">
            <Providers session={session}>
                <NavigationBar />
                {children}
                {/*<Footer />*/}
            </Providers>
        </body>
    </html>);
}
