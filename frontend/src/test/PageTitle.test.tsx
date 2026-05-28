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
});