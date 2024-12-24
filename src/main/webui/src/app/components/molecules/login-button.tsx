import {signInAction} from "@/src/app/actions/auth";

export function LoginButton() {
    return (<form
        action={() => {
            signInAction();
        }}
    >
        <button type="submit" className="btn btn-square btn-ghost">Login</button>
    </form>)
}
