import { ChangeEvent, ChangeEventHandler } from "react";
import Field from "../common/Field";
import { ValidationResult, notEmpty } from "../../validations";

interface LoginPasswordProps {
    value: unknown;
    onChange: (event: ChangeEvent<HTMLInputElement>, result: ValidationResult) => void;
}

export default function LoginPasswordField(props: LoginPasswordProps) {

    const validation = (v: string) => {
        return notEmpty(v, "Please set your password")
    }

    return (
        <Field
            label="Password"
            value={props.value}
            required={true}
            onChange={props.onChange}
            validation={validation}
            fullWidth
            type="password"
        />
    )
}