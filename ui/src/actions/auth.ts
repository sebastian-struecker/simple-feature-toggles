'use server'

import {signIn, signOut} from "@/auth";

export async function signInAction() {
    "use server"
    await signIn();
}

export async function signOutAction() {
    "use server"
    await signOut();
}
