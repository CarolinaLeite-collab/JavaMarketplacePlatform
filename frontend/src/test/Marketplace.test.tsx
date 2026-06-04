import {render, screen, waitFor} from '@/test-utils';
import userEvent from '@testing-library/user-event';
import AppContext from '../context/AppContext';
import { useUser } from '../context/UserContext';
import Marketplace from '../pages/Marketplace/Marketplace';
import {apiClient} from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getDirectSales: vi.fn(),
        getByHref: vi.fn(),
        getGenres: vi.fn(),
        getItemById: vi.fn(),
    },
}));

vi.mock('../context/UserContext', async () => {
    const actual = await vi.importActual('../context/UserContext');

    return {
        ...actual,
        useUser: vi.fn(),
    };
});

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

function renderMarketplace({ appState = {} } = {}) {
    return render(
        <AppContext.Provider
            value={{
                state: {
                    app: {
                        myListsHref: null,
                        createListHref: null,
                        genresHref: null,
                        libraryHref: null,
                        directSalesHref: null,
                        directSalesWithoutPriceHref: null,
                        ...appState,
                    },
                    lists: {
                        lists: [],
                        genres: [],
                        error: null,
                    },
                },
                dispatch: vi.fn(),
            }}
        >
            <Marketplace />
        </AppContext.Provider>
    );
}

describe('Marketplace', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'pedro@aeiou.com',
            toggleUser: vi.fn(),
        });
        apiClient.getDirectSales.mockResolvedValue(directSales);
        apiClient.getByHref.mockResolvedValue(directSales);
        apiClient.getGenres.mockResolvedValue(genres);
        apiClient.getItemById.mockImplementation(async (itemId) => itemDetails[itemId]);
    });

    it('renders marketplace items after loading', async () => {
        renderMarketplace();

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
    });

    it('renders the page title and subtitle', async () => {
        renderMarketplace();

        expect(screen.getByRole('heading', { name: /marketplace/i })).toBeInTheDocument();
        expect(screen.getByText(/check all sales:/i)).toBeInTheDocument();
        expect(await screen.findByText('Book 1')).toBeInTheDocument();
    });

    it('loads direct sales and item details from the backend client', async () => {
        renderMarketplace();

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();

        expect(apiClient.getDirectSales).toHaveBeenCalled();
        expect(apiClient.getGenres).toHaveBeenCalled();
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-001');
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-002');
    });

    it('shows the loading state while marketplace data is being fetched', async () => {
        let resolveDirectSales;
        apiClient.getDirectSales.mockReturnValue(
            new Promise((resolve) => {
                resolveDirectSales = resolve;
            })
        );

        renderMarketplace();

        expect(screen.getByText(/loading marketplace/i)).toBeInTheDocument();

        resolveDirectSales(directSales);

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
    });

    it('filters items by search text', async () => {
        const user = userEvent.setup();
        renderMarketplace();

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

        renderMarketplace();

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

        renderMarketplace();

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
        expect(screen.queryByText('Book 2')).not.toBeInTheDocument();
        expect(screen.queryByText(/could not load marketplace/i)).not.toBeInTheDocument();
    });

    it('shows an error message when genres fail to load', async () => {
        apiClient.getGenres.mockRejectedValueOnce(new Error('500'));

        renderMarketplace();

        expect(await screen.findByText(/could not load marketplace/i)).toBeInTheDocument();
    });

    it('shows an error message when an item lookup fails with a non-404 error', async () => {
        apiClient.getItemById.mockImplementation(async (itemId) => {
            if (itemId === 'ITEM-002') {
                throw new Error('500');
            }

            return itemDetails[itemId];
        });

        renderMarketplace();

        expect(await screen.findByText(/could not load marketplace/i)).toBeInTheDocument();
    });

    it('uses the guest marketplace feed and hides prices for guest users', async () => {
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'guest@aeiou.com',
            toggleUser: vi.fn(),
        });

        renderMarketplace({
            appState: {
                directSalesWithoutPriceHref: '/direct-sales/public',
            },
        });

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
        expect(apiClient.getByHref).toHaveBeenCalledWith('/direct-sales/public');
        expect(apiClient.getDirectSales).not.toHaveBeenCalled();
        expect(screen.queryByRole('columnheader', { name: /price/i })).not.toBeInTheDocument();
        expect(screen.queryByText('10 EUR')).not.toBeInTheDocument();
    });
});
