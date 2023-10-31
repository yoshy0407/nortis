import EndpointIcon from "../../atoms/menu/EndpointIcon";
import MenuItem from "../../common/MenuItem";

export interface EndpointMenuProp {
    /**
     * ミニサイズにするかどうか
     */
    mini: boolean;
}

export default function EndpointMenuItem(prop: EndpointMenuProp) {
    return (
        <MenuItem uri="/endpoints" mini={prop.mini} text="Endpoints">
            <EndpointIcon placement="right-start" />
        </MenuItem>
    )
}