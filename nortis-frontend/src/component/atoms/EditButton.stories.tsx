import type { Meta, StoryObj } from '@storybook/react';
import EditButton from './EditButton';
import { Send } from '@mui/icons-material';
import { ThemeProvider } from '@mui/material';
import { theme } from '../theme';

const meta = {
    title: 'atoms/EditButton',
    component: EditButton,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
    decorators: [
        (Story) => {
            return (
                <ThemeProvider theme={theme}>
                    <Story />
                </ThemeProvider>
            )
        }
    ]
} satisfies Meta<typeof EditButton>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        label: 'Default',
        onClick: () => { }
    }
};

export const Disable: Story = {
    args: {
        label: 'Disable',
        disabled: true,
        onClick: () => { }
    }
};
