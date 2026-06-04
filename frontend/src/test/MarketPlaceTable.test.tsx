import { render, screen, within } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import {MarketPlaceTable} from '../components/marketPlaceTable/MarketPlaceTable';

const items = [
    { id: '1', item: 'Book 1', genreId: 'HORROR', genreName: 'Horror', type: 'Direct Sale', price: '10 EUR' },
    { id: '2', item: 'Book 2', genreId: 'ROMANCE', genreName: 'Romance', type: 'Auction', price: '20 EUR' },
    { id: '3', item: 'Book 3', genreId: 'HORROR', genreName: 'Horror', type: 'Auction', price: '30 EUR' },
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
            selectedGenre="all"
            onGenreChange={() => {}}
            showDirectSales={false}
            showAuctions={false}
            onShowDirectSalesChange={() => {}}
            onShowAuctionsChange={() => {}}
            canSeePrice
            {...overrides}
        />
    );
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

    it('filters items by selected genre', () => {
        renderTable({ selectedGenre: 'ROMANCE' });

        expect(screen.queryByText('Book 1')).not.toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.queryByText('Book 3')).not.toBeInTheDocument();
    });

    it('shows the empty state when no item matches the filters', () => {
        renderTable({ selectedGenre: 'ROMANCE', showDirectSales: true, showAuctions: false });

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
});
