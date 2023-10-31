import UserIcon from "../../atoms/menu/UserIcon";
import MenuItem from "../../common/MenuItem";

export interface UserMenuProp {
    /**
     * ミニサイズにするかどうか
     */
    mini: boolean;
}

export default function UserMenuItem(prop: UserMenuProp) {
    return (
        <MenuItem uri="/users" mini={prop.mini} text="Users">
            <UserIcon placement="right-start" />
        </MenuItem>
    )
}