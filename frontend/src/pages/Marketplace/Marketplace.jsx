import { useState } from 'react';
import { DefaultLayout } from '../../components/layout/DefaultLayout.tsx';
import { MarketPlaceTable } from '../../components/marketPlaceTable/MarketPlaceTable.jsx';

const genres = [
    { value: 'all', label: 'All genres' },
    { value: 'HORROR', label: 'Horror' },
    { value: 'ROMANCE', label: 'Romance' },
    { value: 'SCIENCE-FICTION', label: 'Science Fiction' },
];

const items = [
    {
        id: 'direct-sale-1',
        item: 'Book 1',
        genreId: 'HORROR',
        genreName: 'Horror',
        type: 'Direct Sale',
        price: '10 EUR',
    },
    {
        id: 'direct-sale-2',
        item: 'Book 2',
        genreId: 'ROMANCE',
        genreName: 'Romance',
        type: 'Direct Sale',
        price: '18 EUR',
    },
    {
        id: 'auction-1',
        item: 'Book 3',
        genreId: 'HORROR',
        genreName: 'Horror',
        type: 'Auction',
        price: '12 EUR',
    },
    {
        id: 'auction-2',
        item: 'Book 4',
        genreId: 'SCIENCE-FICTION',
        genreName: 'Science Fiction',
        type: 'Auction',
        price: '25 EUR',
    },
    {
        id: 'direct-sale-3',
        item: 'Book 5',
        genreId: 'HORROR',
        genreName: 'Horror',
        type: 'Direct Sale',
        price: '16 EUR',
    },
    {
        id: 'auction-3',
        item: 'Book 6',
        genreId: 'ROMANCE',
        genreName: 'Romance',
        type: 'Auction',
        price: '20 EUR',
    },
];

export default function Marketplace() {
    const [selectedGenre, setSelectedGenre] = useState('all');
    const [showDirectSales, setShowDirectSales] = useState(false);
    const [showAuctions, setShowAuctions] = useState(false);

    return (
        <DefaultLayout title="Marketplace" subtitle="CHECK ALL SALES:">
            <MarketPlaceTable
                items={items}
                genres={genres}
                selectedGenre={selectedGenre}
                onGenreChange={setSelectedGenre}
                showDirectSales={showDirectSales}
                showAuctions={showAuctions}
                onShowDirectSalesChange={setShowDirectSales}
                onShowAuctionsChange={setShowAuctions}
            />
        </DefaultLayout>
    );
}
