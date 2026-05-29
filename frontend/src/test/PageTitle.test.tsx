import { render, screen } from '@/test-utils';
import { PageTitle } from '../components/layout/PageTitle';

describe('PageTitle', () => {
    it('renders children as a level 1 heading', () => {
        render(<PageTitle>Profile</PageTitle>);

        expect(
            screen.getByRole('heading', { name: 'Profile', level: 1 })
        ).toBeInTheDocument();
    });

    it('renders the provided text content', () => {
        render(<PageTitle>My page title</PageTitle>);

        expect(screen.getByText('My page title')).toBeInTheDocument();
    });

    it('renders subtitle when provided', () => {
        render(<PageTitle subtitle="check out your lists:">My page title</PageTitle>);

        expect(screen.getByText('check out your lists:')).toBeInTheDocument();
    });

    it('does not render subtitle when not provided', () => {
        render(<PageTitle>My page title</PageTitle>);

        expect(screen.queryByText('check out your lists:')).not.toBeInTheDocument();
    });
});