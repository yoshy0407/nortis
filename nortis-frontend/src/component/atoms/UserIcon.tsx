import { IconButton, SxProps, Tooltip } from "@mui/material";
import AccountCicle from '@mui/icons-material/AccountCircle'

interface UserIconProps {
    sx?: undefined | SxProps;
    onClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

export default function UserIcon(props: UserIconProps) {

    return (
        <Tooltip title="User Info" sx={props.sx} >
            <IconButton
                aria-controls="menu-appbar"
                size="large"
                edge="start"
                color="inherit"
                onClick={props.onClick}
            >
                <AccountCicle />
            </IconButton>
        </Tooltip>
    )
}