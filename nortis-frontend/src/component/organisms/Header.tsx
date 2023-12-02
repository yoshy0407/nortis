"use client"
import { Box, Toolbar, Typography, styled } from "@mui/material";
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import MenuButton from "../atoms/menu/MenuButton";
import LogoutIcon from "../atoms/LogoutIcon";
import UserInfoMenu from "../molecules/UserInfoMenu";

export interface HeaderProps {
    menuOpen: boolean;
    menuWidth: number;
    withLogin: boolean;
    onClickMenu: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

interface AppBarProps extends MuiAppBarProps {
    open?: boolean;
    menuWidth: number;
}

const AppBar = styled(
    MuiAppBar,
    {
        shouldForwardProp: (prop) => prop !== 'open',
    })<AppBarProps>(({ theme, open, menuWidth }) => ({
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        ...(open && {
            marginLeft: menuWidth,
            width: `calc(100% - ${menuWidth}px)`,
            transition: theme.transitions.create(['width', 'margin'], {
                easing: theme.transitions.easing.sharp,
                duration: theme.transitions.duration.enteringScreen,
            }),
        }),
    }));

export default function Header(props: HeaderProps) {

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="fixed" open={props.menuOpen} menuWidth={props.menuWidth}>
                <Toolbar>
                    {props.withLogin
                        ? <MenuButton
                            onClick={(e) => {
                                props.onClickMenu(e)
                            }}
                            visible={!props.menuOpen}
                        />
                        : <></>
                    }
                    <Typography variant="h6" noWrap component="div">Nortis</Typography>
                    <Box sx={{ flexGrow: 1 }} />
                    {props.withLogin
                        ? <Box sx={{ display: { md: 'flex' } }}>
                            <UserInfoMenu sx={{
                                marginRight: 2
                            }} />
                            <LogoutIcon />
                        </Box>
                        : <></>
                    }
                </Toolbar>
            </AppBar >
        </Box>
    )
}