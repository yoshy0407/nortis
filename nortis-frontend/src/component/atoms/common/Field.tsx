import { TextField } from "@mui/material";
import { ChangeEvent, ChangeEventHandler, HTMLInputTypeAttribute, ReactNode, useState } from "react";
import { ValidationResult, success } from "../../validations";


interface FieldProps {
    label: string;
    value: unknown;
    onChange: (event: ChangeEvent<HTMLInputElement>, result: ValidationResult) => void;
    id?: string;
    required?: boolean;
    disabled?: boolean;
    defaultValue?: unknown;
    type?: HTMLInputTypeAttribute;
    fullWidth?: boolean;
    autoFocus?: boolean;
    validation?: (value: string) => ValidationResult;
}


export default function Field(props: FieldProps) {
    const [error, setError] = useState(false);
    const [message, setMessage] = useState<string | undefined>('');

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value;
        if (props.validation) {
            const result = props.validation(value);
            setError(result.error);
            setMessage(result.message);
            props.onChange(event, result)
        } else {
            props.onChange(event, success());
        }
    }

    return (
        <TextField
            variant="outlined"
            label={props.label}
            value={props.value}
            onChange={handleChange}
            id={props.id}
            required={props.required}
            disabled={props.disabled}
            defaultValue={props.defaultValue}
            type={props.type}
            fullWidth={props.fullWidth}
            autoFocus={props.autoFocus}
            margin="normal"
            error={error}
        />
    )
}