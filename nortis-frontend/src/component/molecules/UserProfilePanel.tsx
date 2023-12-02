import { Card, CardActions, CardContent, Table, TableBody, TableCell, TableRow } from "@mui/material";
import EditButton from "../atoms/EditButton";
import { User } from "@/domains/types";

interface UserProfilePanelProps {
    user: User;
}


export default function UserProfilePanel(props: UserProfilePanelProps) {

    return (
        <Card>
            <CardContent>
                <Table sx={{}} aria-label="user profile table">
                    <TableBody>
                        <TableRow>
                            <TableCell>User ID</TableCell>
                            <TableCell>{props.user.userId}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell>Username</TableCell>
                            <TableCell>{props.user.username}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell>Login ID</TableCell>
                            <TableCell>{props.user.loginId}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </CardContent>
            <CardActions>
                <EditButton label="Edit" onClick={() => { }} />
            </CardActions>
        </Card>
    )
}