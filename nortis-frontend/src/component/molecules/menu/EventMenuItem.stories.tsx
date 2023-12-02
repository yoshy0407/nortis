import type { Meta, StoryObj } from '@storybook/react';
import EventMenuItem from './EventMenuItem';

const meta = {
    title: 'molecules/menu/EventMenuItem',
    component: EventMenuItem,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof EventMenuItem>;

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
