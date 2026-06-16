import {useContext, useEffect, useState} from 'react';
import {Text} from '@mantine/core';
import {DefaultLayout} from '../../components/layout/DefaultLayout.tsx';
import {MarketPlaceTable} from '../../components/marketPlaceTable/MarketPlaceTable.jsx';
import {apiClient} from '../../services/apiClient';
import AppContext from '../../context/AppContext';
import {useUser} from '../../context/UserContext';
import {useDisclosure} from "@mantine/hooks";
import {SaleDetailsModal} from "@/components/saleDetailsModal/SaleDetailsModal.tsx";

function formatPrice(priceValue, priceCurrency) {
    if (priceValue == null) return '';
    if (!priceCurrency) return String(priceValue);
    return `${priceValue} ${priceCurrency}`;
}

function buildGenreMaps(genres) {
    const genreIdToName = new Map();
    const genreNameToId = new Map();

    genres.forEach((genre) => {
        genreIdToName.set(genre.genreId, genre.genreName);
        genreNameToId.set(genre.genreName, genre.genreId);
    });

    return { genreIdToName, genreNameToId };
}

function buildGenreOptions(genres) {
    return [
        { value: 'all', label: 'All genres' },
        ...genres.map((genre) => ({
            value: genre.genreId,
            label: genre.genreName,
        })),
    ];
}

function buildMarketplaceItems(directSales, itemDetailsMap, genreNameToId) {
    return directSales.flatMap((sale) => {
        const itemIds = sale.itemsId ?? [];

        return itemIds.map((itemId) => {
            const itemDetails = itemDetailsMap.get(itemId);
            if (!itemDetails) return null;

            const genreName = itemDetails?.genreName ?? 'Unknown';

            return {
                //main table details
                id: `${sale.directSaleId}-${itemId}`,
                item: itemDetails?.title ?? itemId,
                genreId: genreNameToId.get(genreName) ?? 'unknown',
                genreName,
                type: 'Direct Sale',
                price: formatPrice(sale.priceValue, sale.priceCurrency),

                //details card fields
                author:itemDetails?.author ?? 'unknown',
                condition:itemDetails?.condition ?? 'unknown',
                cover:itemDetails?.cover ?? '',
                seller:sale.seller ?? 'unknown',
                saleType:'Direct Sale',
            };
        }).filter(Boolean);
    });
}

function buildAuctionItems(auctions, itemDetailsMap, genreNameToId) {
    return auctions.flatMap((auction) => {
        const itemIds = auction.itemIds ?? [];

        return itemIds.map((itemId) => {
            const itemDetails = itemDetailsMap.get(itemId);
            if (!itemDetails) return null;

            const genreName = itemDetails?.genreName ?? 'Unknown';

            return {
                //main table details
                id: `${auction.auctionId}-${itemId}`,
                item: itemDetails?.title ?? itemId,
                genreId: genreNameToId.get(genreName) ?? 'unknown',
                genreName,
                type: 'Auction',
                price: formatPrice(auction.startingPrice, auction.priceCurrency),

                //details card fields
                author: itemDetails?.author ?? 'unknown',
                condition: itemDetails?.condition ?? 'unknown',
                cover: itemDetails?.cover ?? '',
                seller: 'unknown',
                saleType: 'Auction',
            };
        }).filter(Boolean);
    });
}

export default function Marketplace() {
    const { state } = useContext(AppContext);
    const { directSalesWithoutPriceHref } = state.app;
    const { currentUser } = useUser();
    const isLoggedIn = currentUser !== 'guest@aeiou.com';
    const canSeePrice = isLoggedIn;
    const marketplaceHref = !isLoggedIn ? directSalesWithoutPriceHref : null;

    const [items, setItems] = useState([]);
    const [genres, setGenres] = useState([{ value: 'all', label: 'All genres' }]);
    const [selectedGenre, setSelectedGenre] = useState('all');
    const [showDirectSales, setShowDirectSales] = useState(false);
    const [showAuctions, setShowAuctions] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [selectedSale, setSelectedSale] = useState(null);
    const [detailsOpened, { open: openDetails, close: closeDetails}] = useDisclosure(false);
    const handleSaleClick = (sale) => {setSelectedSale(sale);openDetails();}

    useEffect(() => {
        let isMounted = true;

        async function loadMarketplace() {
            try {
                setLoading(true);
                setError('');

                const [directSalesResponse, auctionsResponse, genresResponse] = await Promise.all([
                    marketplaceHref ? apiClient.getByHref(marketplaceHref) : apiClient.getDirectSales(),
                    apiClient.getAuctions(),
                    apiClient.getGenres(),
                ]);

                const directSales = directSalesResponse ?? [];
                const auctions = auctionsResponse ?? [];
                const genreList = genresResponse ?? [];
                const { genreNameToId } = buildGenreMaps(genreList);

                const uniqueItemIds = [...new Set([
                    ...directSales.flatMap((sale) => sale.itemsId ?? []),
                    ...auctions.flatMap((auction) => auction.itemIds ?? []),
                ])];

                const itemResponses = await Promise.all(
                    uniqueItemIds.map(async (itemId) => {
                        try {
                            const itemDetails = await apiClient.getItemById(itemId);
                            return [itemId, itemDetails];
                        } catch (itemError) {
                            if (itemError instanceof Error && itemError.message === '404') {
                                return null;
                            }

                            throw itemError;
                        }
                    })
                );

                const itemDetailsMap = new Map(itemResponses.filter(Boolean));
                const marketplaceItems = [
                    ...buildMarketplaceItems(directSales, itemDetailsMap, genreNameToId),
                    ...buildAuctionItems(auctions, itemDetailsMap, genreNameToId),
                ];

                if (!isMounted) return;

                setGenres(buildGenreOptions(genreList));
                setItems(marketplaceItems);
            } catch (loadError) {
                if (!isMounted) return;
                setError('Could not load marketplace');
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        }

        loadMarketplace();

        return () => {
            isMounted = false;
        };
    }, [marketplaceHref]);

    return (
        <DefaultLayout title="Marketplace" subtitle="CHECK ALL SALES:">
            {loading ? (
                <Text>Loading marketplace...</Text>
            ) : error ? (
                <Text c="red">{error}</Text>
            ) : (
                <>
                    <MarketPlaceTable
                        items={items}
                        genres={genres}
                        selectedGenre={selectedGenre}
                        onGenreChange={setSelectedGenre}
                        showDirectSales={showDirectSales}
                        showAuctions={showAuctions}
                        onShowDirectSalesChange={setShowDirectSales}
                        onShowAuctionsChange={setShowAuctions}
                        canSeePrice={canSeePrice}
                        onSaleClick={handleSaleClick}
                    />

                    <SaleDetailsModal
                        opened={detailsOpened}
                        item={
                            selectedSale && {
                                cover: selectedSale.cover,
                                title: selectedSale.item,
                                author: selectedSale.author,
                                genre: selectedSale.genreName,
                                condition: selectedSale.condition,
                                price: selectedSale.price,
                                seller: selectedSale.seller,
                            }
                        }
                        onClose={closeDetails}
                        onSeeMore={() => {
                            // depois é preciso ligar à página da venda em si
                        }}
                        />
                </>
                )}
        </DefaultLayout>
    );
}
