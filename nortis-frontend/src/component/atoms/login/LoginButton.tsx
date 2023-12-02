import PositiveButton from "../common/PositiveButton";

interface LoginButtonProps {
    disabled: boolean;
}

export default function LoginButton(props: LoginButtonProps) {
    return (
        <PositiveButton
            label="Login"
            type="submit"
            disabled={props.disabled}
            onClick={() => { }}
            fullWidth
            sx={{
                mt: 3,
                mb: 3
            }} />
    )
}