import { IconButton } from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu'
import React from "react";

export interface MenuButtonProps {
    visible: boolean;
    onClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

export default function MenuButton(props: MenuButtonProps) {

    return (
        <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            onClick={(e) => props.onClick(e)}
            sx={{
                mr: 2,
                ...(!props.visible && { display: 'none' })
            }}
        >
            <MenuIcon />
        </IconButton>
    )
}