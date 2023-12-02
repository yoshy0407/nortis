"use client"
import { Menu, MenuItem, SxProps } from "@mui/material";
import React, { useState } from "react";
import UserIcon from "../atoms/UserIcon";
import PasswordChangeModal, { PasswordChangeModalType } from "../organisms/PasswordChangeModal";

export interface UserInfoMenuProps {
    sx?: undefined | SxProps;
}

export default function UserInfoMenu(props: UserInfoMenuProps) {
    const modalRef = React.useRef<PasswordChangeModalType>(null);
    const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);

    const menuOpen = (e: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(e.currentTarget);
    }
    const menuClose = (e: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(null);
    }
    const isMenuOpen = () => {
        return anchorEl !== null;
    }
    const openProfile = (e: React.MouseEvent<HTMLElement>) => {
        menuClose(e);
    }
    const openChangePassword = (e: React.MouseEvent<HTMLElement>) => {
        menuClose(e);
        modalRef.current?.show();
    }

    return (
        <div>
            <UserIcon onClick={menuOpen} sx={props.sx} />
            <Menu
                anchorEl={anchorEl}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right'
                }}
                keepMounted
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right'
                }}
                open={isMenuOpen()}
                onClose={menuClose}
            >
                <MenuItem onClick={openProfile}>Profile</MenuItem>
                <MenuItem onClick={openChangePassword}>Change Password</MenuItem>
            </Menu>
            <PasswordChangeModal ref={modalRef} />
        </div>
    )
}