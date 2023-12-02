import type { Meta, StoryObj } from '@storybook/react';
import PositiveButton from './PositiveButton';
import { Send } from '@mui/icons-material';

const meta = {
    title: 'atoms/common/PositiveButton',
    component: PositiveButton,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof PositiveButton>;

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

export const StartIcon: Story = {
    args: {
        label: 'StartIcon',
        startIcon: (<Send />),
        onClick: () => { }
    }
};

export const EndIcon: Story = {
    args: {
        label: 'EndIcon',
        endIcon: (<Send />),
        onClick: () => { }
    }
};
