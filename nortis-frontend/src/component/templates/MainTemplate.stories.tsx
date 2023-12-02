import type { Meta, StoryObj } from '@storybook/react';
import MainTemplate from './MainTemplate';

const meta = {
    title: 'templates/MainTemplate',
    component: MainTemplate,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof MainTemplate>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Normal: Story = {
    args: {
        children: (
            <div>Text</div>
        )
    }
};
