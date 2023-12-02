import type { Meta, StoryObj } from '@storybook/react';
import LoginPanel from './LoginPanel';

const meta = {
    title: 'molecules/LoginPanel',
    component: LoginPanel,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof LoginPanel>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Normal: Story = {
};