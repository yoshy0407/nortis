import ConsumerIcon from "../../atoms/menu/ConsumerIcon";
import MenuItem from "../../atoms/common/MenuItem";

export interface ConsumerMenuItemProp {
    /**
     * ミニサイズにするかどうか
     */
    mini: boolean;
}

export default function ConsumerMenuItem(prop: ConsumerMenuItemProp) {
    return (
        <MenuItem uri="/consumers" mini={prop.mini} text="Consumers">
            <ConsumerIcon placement="right-start" />
        </MenuItem>
    )
}