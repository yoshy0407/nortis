import type { Meta, StoryObj } from '@storybook/react';
import Menu from './Menu';

const meta = {
    title: 'organisms/Menu',
    component: Menu,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof Menu>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Normal: Story = {
    args: {
        open: true
    }
};
