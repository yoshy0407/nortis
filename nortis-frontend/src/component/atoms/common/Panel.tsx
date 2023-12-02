"use client"
import { Box, Card, CardContent, SxProps } from "@mui/material";
import { FC, ReactNode } from "react";

interface PanelProps {
    sx: SxProps
    children: ReactNode;
}

const Panel: FC<PanelProps> = (props) => {
    return (
        <Card
            sx={props.sx}
        >
            <CardContent>
                <Box sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'
                }}>
                    {props.children}
                </Box>
            </CardContent>

        </Card>
    )
}

export default Panel;