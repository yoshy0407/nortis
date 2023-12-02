import type { Meta, StoryObj } from '@storybook/react';
import ConsumerMenuItem from './ConsumerMenuItem';

const meta = {
    title: 'molecules/menu/ConsumerMenuItem',
    component: ConsumerMenuItem,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof ConsumerMenuItem>;

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
