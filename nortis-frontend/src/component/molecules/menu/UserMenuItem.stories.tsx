import type { Meta, StoryObj } from '@storybook/react';
import UserMenuItem from './UserMenuItem';

const meta = {
    title: 'molecules/menu/UserMenuItem',
    component: UserMenuItem,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof UserMenuItem>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Normal: Story = {
    args: {
        mini: false
    }
};

export const Mini: Story = {
    args: {
        mini: true
    }
};
