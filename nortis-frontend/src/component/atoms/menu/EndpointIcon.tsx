import { Send } from "@mui/icons-material";
import { Tooltip } from "@mui/material";

export interface EndpointIconProp {
    placement: "bottom" | "left" | "right" | "top" | "bottom-end" | "bottom-start" | "left-end" | "left-start" | "right-end" | "right-start" | "top-end" | "top-start"
}

export default function EndpointIcon(prop: EndpointIconProp) {
    return (
        <Tooltip title="Endpoints" placement={prop.placement}>
            <Send />
        </Tooltip>
    )
}