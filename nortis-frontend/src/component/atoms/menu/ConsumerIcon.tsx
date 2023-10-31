import { Mediation } from "@mui/icons-material";
import { Tooltip } from "@mui/material";

export interface ConsumerIconProp {
    placement: "bottom" | "left" | "right" | "top" | "bottom-end" | "bottom-start" | "left-end" | "left-start" | "right-end" | "right-start" | "top-end" | "top-start"
}

export default function ConsumerIcon(prop: ConsumerIconProp) {
    return (
        <Tooltip title="Consumers" placement={prop.placement}>
            <Mediation />
        </Tooltip>
    )
}