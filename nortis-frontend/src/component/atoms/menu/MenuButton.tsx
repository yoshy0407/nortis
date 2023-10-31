import { IconButton } from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu'
import React from "react";

export interface MenuButtonProp {
    onClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

export default function MenuButton(prop: MenuButtonProp) {

    return (
        <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            onClick={(e) => prop.onClick(e)}
            sx={{ mr: 2 }}
        >
            <MenuIcon />
        </IconButton>
    )
}