import { Button, SxProps } from "@mui/material";
import React, { ReactNode } from "react";
import { Edit } from "@mui/icons-material";

interface EditButtonProps {
    label: string;
    disabled?: boolean;
    fullWidth?: boolean;
    sx?: SxProps;
    type?: "button" | "reset" | "submit";
    onClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

export default function EditButton(props: EditButtonProps) {
    return (
        <Button
            color="success"
            type={props.type}
            variant="contained"
            onClick={props.onClick}
            disabled={props.disabled}
            startIcon={<Edit />}
            fullWidth={props.fullWidth}
            sx={props.sx}
        >
            {props.label}
        </Button>
    )
}