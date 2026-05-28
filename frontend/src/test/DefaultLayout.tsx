import { axe, render, screen } from '@/test-utils';
import { DefaultLayout } from '../components/layout/DefaultLayout';

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
});