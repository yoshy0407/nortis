import { Alert, Box, Card, CardActions, CardContent } from "@mui/material";
import EditButton from "../atoms/EditButton";
import NegativeButton from "../atoms/common/NegativeButton";
import Field from "../atoms/common/Field";
import React, { useEffect, useState } from "react";

interface PasswordChangeCardProps {
    onCancel: (e: React.MouseEvent<HTMLInputElement>) => void;
}


export default function PasswordChangeCard(props: PasswordChangeCardProps) {
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [newPasswordConfirm, setNewPasswordConfirm] = useState("");
    const [message, setMessage] = useState("");
    const [disabled, setDisabled] = useState(true);

    useEffect(() => {
        let disabled = false;
        if (currentPassword === "") {
            disabled = true;
        }
        if (newPassword === "") {
            disabled = true;
        }
        if (newPasswordConfirm === "") {
            disabled = true;
        }

        if (newPassword !== newPasswordConfirm) {
            setMessage("new Password not match")
            disabled = true;
        }
        if (newPassword === newPasswordConfirm) {
            setMessage("");
        }
        setDisabled(disabled);
    }, [currentPassword, newPassword, newPasswordConfirm])

    return (
        <Card sx={{
            maxWidth: 400
        }}>
            <CardContent>
                <Box sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'
                }}>

                    {message !== ""
                        ? <Alert severity="error">{message}</Alert>
                        : <></>
                    }
                    <Field label="Current Password" value={currentPassword} onChange={(e) => setCurrentPassword(e.target.value)} />
                    <Field label="New Password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} />
                    <Field label="New Password (Confirm)" value={newPasswordConfirm} onChange={(e) => setNewPasswordConfirm(e.target.value)} />
                </Box>
            </CardContent>

            <CardActions>
                <EditButton label="Update" onClick={() => { }} disabled={disabled} />
                <NegativeButton label="Cancel" onClick={() => { }} />
            </CardActions>
        </Card>
    )
}