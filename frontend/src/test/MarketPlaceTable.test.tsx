import { render, screen, within } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import {MarketPlaceTable} from '../components/marketPlaceTable/MarketPlaceTable';

const items = [
    { id: '1', item: 'Book 1', genreId: 'HORROR', genreName: 'Horror', type: 'Direct Sale', price: '10.00 EUR', priceValue: 10, cover: 'https://example.com/book-1.jpg' },
    { id: '2', item: 'Book 2', genreId: 'ROMANCE', genreName: 'Romance', type: 'Auction', price: '3.50 EUR', priceValue: 3.5, cover: 'https://example.com/book-2.jpg' },
    { id: '3', item: 'Book 3', genreId: 'HORROR', genreName: 'Horror', type: 'Auction', price: '20.00 EUR', priceValue: 20, cover: 'https://example.com/book-3.jpg' },
];

const genres = [
    { value: 'all', label: 'All genres' },
    { value: 'HORROR', label: 'Horror' },
    { value: 'ROMANCE', label: 'Romance' },
];

function renderTable(overrides = {}) {
    return render(
        <MarketPlaceTable
            items={items}
            genres={genres}
            showDirectSales={false}
            showAuctions={false}
            onShowDirectSalesChange={() => {}}
            onShowAuctionsChange={() => {}}
            canSeePrice
            {...overrides}
        />
    );
}
function getRenderedPrices() {
    return screen
        .getAllByRole('row')
        .slice(1)
        .map((row) => within(row).getAllByRole('cell')[3].textContent);
}

function getRenderedItemNames() {
    return screen
        .getAllByRole('row')
        .slice(1)
        .map((row) => within(row).getAllByRole('cell')[0].textContent);
}

describe('MarketPlaceTable', () => {
    it('renders table headers and all items by default', () => {
        renderTable();

        expect(screen.getByRole('columnheader', { name: /item/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /genre/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /type/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /price/i })).toBeInTheDocument();
        expect(screen.getByText('Book 1')).toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.getByText('Book 3')).toBeInTheDocument();
        expect(screen.getByAltText('Cover of Book 1')).toBeInTheDocument();
    });

    it('shows only direct sales when only direct sale is selected', () => {
        renderTable({ showDirectSales: true, showAuctions: false });

        expect(screen.getByText('Book 1')).toBeInTheDocument();
        expect(screen.queryByText('Book 2')).not.toBeInTheDocument();
        expect(screen.queryByText('Book 3')).not.toBeInTheDocument();
    });

    it('shows only auctions when only auction is selected', () => {
        renderTable({ showDirectSales: false, showAuctions: true });

        expect(screen.queryByText('Book 1')).not.toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.getByText('Book 3')).toBeInTheDocument();
    });

    it('shows all items when both sale type filters are selected', () => {
        renderTable({ showDirectSales: true, showAuctions: true });

        expect(screen.getByText('Book 1')).toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.getByText('Book 3')).toBeInTheDocument();
    });

    it('filters items by selected genre', async () => {
        const user = userEvent.setup();
        renderTable();

        await user.click(screen.getByPlaceholderText('All genres'));
        await user.click(await screen.findByRole('option', { name: 'Romance', hidden: true }));

        expect(screen.queryByText('Book 1')).not.toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.queryByText('Book 3')).not.toBeInTheDocument();
    });

    it('shows the empty state when no item matches the filters', async () => {
        const user = userEvent.setup();
        renderTable({ showDirectSales: true, showAuctions: false });

        await user.click(screen.getByPlaceholderText('All genres'));
        await user.click(await screen.findByRole('option', { name: 'Romance', hidden: true }));

        expect(screen.getByText(/nothing found/i)).toBeInTheDocument();
        expect(screen.queryByText('Book 1')).not.toBeInTheDocument();
        expect(screen.queryByText('Book 2')).not.toBeInTheDocument();
        expect(screen.queryByText('Book 3')).not.toBeInTheDocument();
    });

    it('filters items by search text', async () => {
        const user = userEvent.setup();
        renderTable();

        await user.type(
            screen.getByPlaceholderText(/search by item, genre, type or price/i),
            'Book 2'
        );

        expect(screen.queryByText('Book 1')).not.toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.queryByText('Book 3')).not.toBeInTheDocument();
    });

    it('sorts items by genre when the header is clicked', async () => {
        const user = userEvent.setup();
        renderTable();

        await user.click(screen.getByRole('button', { name: /genre/i }));

        expect(getRenderedItemNames()).toEqual(['Book 1', 'Book 3', 'Book 2']);
    });

    it('hides the price column when user cannot see prices', () => {
        renderTable({ canSeePrice: false });

        expect(screen.queryByRole('columnheader', { name: /price/i })).not.toBeInTheDocument();
        expect(screen.queryByText('10 EUR')).not.toBeInTheDocument();
    });

    it('renders prices with two decimal places', () => {
        renderTable();

        expect(screen.getByText('10.00 EUR')).toBeInTheDocument();
        expect(screen.getByText('3.50 EUR')).toBeInTheDocument();
        expect(screen.getByText('20.00 EUR')).toBeInTheDocument();
    });

    it('sorts prices in ascending order on first click', async () => {
        const user = userEvent.setup();
        renderTable();

        await user.click(
            screen.getByRole('button', { name: /price/i })
        );

        expect(getRenderedPrices()).toEqual([
            '3.50 EUR',
            '10.00 EUR',
            '20.00 EUR',
        ]);
    });

    it('sorts prices in descending order on second click', async () => {
        const user = userEvent.setup();
        renderTable();

        const priceHeader =
            screen.getByRole('button', { name: /price/i });

        await user.click(priceHeader);
        await user.click(priceHeader);

        expect(getRenderedPrices()).toEqual([
            '20.00 EUR',
            '10.00 EUR',
            '3.50 EUR',
        ]);
    });
});
