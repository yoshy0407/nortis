import UsersIcon from "../../atoms/menu/UsersIcon";
import MenuItem from "../../atoms/common/MenuItem";

export interface UserMenuProp {
    /**
     * ミニサイズにするかどうか
     */
    mini: boolean;
}

export default function UserMenuItem(prop: UserMenuProp) {
    return (
        <MenuItem uri="/users" mini={prop.mini} text="Users">
            <UsersIcon placement="right-start" />
        </MenuItem>
    )
}