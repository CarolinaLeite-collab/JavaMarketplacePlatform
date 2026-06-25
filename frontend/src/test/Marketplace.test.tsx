import { render, screen, waitFor, within } from '@/test-utils';
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
        getRootOptions: vi.fn(),
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
        publisherName: 'Publisher A',
        condition: 'GOOD',
        picture: 'https://example.com/book1.jpg',
    },
    'ITEM-002': {
        itemId: 'ITEM-002',
        title: 'Book 2',
        genreName: 'Romance',
        authorName: 'Author 2',
        publisherName: 'Publisher B',
        condition: 'FAIR',
        picture: 'https://example.com/book2.jpg',
    },
    'ITEM-003': {
        itemId: 'ITEM-003',
        title: 'Book 3',
        genreName: 'Science Fiction',
        authorName: 'Author 1',
        publisherName: 'Publisher A',
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
                        activeDirectSalesHref: 'http://localhost:8081/marketplace/direct-sales',
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

        vi.mocked(apiClient.getRootOptions).mockResolvedValue({
            _links: {
                'active-direct-sales': {
                    href: 'http://localhost:8081/marketplace/direct-sales',
                },
                'direct-sales-without-price': {
                    href: 'http://localhost:8081/marketplace/public-direct-sales',
                },
            },
        });
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

    it('uses the discovered active direct sales feed for logged-in users', async () => {
        renderMarketplace({
            appState: {
                activeDirectSalesHref: 'http://localhost:8081/marketplace/direct-sales',
            },
        });

        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        expect(apiClient.getByHref).toHaveBeenCalledWith('http://localhost:8081/marketplace/direct-sales');
        expect(apiClient.getRootOptions).not.toHaveBeenCalled();
        expect(apiClient.getAuctions).toHaveBeenCalled();
        expect(apiClient.getGenres).toHaveBeenCalled();
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-001');
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-002');
        expect(apiClient.getItemById).toHaveBeenCalledWith('ITEM-003');
    });

    it('discovers the active direct sales feed from root options when the app state has no link', async () => {
        renderMarketplace({
            appState: {
                activeDirectSalesHref: null,
            },
        });

        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        expect(apiClient.getRootOptions).toHaveBeenCalled();
        expect(apiClient.getByHref).toHaveBeenCalledWith('http://localhost:8081/marketplace/direct-sales');
    });

    it('shows loading state while marketplace data is being fetched', async () => {
        let resolveDirectSales: (value: typeof directSales) => void;

        vi.mocked(apiClient.getByHref).mockReturnValue(
            new Promise((resolve) => {
                resolveDirectSales = resolve;
            }),
        );

        renderMarketplace();

        expect(screen.getByText(/loading marketplace/i)).toBeInTheDocument();

        resolveDirectSales!(directSales);

        expect(await screen.findByText('Book 1')).toBeInTheDocument();
    });

    it('filters items by item name', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        await user.type(
            screen.getByPlaceholderText(/search by item name/i),
            'Book 2',
        );

        const table = screen.getByRole('table');
        expect(within(table).queryByText('Book 1')).not.toBeInTheDocument();
        expect(within(table).getByText('Book 2')).toBeInTheDocument();
    });

    it('shows an error message when marketplace data fails to load', async () => {
        vi.mocked(apiClient.getByHref).mockRejectedValueOnce(new Error('500'));

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
        const table = screen.getByRole('table');
        expect(within(table).getByText('Book 3')).toBeInTheDocument();
        expect(within(table).queryByText('Book 2')).not.toBeInTheDocument();
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
        expect(apiClient.getRootOptions).not.toHaveBeenCalled();
        expect(screen.queryByRole('columnheader', { name: /price/i })).not.toBeInTheDocument();
        expect(screen.queryByText('10 EUR')).not.toBeInTheDocument();
        expect(screen.queryByText('15 EUR')).not.toBeInTheDocument();
    });

    it('opens details modal when an item is clicked', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        const item = await screen.findByText('Book 1');
        await user.click(item);

        const dialog = await screen.findByRole('dialog');
        expect(dialog).toHaveTextContent(/book 1/i);
        expect(dialog).toHaveTextContent(/author:\s*author 1/i);
        expect(dialog).toHaveTextContent(/genre:\s*horror/i);
        expect(within(dialog).getByRole('button', { name: /see more/i })).toBeInTheDocument();
    });

    it('shows price in details modal for logged-in users', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        const item = await screen.findByText('Book 1');
        await user.click(item);

        const dialog = await screen.findByRole('dialog');
        expect(dialog).toHaveTextContent(/10\.00 eur/i);
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

        const dialog = await screen.findByRole('dialog');
        expect(dialog).toHaveTextContent(/register or log in to see price/i);
        expect(dialog).not.toHaveTextContent(/10 eur/i);
    });

    it('navigates to auction details when see more is clicked for auction item', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        const auctionItem = await screen.findByText('Book 3');
        await user.click(auctionItem);

        const dialog = await screen.findByRole('dialog');
        await user.click(within(dialog).getByRole('button', { name: /see more/i }));

        expect(mockNavigate).toHaveBeenCalledWith('/auctions/AU-001', {
            state: { selfHref: 'http://localhost:8081/auctions/AU-001' },
        });
    });

    it('navigates to direct sale details when see more is clicked for direct sale item', async () => {
        const user = userEvent.setup();

        renderMarketplace();

        const directSaleItem = await screen.findByText('Book 1');
        await user.click(directSaleItem);

        const dialog = await screen.findByRole('dialog');
        await user.click(within(dialog).getByRole('button', { name: /see more/i }));

        expect(mockNavigate).toHaveBeenCalledWith('/directSales/DS-001', {
            state: { selfHref: 'http://localhost:8081/directSales/DS-001' },
        });
    });

    describe('filter by author', () => {
        it('shows only items from selected author', async () => {
            const user = userEvent.setup();
            renderMarketplace();
            expect(await screen.findByText('Book 1')).toBeInTheDocument();

            await user.click(screen.getByPlaceholderText('All authors'));
            await user.click(await screen.findByRole('option', { name: 'Author 2', hidden: true }));

            const table = screen.getByRole('table');
            expect(within(table).queryByText('Book 1')).not.toBeInTheDocument();
            expect(within(table).getByText('Book 2')).toBeInTheDocument();
            expect(within(table).queryByText('Book 3')).not.toBeInTheDocument();
        });

        it('shows items from multiple selected authors', async () => {
            const user = userEvent.setup();
            renderMarketplace();
            expect(await screen.findByText('Book 1')).toBeInTheDocument();

            await user.click(screen.getByPlaceholderText('All authors'));
            await user.click(await screen.findByRole('option', { name: 'Author 1', hidden: true }));
            await user.click(await screen.findByRole('option', { name: 'Author 2', hidden: true }));

            const table = screen.getByRole('table');
            expect(within(table).getByText('Book 1')).toBeInTheDocument();
            expect(within(table).getByText('Book 2')).toBeInTheDocument();
            expect(within(table).getByText('Book 3')).toBeInTheDocument();
        });
    });

    describe('filter by publication', () => {
        it('shows only the selected publication', async () => {
            const user = userEvent.setup();
            renderMarketplace();
            expect(await screen.findByText('Book 1')).toBeInTheDocument();

            await user.click(screen.getByPlaceholderText('All publications'));
            await user.click(await screen.findByRole('option', { name: 'Book 1', hidden: true }));

            const table = screen.getByRole('table');
            expect(within(table).getByText('Book 1')).toBeInTheDocument();
            expect(within(table).queryByText('Book 2')).not.toBeInTheDocument();
            expect(within(table).queryByText('Book 3')).not.toBeInTheDocument();
        });
    });

    describe('filter by publisher', () => {
        it('shows only items from selected publisher', async () => {
            const user = userEvent.setup();
            renderMarketplace();
            expect(await screen.findByText('Book 1')).toBeInTheDocument();

            await user.click(screen.getByPlaceholderText('All publishers'));
            await user.click(await screen.findByRole('option', { name: 'Publisher B', hidden: true }));

            const table = screen.getByRole('table');
            expect(within(table).queryByText('Book 1')).not.toBeInTheDocument();
            expect(within(table).getByText('Book 2')).toBeInTheDocument();
            expect(within(table).queryByText('Book 3')).not.toBeInTheDocument();
        });

        it('shows items from multiple selected publishers', async () => {
            const user = userEvent.setup();
            renderMarketplace();
            expect(await screen.findByText('Book 1')).toBeInTheDocument();

            await user.click(screen.getByPlaceholderText('All publishers'));
            await user.click(await screen.findByRole('option', { name: 'Publisher A', hidden: true }));
            await user.click(await screen.findByRole('option', { name: 'Publisher B', hidden: true }));

            const table = screen.getByRole('table');
            expect(within(table).getByText('Book 1')).toBeInTheDocument();
            expect(within(table).getByText('Book 2')).toBeInTheDocument();
            expect(within(table).getByText('Book 3')).toBeInTheDocument();
        });
    });

    describe('filter by genre', () => {
        it('shows only items of the selected genre', async () => {
            const user = userEvent.setup();
            renderMarketplace();
            expect(await screen.findByText('Book 1')).toBeInTheDocument();

            await user.click(screen.getByPlaceholderText('All genres'));
            await user.click(await screen.findByRole('option', { name: 'Horror', hidden: true }));

            const table = screen.getByRole('table');
            expect(within(table).getByText('Book 1')).toBeInTheDocument();
            expect(within(table).queryByText('Book 2')).not.toBeInTheDocument();
            expect(within(table).queryByText('Book 3')).not.toBeInTheDocument();
        });
    });

    it('combined author and publisher filters show only matching items', async () => {
        const user = userEvent.setup();
        renderMarketplace();
        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        await user.click(screen.getByPlaceholderText('All authors'));
        await user.click(await screen.findByRole('option', { name: 'Author 1', hidden: true }));

        await user.click(screen.getByPlaceholderText('All publishers'));
        await user.click(await screen.findByRole('option', { name: 'Publisher A', hidden: true }));

        const table = screen.getByRole('table');
        expect(within(table).getByText('Book 1')).toBeInTheDocument();
        expect(within(table).queryByText('Book 2')).not.toBeInTheDocument();
        expect(within(table).getByText('Book 3')).toBeInTheDocument();
    });

    it('combined author and genre filters show only matching items', async () => {
        const user = userEvent.setup();
        renderMarketplace();
        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        await user.click(screen.getByPlaceholderText('All authors'));
        await user.click(await screen.findByRole('option', { name: 'Author 1', hidden: true }));

        await user.click(screen.getByPlaceholderText('All genres'));
        await user.click(await screen.findByRole('option', { name: 'Horror', hidden: true }));

        const table = screen.getByRole('table');
        expect(within(table).getByText('Book 1')).toBeInTheDocument();
        expect(within(table).queryByText('Book 2')).not.toBeInTheDocument();
        expect(within(table).queryByText('Book 3')).not.toBeInTheDocument();
    });

    it('combined author and publication filters show only matching items', async () => {
        const user = userEvent.setup();
        renderMarketplace();
        expect(await screen.findByText('Book 1')).toBeInTheDocument();

        await user.click(screen.getByPlaceholderText('All authors'));
        await user.click(await screen.findByRole('option', { name: 'Author 1', hidden: true }));

        await user.click(screen.getByPlaceholderText('All publications'));
        await user.click(await screen.findByRole('option', { name: 'Book 3', hidden: true }));

        const table = screen.getByRole('table');
        expect(within(table).queryByText('Book 1')).not.toBeInTheDocument();
        expect(within(table).queryByText('Book 2')).not.toBeInTheDocument();
        expect(within(table).getByText('Book 3')).toBeInTheDocument();
    });
});
