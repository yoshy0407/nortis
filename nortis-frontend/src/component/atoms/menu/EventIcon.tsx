import { InsertInvitation } from "@mui/icons-material";
import { Tooltip } from "@mui/material";

export interface EventIconProp {
    placement: "bottom" | "left" | "right" | "top" | "bottom-end" | "bottom-start" | "left-end" | "left-start" | "right-end" | "right-start" | "top-end" | "top-start"
}

export default function EventIcon(prop: EventIconProp) {
    return (
        <Tooltip title="Endpoints" placement={prop.placement}>
            <InsertInvitation />
        </Tooltip>
    )
}