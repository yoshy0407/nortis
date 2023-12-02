import type { Meta, StoryObj } from '@storybook/react';
import TenantMenuItem from './TenantMenuItem';

const meta = {
    title: 'molecules/menu/TenantMenuItem',
    component: TenantMenuItem,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof TenantMenuItem>;

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
