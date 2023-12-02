
export type UserRole = {
    tenantId: string;
    roleId: string;
}

export type User = {
    userId: string;
    username: string;
    loginId: string;
    roles: UserRole[]
}