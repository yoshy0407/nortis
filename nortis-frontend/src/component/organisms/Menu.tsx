import { CSSObject, Divider, MenuList, Theme, useTheme, styled, IconButton, ListItem } from "@mui/material";
import MuiDrawer from '@mui/material/Drawer';
import TenantMenuItem from "../molecules/menu/TenantMenuItem";
import EndpointMenuItem from "../molecules/menu/EndpointMenuItem";
import ConsumerMenuItem from "../molecules/menu/ConsumerMenuItem";
import EventMenuItem from "../molecules/menu/EventMenuItem";
import UserMenuItem from "../molecules/menu/UserMenuItem";
import ChevronRightIcon from '@mui/icons-material/ChevronRight'
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft'


export interface MenuProps {
    open: boolean;
    menuWidth: number;
    onClickMenuClose: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

function openedMixin(theme: Theme, width: number): CSSObject {
    return {
        width,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
        overflowX: 'hidden',
    }
}

function closedMixin(theme: Theme): CSSObject {
    return {
        transition: theme.transitions.create(
            'width',
            {
                easing: theme.transitions.easing.sharp,
                duration: theme.transitions.duration.leavingScreen,
            }
        ),
        overflowX: 'hidden',
        width: `calc(${theme.spacing(7)} + 1px)`,
        [theme.breakpoints.up('sm')]: {
            width: `calc(${theme.spacing(8)} + 1px)`,
        },
    }
}

interface DrawerProps {
    menuWidth: number;
}

const Drawer = styled(MuiDrawer, { shouldForwardProp: (prop) => prop !== 'open' })<DrawerProps>(
    ({ theme, open, menuWidth }) => ({
        width: menuWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
        boxSizing: 'border-box',
        ...(open && {
            ...openedMixin(theme, menuWidth),
            '& .MuiDrawer-paper': openedMixin(theme, menuWidth),
        }),
        ...(!open && {
            ...closedMixin(theme),
            '& .MuiDrawer-paper': closedMixin(theme),
        }),
    }),
);

const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
}));

export default function Menu(props: MenuProps) {
    const theme = useTheme();
    const mini = () => !props.open;

    return (
        <Drawer variant="permanent" anchor="left" open={props.open} menuWidth={props.menuWidth} >
            <MenuList>
                <DrawerHeader>
                    <IconButton onClick={props.onClickMenuClose}>
                        {theme.direction === 'rtl' ? <ChevronRightIcon /> : <ChevronLeftIcon />}
                    </IconButton>
                </DrawerHeader>
                <EndpointMenuItem mini={mini()} />
                <ConsumerMenuItem mini={mini()} />
                <EventMenuItem mini={mini()} />
                <UserMenuItem mini={mini()} />
                <Divider />
                <TenantMenuItem mini={mini()} />
            </MenuList>
        </Drawer>
    )
}