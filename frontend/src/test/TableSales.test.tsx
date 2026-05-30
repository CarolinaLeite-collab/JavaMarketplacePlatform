import { render, screen } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import { TableSales } from '../components/tablesales/TableSales';

describe('TableSales', () => {
    it('renders table headers', () => {
        render(<TableSales />);

        expect(screen.getByRole('columnheader', { name: /title/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /genre/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /expires in/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /price/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /type of sale/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /delete/i })).toBeInTheDocument();
    });

    it('renders seeded sales rows', () => {
        render(<TableSales />);

        expect(screen.getByText('1984')).toBeInTheDocument();
        expect(screen.getByText('The Catcher in the Rye')).toBeInTheDocument();
        expect(screen.getByText('Pride and Prejudice')).toBeInTheDocument();
        expect(screen.getAllByText(/direct sale/i)).toHaveLength(3);
    });

    it('filters rows by search text', async () => {
        const user = userEvent.setup();
        render(<TableSales />);

        const searchInput = screen.getByPlaceholderText(/search by title, genre, price or sale type/i);
        await user.type(searchInput, '1984');

        expect(screen.getByText('1984')).toBeInTheDocument();
        expect(screen.queryByText('The Catcher in the Rye')).not.toBeInTheDocument();
        expect(screen.queryByText('Pride and Prejudice')).not.toBeInTheDocument();
    });

    it('shows empty state when search has no matches', async () => {
        const user = userEvent.setup();
        render(<TableSales />);

        const searchInput = screen.getByPlaceholderText(/search by title, genre, price or sale type/i);
        await user.type(searchInput, 'nonexistent');

        expect(screen.getByText(/nothing found/i)).toBeInTheDocument();
    });
});