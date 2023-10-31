import TenantIcon from "../../atoms/menu/TenantIcon";
import MenuItem from "../../common/MenuItem";

export interface TenantMenuProp {
    /**
     * ミニサイズにするかどうか
     */
    mini: boolean;
}

export default function TenantMenuItem(prop: TenantMenuProp) {
    return (
        <MenuItem uri="/tenants" mini={prop.mini} text="Tenants">
            <TenantIcon placement="right-start" />
        </MenuItem>
    )
}