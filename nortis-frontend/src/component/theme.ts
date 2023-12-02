import { createTheme } from "@mui/material";

export const theme = createTheme({
    palette: {
        primary: {
            main: '#9393ff',
            contrastText: '#ffffff'
        },
        secondary: {
            main: '#82cddd'
        },
        error: {
            main: '#ff7f7f'
        },
        warning: {
            main: '#ffbf7f'
        },
        info: {
            main: '#7fbfff'
        },
        success: {
            main: '#0075c2'
        }
    }
})