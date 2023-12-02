import Typography from "@mui/material/Typography";
import { FC, ReactNode } from "react"

interface TitleProps {
    children: ReactNode;
}


const Title: FC<TitleProps> = (props) => {

    return (
        <Typography variant="h5" gutterBottom component="h1">
            {props.children}
        </Typography>

    )
}

export default Title;