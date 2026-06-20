import { render, screen, waitFor } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import AppContext from '../context/AppContext';
import { useUser } from '../context/UserContext';
import Marketplace from '../pages/Marketplace/Marketplace';
import { apiClient } from '../services/apiClient';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getDirectSales: vi.fn(),
        getByHref: vi.fn(),
        getGenres: vi.fn(),
        getItemById: vi.fn(),
        getAuctions: vi.fn(),
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
        seller: 'pedro@aeiou.com',
        _links: {
            self: { href: 'http://localhost:8081/directSales/DS-001' },
        },
    },
    {
        directSaleId: 'DS-002',
        itemsId: ['ITEM-002'],
        priceValue: 18,
        priceCurrency: 'EUR',
        seller: 'ana@aeiou.com',
        _links: {
            self: { href: 'http://localhost:8081/directSales/DS-002' },
        },
    },
];

const auctions = [
    {
        auctionId: 'AU-001',
        itemIds: ['ITEM-003'],
        startingPrice: 15,
        priceCurrency: 'EUR',
        seller: 'rui@aeiou.com',
        _links: {
            self: { href: 'http://localhost:8081/auctions/AU-001' },
        },
    },
];

const genres = [
    { genreId: 'HORROR', genreName: 'Horror' },
    { genreId: 'ROMANCE', genreName: 'Romance' },
    { genreId: 'SCIFI', genreName: 'Science Fiction' },
];

const itemDetails: Record<string, any> = {
    'ITEM-001': {
        itemId: 'ITEM-001',
        title: 'Book 1',
        genreName: 'Horror',
        authorName: 'Author 1',
        condition: 'GOOD',
        picture: 'https://example.com/book1.jpg',
    },
    'ITEM-002': {
        itemId: 'ITEM-002',
        title: 'Book 2',
        genreName: 'Romance',
        authorName: 'Author 2',
        condition: 'FAIR',
        picture: 'https://example.com/book2.jpg',
    },
    'ITEM-003': {
        itemId: 'ITEM-003',
        title: 'Book 3',
        genreName: 'Science Fiction',
        authorName: 'Author 3',
        condition: 'GOOD',
        picture: 'https://example.com/book3.jpg',
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
        </AppContext.Provider>,
    );
}

describe('Marketplace', () => {
    beforeEach(() => {
        vi.clearAllMocks();

        vi.mocked(useUser).mockReturnValue({
            currentUser: 'pedro@aeiou.com',
            toggleUser: vi.fn(),
        });

        vi.mocked(apiClient.getDirectSales).mockResolvedValue(directSales);
        vi.mocked(apiClient.getByHref).mockResolvedValue(directSales);
        vi.mocked(apiClient.getGenres).mockResolvedValue(genres);
        vi.mocked(apiClient.getItemById).mockImplementation(async (itemId: string) => itemDetails[itemId]);
        vi.mocked(apiClient.getAuctions).mockResolvedValue(auctions);
    });

    it('renders marketplace items after loading', async () => {
        renderMarketplace();

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.getByText('Book 3')).toBeInTheDocument();
    });

    it('renders the page title and subtitle', async () => {
        renderMarketplace();

        expect(screen.getByRole('heading', { name: /marketplace/i })).toBeInTheDocument();
        expect(screen.getByText(/check all sales:/i)).toBeInTheDocument();
        expect(await screen.findByText('Book 1')).toBeInTheDocument();
    });

    it('loads marketplace data from backend clients', async () => {
        renderMarketplace();

        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        expect(apiClient.getDirectSales).toHaveBeenCalled();
        expect(apiClient.getAuctions).toHaveBeenCalled();
        expect(apiClient.getGenres).toHaveBeenCalled();
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-001');
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-002');
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-003');
    });

    it('shows loading state while marketplace data is being fetched', async () => {
        let resolveDirectSales: (value: typeof directSales) => void;

        vi.mocked(apiClient.getDirectSales).mockReturnValue(
            new Promise((resolve) => {
                resolveDirectSales = resolve;
            }),
        );

        renderMarketplace();

        expect(screen.getByText(/loading marketplace/i)).toBeInTheDocument();

        resolveDirectSales!(directSales);

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
    });

    it('filters items by search text', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        await user.type(
            screen.getByPlaceholderText(/search by item, genre, type or price/i),
            'Book 2',
        );

        expect(screen.queryByText('Book 1')).not.toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
    });

    it('shows an error message when marketplace data fails to load', async () => {
        vi.mocked(apiClient.getDirectSales).mockRejectedValueOnce(new Error('500'));

        renderMarketplace();

        expect(await screen.findByText(/could not load marketplace/i)).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.queryByText(/loading marketplace/i)).not.toBeInTheDocument();
        });
    });

    it('keeps valid items visible when one item lookup returns 404', async () => {
        vi.mocked(apiClient.getItemById).mockImplementation(async (itemId: string) => {
            if (itemId === 'ITEM-002') {
                throw new Error('404');
            }
            return itemDetails[itemId];
        });

        renderMarketplace();

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
        expect(screen.getByText('Book 3')).toBeInTheDocument();
        expect(screen.queryByText('Book 2')).not.toBeInTheDocument();
        expect(screen.queryByText(/could not load marketplace/i)).not.toBeInTheDocument();
    });

    it('shows an error message when genres fail to load', async () => {
        vi.mocked(apiClient.getGenres).mockRejectedValueOnce(new Error('500'));

        renderMarketplace();

        expect(await screen.findByText(/could not load marketplace/i)).toBeInTheDocument();
    });

    it('shows an error message when an item lookup fails with a non-404 error', async () => {
        vi.mocked(apiClient.getItemById).mockImplementation(async (itemId: string) => {
            if (itemId === 'ITEM-002') {
                throw new Error('500');
            }
            return itemDetails[itemId];
        });

        renderMarketplace();

        expect(await screen.findByText(/could not load marketplace/i)).toBeInTheDocument();
    });

    it('uses the guest marketplace feed and hides prices for guest users (table)', async () => {
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

        // table: no price column, no concrete price text
        expect(screen.queryByRole('columnheader', { name: /price/i })).not.toBeInTheDocument();
        expect(screen.queryByText('10 EUR')).not.toBeInTheDocument();
        expect(screen.queryByText('15 EUR')).not.toBeInTheDocument();
    });

    it('opens details modal when an item is clicked', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        const item = await screen.findByText('Book 1');
        await user.click(item);

        expect(await screen.findByText('Sold by: pedro')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /see more/i })).toBeInTheDocument();
    });

    it('shows price in details modal for logged-in users', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        const item = await screen.findByText('Book 1');
        await user.click(item);

        // From directSales: 10 EUR should be visible for logged-in user
        expect(await screen.findByText('10 EUR')).toBeInTheDocument();
    });

    it('hides price in details modal for guest users', async () => {
        const user = userEvent.setup();

        vi.mocked(useUser).mockReturnValue({
            currentUser: 'guest@aeiou.com',
            toggleUser: vi.fn(),
        });

        renderMarketplace({
            appState: {
                directSalesWithoutPriceHref: '/direct-sales/public',
            },
        });

        const item = await screen.findByText('Book 1');
        await user.click(item);

        // The modal should not show a concrete price for guests
        expect(
            screen.getByText(/register or log in to see price/i),
        ).toBeInTheDocument();
        expect(screen.queryByText('10 EUR')).not.toBeInTheDocument();
    });

    it('navigates to auction details when see more is clicked for auction item', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        const auctionItem = await screen.findByText('Book 3');
        await user.click(auctionItem);

        await user.click(screen.getByRole('button', { name: /see more/i }));

        expect(mockNavigate).toHaveBeenCalledWith('/auctions/AU-001', {
            state: { selfHref: 'http://localhost:8081/auctions/AU-001' },
        });
    });

    it('navigates to direct sale details when see more is clicked for direct sale item', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        const directSaleItem = await screen.findByText('Book 1');
        await user.click(directSaleItem);

        await user.click(screen.getByRole('button', { name: /see more/i }));

        expect(mockNavigate).toHaveBeenCalledWith('/directSales/DS-001', {
            state: { selfHref: 'http://localhost:8081/directSales/DS-001' },
        });
    });
});