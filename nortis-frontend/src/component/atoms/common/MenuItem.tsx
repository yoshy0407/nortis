import { ListItemButton, ListItemIcon, ListItemText } from "@mui/material"
import ListItem from "@mui/material/ListItem"
import { useRouter } from "next/router"
import { FC, ReactElement, ReactNode } from "react";

export interface ConsumerMenuItemProp {
    /**
     * ミニサイズにするかどうか
     */
    mini: boolean;
    /**
     * リンク先のURI
     */
    uri: string;
    /**
     * テキスト
     */
    text: string;
    /**
     * 子構造
     */
    children: ReactNode;
}


const MenuItem: FC<ConsumerMenuItemProp> = (prop) => {
    const router = useRouter();

    return (
        <ListItem sx={{ display: 'block' }}>
            <ListItemButton
                onClick={(e) => router.push(prop.uri)}
                sx={{
                    minHeight: 48,
                    justifyContent: prop.mini ? 'center' : 'initial',
                    px: 2.5
                }}>
                <ListItemIcon
                    sx={{
                        minWidth: 0,
                        mr: prop.mini ? 'auto' : 3,
                        justifyContent: 'center'
                    }}
                >
                    {prop.children}
                </ListItemIcon>
                <ListItemText primary={prop.text} sx={{ opacity: prop.mini ? 0 : 1 }}></ListItemText>
            </ListItemButton>
        </ListItem>
    )
}
export default MenuItem;