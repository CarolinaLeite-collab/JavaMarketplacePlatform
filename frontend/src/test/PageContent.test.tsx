import { render, screen } from '@/test-utils';
import { PageContent } from '../components/layout/PageContent';

describe('PageContent', () => {
    it('renders children', () => {
        render(
            <PageContent>
                <p>Body content</p>
            </PageContent>
        );

        expect(screen.getByText('Body content')).toBeInTheDocument();
    });

    it('renders the title when title is provided', () => {
        render(
            <PageContent title="Settings">
                <p>Body content</p>
            </PageContent>
        );

        expect(
            screen.getByRole('heading', { name: 'Settings', level: 1 })
        ).toBeInTheDocument();
    });

    it('does not render a title when title is not provided', () => {
        render(
            <PageContent>
                <p>Body content</p>
            </PageContent>
        );

        expect(screen.queryByRole('heading', { level: 1 })).not.toBeInTheDocument();
    });
});