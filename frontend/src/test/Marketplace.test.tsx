import { axe, render, screen } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import Marketplace from '../pages/Marketplace/Marketplace';

describe('Marketplace', () => {
    axe([<Marketplace key="1" />]);

    it('renders correctly', () => {
        render(<Marketplace />);
    });

    it('renders the page title and subtitle', () => {
        render(<Marketplace />);

        expect(screen.getByRole('heading', { name: /marketplace/i })).toBeInTheDocument();
        expect(screen.getByText(/check all sales:/i)).toBeInTheDocument();
    });

    it('renders the marketplace items by default', () => {
        render(<Marketplace />);

        expect(screen.getByText('Book 1')).toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.getByText('Book 3')).toBeInTheDocument();
        expect(screen.getByText('Book 4')).toBeInTheDocument();
    });

    it('filters items when direct sale checkbox is selected', async () => {
        const user = userEvent.setup();
        render(<Marketplace />);

        await user.click(screen.getByRole('checkbox', { name: /direct sale/i }));

        expect(screen.getByText('Book 1')).toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.queryByText('Book 3')).not.toBeInTheDocument();
        expect(screen.queryByText('Book 4')).not.toBeInTheDocument();
    });

    it('filters items by search text', async () => {
        const user = userEvent.setup();
        render(<Marketplace />);

        await user.type(
            screen.getByPlaceholderText(/search by item, genre, type or price/i),
            'Book 6'
        );

        expect(screen.queryByText('Book 1')).not.toBeInTheDocument();
        expect(screen.getByText('Book 6')).toBeInTheDocument();
    });
});
