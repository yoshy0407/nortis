import { Button, SxProps } from "@mui/material";
import React, { ReactNode } from "react";

interface NegativeButtonProps {
    label: string;
    disabled?: boolean;
    startIcon?: ReactNode;
    endIcon?: ReactNode;
    fullWidth?: boolean;
    sx?: SxProps;
    type?: "button" | "reset" | "submit";
    onClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

export default function NegativeButton(props: NegativeButtonProps) {
    return (
        <Button
            color="primary"
            variant="outlined"
            type={props.type}
            onClick={props.onClick}
            disabled={props.disabled}
            startIcon={props.startIcon}
            endIcon={props.endIcon}
            fullWidth={props.fullWidth}
            sx={props.sx}
        >
            {props.label}
        </Button>
    )
}