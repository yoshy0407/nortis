import { IconButton, SxProps, Tooltip } from "@mui/material";
import Logout from '@mui/icons-material/Logout'

interface LogoutIconProps {
    sx?: undefined | SxProps;
}

export default function LogoutIcon(props: LogoutIconProps) {

    const doLogout = () => {
        //:TODO
    };

    return (
        <Tooltip title="logout" sx={props.sx} >
            <IconButton
                aria-controls="menu-appbar"
                size="large"
                edge="start"
                color="inherit"
                onClick={doLogout}
            >
                <Logout />
            </IconButton>
        </Tooltip>
    )
}