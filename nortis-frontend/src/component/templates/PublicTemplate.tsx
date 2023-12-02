"use client"
import Box from "@mui/material/Box";
import { ReactNode } from "react"
import Header from "../organisms/Header";
import { Grid, ThemeProvider } from "@mui/material";
import { theme } from "../theme";


export interface PublicTemplateProps {
    children: ReactNode;
}


const PublicTemplate: React.FC<PublicTemplateProps> = (props: PublicTemplateProps) => {
    return (
        <ThemeProvider theme={theme} >
            <Box sx={{ display: 'flex' }}>
                <Header
                    menuWidth={240}
                    menuOpen={false}
                    withLogin={false}
                    onClickMenu={(e) => { }}
                />

                <Grid sx={{
                    marginTop: 20
                }} container alignItems='center' justifyContent='center' direction="column">
                    <Grid item xs={12} md={12}>
                        {props.children}
                    </Grid>
                </Grid>
            </Box>
        </ThemeProvider>
    )
}

export default PublicTemplate;
