import { render, screen } from '@/test-utils';
import { MarketPlaceTable } from '../components/marketPlaceTable/MarketPlaceTable';

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
            {...overrides}
        />
    );
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

    it('filters items by selected genre', () => {
        renderTable({ selectedGenre: 'ROMANCE' });

        expect(screen.queryByText('Book 1')).not.toBeInTheDocument();
        expect(screen.getByText('Book 2')).toBeInTheDocument();
        expect(screen.queryByText('Book 3')).not.toBeInTheDocument();
    });
});
