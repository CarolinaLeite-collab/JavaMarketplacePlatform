import { axe, render, screen } from '@/test-utils';
import { DefaultLayout } from '../components/layout/DefaultLayout';
import * as mantine from '@mantine/core';

describe('DefaultLayout', () => {
    axe([<DefaultLayout key="1"><p>Test content</p></DefaultLayout>]);

    it('renders correctly', () => {
        render(<DefaultLayout><p>Test content</p></DefaultLayout>);
    });

    it('renders children', () => {
        render(
            <DefaultLayout>
                <p>Test content</p>
            </DefaultLayout>
        );
        expect(screen.getByText('Test content')).toBeInTheDocument();
    });

    it('renders the header', () => {
        render(<DefaultLayout><p>Test content</p></DefaultLayout>);
        expect(screen.getByRole('banner')).toBeInTheDocument();
    });

    it('renders the footer', () => {
        render(<DefaultLayout><p>Test content</p></DefaultLayout>);
        expect(screen.getByText('© 2026 Cooperativa de Código da Asprela')).toBeInTheDocument();
    });
    it('renders the page title when title is provided', () => {
        render(
            <DefaultLayout title="Dashboard">
                <p>Test content</p>
            </DefaultLayout>
        );

        expect(
            screen.getByRole('heading', { name: 'Dashboard', level: 1 })
        ).toBeInTheDocument();
    });

    it('uses light background when computed color scheme is light', () => {
        vi.spyOn(mantine, 'useComputedColorScheme').mockReturnValue('light');

        render(
            <DefaultLayout title="Dashboard">
                <p>Test content</p>
            </DefaultLayout>
        );

        expect(screen.getByText('Test content').parentElement?.parentElement).toHaveStyle({
            backgroundColor: 'var(--mantine-color-gray-1)',
        });
    });

    it('uses dark background when computed color scheme is dark', () => {
        vi.spyOn(mantine, 'useComputedColorScheme').mockReturnValue('dark');

        render(
            <DefaultLayout title="Dashboard">
                <p>Test content</p>
            </DefaultLayout>
        );

        expect(screen.getByText('Test content').parentElement?.parentElement).toHaveStyle({
            backgroundColor: 'var(--mantine-color-dark-6)',
        });
    });
});