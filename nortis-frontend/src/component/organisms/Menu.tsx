import { CSSObject, Divider, MenuList, Theme, useTheme, styled, IconButton } from "@mui/material";
import MuiDrawer from '@mui/material/Drawer';
import TenantMenuItem from "../molecules/menu/TenantMenuItem";
import EndpointMenuItem from "../molecules/menu/EndpointMenuItem";
import ConsumerMenuItem from "../molecules/menu/ConsumerMenuItem";
import EventMenuItem from "../molecules/menu/EventMenuItem";
import UserMenuItem from "../molecules/menu/UserMenuItem";
import ChevronRightIcon from '@mui/icons-material/ChevronRight'
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft'


const drawerWidth = 240;

export interface MenuProp {
    open: boolean;
    onClickMenuClose: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

const openedMixin = (theme: Theme): CSSObject => ({
    width: drawerWidth,
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
    }),
    overflowX: 'hidden',
});

const closedMixin = (theme: Theme): CSSObject => ({
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: `calc(${theme.spacing(7)} + 1px)`,
    [theme.breakpoints.up('sm')]: {
        width: `calc(${theme.spacing(8)} + 1px)`,
    },
});

const Drawer = styled(MuiDrawer, { shouldForwardProp: (prop) => prop !== 'open' })(
    ({ theme, open }) => ({
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
        boxSizing: 'border-box',
        ...(open && {
            ...openedMixin(theme),
            '& .MuiDrawer-paper': openedMixin(theme),
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

export default function Menu(prop: MenuProp) {
    const theme = useTheme();
    const mini = () => !prop.open;

    return (
        <Drawer variant="permanent" anchor="left" open={prop.open}>
            <MenuList>
                //TODO
                <DrawerHeader>
                    <IconButton onClick={prop.onClickMenuClose}>
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