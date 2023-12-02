"use client"
import { Avatar, Card, CardActions, CardContent, Grid, Link, Stack, Typography } from "@mui/material";
import LoginIdField from "../atoms/login/LoginIdField";
import LoginPasswordField from "../atoms/login/LoginPasswordField";
import { LockOutlined } from "@mui/icons-material";
import { useEffect, useState } from "react";
import { ValidationResult } from "../validations";
import { Box } from "@mui/system";
import LoginButton from "../atoms/login/LoginButton";
import Title from "../atoms/common/Title";

interface FieldResult {
    value: string;
    validationResult: boolean;
}

const defaultResult: FieldResult = {
    value: '',
    validationResult: false
}

const createResult = (value: string, validationResult: boolean) => {
    return {
        value,
        validationResult
    }
}

export default function LoginPanel() {
    const [loginId, setLoginId] = useState<FieldResult>(defaultResult);
    const changeLoginId = (e: React.ChangeEvent<HTMLInputElement>, result: ValidationResult) => {
        setLoginId(createResult(e.target.value, result.error));
    }

    const [password, setPassword] = useState<FieldResult>(defaultResult);
    const changePassword = (e: React.ChangeEvent<HTMLInputElement>, result: ValidationResult) => {
        setPassword(createResult(e.target.value, result.error));
    }

    const [btnDisable, setBtnDisable] = useState<boolean>(true);
    useEffect(
        () => {
            setBtnDisable(loginId.validationResult && password.validationResult);
        },
        [loginId, password]
    )

    return (
        <Card
            sx={{
                minWidth: 50,
                maxWidth: 450
            }}
            elevation={3}
        >
            <CardContent>
                <Box sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'
                }}>
                    <Avatar sx={{ m: 1, bgcolor: 'primary.main' }} >
                        <LockOutlined />
                    </Avatar>
                    <Title>
                        Login
                    </Title>
                    <Box component="form" onSubmit={() => { }} sx={{ mt: 1 }}>
                        <LoginIdField value={loginId.value} onChange={changeLoginId} />
                        <LoginPasswordField value={password.value} onChange={changePassword} />
                        <LoginButton disabled={btnDisable} />
                    </Box>
                    <Grid container>
                        <Grid item xs>
                            <Link href="#" variant="body2">
                                Forgot password?
                            </Link>
                        </Grid>
                    </Grid>
                </Box>
            </CardContent>
        </Card>
    )
}