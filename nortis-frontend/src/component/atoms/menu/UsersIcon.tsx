import { ManageAccounts } from "@mui/icons-material";
import { Tooltip } from "@mui/material";

export interface UserIconProp {
    placement: "bottom" | "left" | "right" | "top" | "bottom-end" | "bottom-start" | "left-end" | "left-start" | "right-end" | "right-start" | "top-end" | "top-start"
}

export default function UsersIcon(prop: UserIconProp) {
    return (
        <Tooltip title="Users" placement={prop.placement}>
            <ManageAccounts />
        </Tooltip>
    )
}