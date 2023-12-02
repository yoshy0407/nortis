import { Button, SxProps } from "@mui/material";
import React, { ReactNode } from "react";

interface PositiveButtonProps {
    label: string;
    disabled?: boolean;
    startIcon?: ReactNode;
    endIcon?: ReactNode;
    fullWidth?: boolean;
    sx?: SxProps;
    type?: "button" | "reset" | "submit";
    onClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

export default function PositiveButton(props: PositiveButtonProps) {
    return (
        <Button
            color="primary"
            type={props.type}
            variant="contained"
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