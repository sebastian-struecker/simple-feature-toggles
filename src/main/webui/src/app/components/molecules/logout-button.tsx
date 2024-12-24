import {signOutAction} from "@/src/app/actions/auth";

export function LogoutButton() {
    return (<form
        action={() => {
            signOutAction();
        }}
    >
        <button type="submit" className="btn btn-secondary">Logout</button>
    </form>)
}
