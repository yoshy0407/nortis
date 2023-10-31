import EventIcon from "../../atoms/menu/EventIcon";
import MenuItem from "../../common/MenuItem";

export interface EventMenuProp {
    /**
     * ミニサイズにするかどうか
     */
    mini: boolean;
}

export default function EventMenuItem(prop: EventMenuProp) {
    return (
        <MenuItem uri="/events" mini={prop.mini} text="Events">
            <EventIcon placement="right-start" />
        </MenuItem>
    )
}