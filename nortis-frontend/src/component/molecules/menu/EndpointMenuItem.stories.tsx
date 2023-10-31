import type { Meta, StoryObj } from '@storybook/react';
import EndpointMenuItem from './EndpointMenuItem';

const meta = {
    title: 'molecules/menu/EndpointMenuItem',
    component: EndpointMenuItem,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof EndpointMenuItem>;

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
