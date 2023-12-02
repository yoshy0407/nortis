import { ChangeEvent, ChangeEventHandler } from "react";
import Field from "../common/Field";
import { ValidationResult, notEmpty } from "../../validations";

interface LoginIdProps {
    value: unknown;
    onChange: (event: ChangeEvent<HTMLInputElement>, result: ValidationResult) => void;
}

export default function LoginIdField(props: LoginIdProps) {

    const validation = (v: string) => {
        return notEmpty(v, "Please set your login ID")
    }

    return (
        <Field
            label="LoginId"
            value={props.value}
            required={true}
            onChange={props.onChange}
            fullWidth
            autoFocus
            validation={validation}
        />
    )
}