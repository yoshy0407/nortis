import type { Meta, StoryObj } from '@storybook/react';
import UserProfilePanel from './UserProfilePanel';

const meta = {
    title: 'molecules/UserProfilePanel',
    component: UserProfilePanel,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof UserProfilePanel>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Normal: Story = {
    args: {
        user: {
            userId: "1234567",
            username: "Test User",
            loginId: "testId",
            roles: []
        }
    }
};