import type { Meta, StoryObj } from '@storybook/react';
import PasswordChangeCard from './PasswordChangeCard';

const meta = {
    title: 'molecules/PasswordChangeCard',
    component: PasswordChangeCard,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof PasswordChangeCard>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Normal: Story = {
    args: {
        onCancel: () => { }
    }
};