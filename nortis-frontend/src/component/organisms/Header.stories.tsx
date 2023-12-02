import type { Meta, StoryObj } from '@storybook/react';
import Header from './Header';

const meta = {
    title: 'organisms/Header',
    component: Header,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof Header>;

export default meta;
type Story = StoryObj<typeof meta>;

export const MenuOpen: Story = {
    args: {
        menuWidth: 240,
        withLogin: false,
        menuOpen: true
    }
};

export const MenuClose: Story = {
    args: {
        menuWidth: 240,
        withLogin: false,
        menuOpen: false
    }
};

export const LoginMenu: Story = {
    args: {
        menuWidth: 240,
        withLogin: true,
        menuOpen: false
    }
};
