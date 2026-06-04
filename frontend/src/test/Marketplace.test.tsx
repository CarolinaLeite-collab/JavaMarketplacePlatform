import { render, screen, waitFor } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import Marketplace from '../pages/Marketplace/Marketplace';
import { apiClient } from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getDirectSales: vi.fn(),
        getGenres: vi.fn(),
        getItemById: vi.fn(),
    },
}));

const directSales = [
    {
        directSaleId: 'DS-001',
        itemsId: ['ITEM-001'],
        priceValue: 10,
        priceCurrency: 'EUR',
    },
    {
        directSaleId: 'DS-002',
        itemsId: ['ITEM-002'],
        priceValue: 18,
        priceCurrency: 'EUR',
    },
];

const genres = [
    { genreId: 'HORROR', genreName: 'Horror' },
    { genreId: 'ROMANCE', genreName: 'Romance' },
];

const itemDetails = {
    'ITEM-001': {
        itemId: 'ITEM-001',
        title: 'Book 1',
        genreName: 'Horror',
    },
    'ITEM-002': {
        itemId: 'ITEM-002',
        title: 'Book 2',
        genreName: 'Romance',
    },
};

describe('Marketplace', () => {
    beforeEach(() => {
        apiClient.getDirectSales.mockResolvedValue(directSales);
        apiClient.getGenres.mockResolvedValue(genres);
        apiClient.getItemById.mockImplementation(async (itemId) => itemDetails[itemId]);
    });

    it('passes accessibility checks', async () => {
        render(<Marketplace />);

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
    });

    it('renders the page title and subtitle', async () => {
        render(<Marketplace />);

        expect(screen.getByRole('heading', { name: /marketplace/i })).toBeInTheDocument();
        expect(screen.getByText(/check all sales:/i)).toBeInTheDocument();
        expect(await screen.findByText('Book 1')).toBeInTheDocument();
    });

    it('loads direct sales and item details from the backend client', async () => {
        render(<Marketplace />);

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();

        expect(apiClient.getDirectSales).toHaveBeenCalled();
        expect(apiClient.getGenres).toHaveBeenCalled();
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-001');
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-002');
    });

    it('filters items when direct sale checkbox is selected', async () => {
        const user = userEvent.setup();
        render(<Marketplace />);

        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        await user.click(screen.getByRole('checkbox', { name: /direct sale/i }));

        expect(screen.getByText('Book 1')).toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
    });

    it('filters items by search text', async () => {
        const user = userEvent.setup();
        render(<Marketplace />);

        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        await user.type(
            screen.getByPlaceholderText(/search by item, genre, type or price/i),
            'Book 2'
        );

        expect(screen.queryByText('Book 1')).not.toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
    });

    it('shows an error message when marketplace data fails to load', async () => {
        apiClient.getDirectSales.mockRejectedValueOnce(new Error('500'));

        render(<Marketplace />);

        expect(await screen.findByText(/could not load marketplace/i)).toBeInTheDocument();
        await waitFor(() => {
            expect(screen.queryByText(/loading marketplace/i)).not.toBeInTheDocument();
        });
    });

    it('keeps valid direct sales visible when one item lookup returns 404', async () => {
        apiClient.getItemById.mockImplementation(async (itemId) => {
            if (itemId === 'ITEM-002') {
                throw new Error('404');
            }

            return itemDetails[itemId];
        });

        render(<Marketplace />);

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
        expect(screen.queryByText('Book 2')).not.toBeInTheDocument();
        expect(screen.queryByText(/could not load marketplace/i)).not.toBeInTheDocument();
    });

    it('shows an error message when genres fail to load', async () => {
        apiClient.getGenres.mockRejectedValueOnce(new Error('500'));

        render(<Marketplace />);

        expect(await screen.findByText(/could not load marketplace/i)).toBeInTheDocument();
    });

    it('shows an error message when an item lookup fails with a non-404 error', async () => {
        apiClient.getItemById.mockImplementation(async (itemId) => {
            if (itemId === 'ITEM-002') {
                throw new Error('500');
            }

            return itemDetails[itemId];
        });

        render(<Marketplace />);

        expect(await screen.findByText(/could not load marketplace/i)).toBeInTheDocument();
    });
});
