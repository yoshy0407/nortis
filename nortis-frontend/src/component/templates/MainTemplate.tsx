import Box from '@mui/material/Box'
import Header from '../organisms/Header'
import { ReactNode, useState } from 'react'
import Typography from '@mui/material/Typography';
import Menu from '../organisms/Menu';
import { CssBaseline, ThemeProvider } from '@mui/material';
import { theme } from '../theme';

const menuWidth = 240;

export interface MainTemplateProps {
    /**
     * 子構造
     */
    children: ReactNode;
}

const MainTemplate: React.FC<MainTemplateProps> = (props: MainTemplateProps) => {

    const [menuOpen, setMenuOpen] = useState(false);

    return (
        <ThemeProvider theme={theme} >
            <Box sx={{ display: 'flex' }}>
                <CssBaseline />
                <Header
                    menuWidth={240}
                    menuOpen={menuOpen}
                    withLogin={true}
                    onClickMenu={(e) => setMenuOpen(!menuOpen)}
                />
                <Menu
                    menuWidth={menuWidth}
                    open={menuOpen}
                    onClickMenuClose={e => setMenuOpen(false)}
                />
                <Box
                    component="main"
                    sx={{
                        flexGrow: 1,
                        p: 3,
                        marginTop: 8
                    }}
                >
                    {props.children}
                </Box>
            </Box>
        </ThemeProvider>
    )
}

export default MainTemplate;
