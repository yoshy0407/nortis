import type { Meta, StoryObj } from '@storybook/react';
import UserInfoMenu from './UserInfoMenu';

const meta = {
    title: 'molecules/UserInfoMenu',
    component: UserInfoMenu,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof UserInfoMenu>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Normal: Story = {
};