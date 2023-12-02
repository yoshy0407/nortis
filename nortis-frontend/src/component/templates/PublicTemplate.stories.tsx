import type { Meta, StoryObj } from '@storybook/react';
import PublicTemplate from './PublicTemplate';
import { Card, CardContent } from '@mui/material';

const meta = {
    title: 'templates/PublicTemplate',
    component: PublicTemplate,
    tags: ['autodocs'],
    parameters: {
        layout: 'fullscreen',
    },
} satisfies Meta<typeof PublicTemplate>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Normal: Story = {
    args: {
        children: (
            <Card>
                <CardContent>Test</CardContent>
            </Card>
        )
    }
};
