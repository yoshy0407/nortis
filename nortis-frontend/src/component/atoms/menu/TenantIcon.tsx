import { Domain } from "@mui/icons-material";
import { Tooltip } from "@mui/material";

export interface TenantIconProp {
    placement: "bottom" | "left" | "right" | "top" | "bottom-end" | "bottom-start" | "left-end" | "left-start" | "right-end" | "right-start" | "top-end" | "top-start"
}

export default function TenantIcon(prop: TenantIconProp) {
    return (
        <Tooltip title="Tenants" placement={prop.placement}>
            <Domain />
        </Tooltip>
    )
}