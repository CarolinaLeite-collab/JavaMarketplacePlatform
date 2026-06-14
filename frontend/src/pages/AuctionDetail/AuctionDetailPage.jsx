import { useEffect, useReducer, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
    Text, Card, Group, Stack, TextInput, Button, Badge, Alert, Grid, Table, Image,
} from '@mantine/core';
import { DefaultLayout } from '../../components/layout/DefaultLayout.tsx';
import { useUser } from '../../context/UserContext';
import {
    getAuction, placeBid, clearAuctionMessages, getAuctionSuccess,
} from '../../context/auctions/AuctionActions.jsx';
import {
    auctionReducer, initialAuctionState,
} from '../../context/auctions/AuctionReducer.jsx';

export default function AuctionDetailPage() {
    const { auctionId } = useParams();
    const { currentUser } = useUser();
    const isLoggedIn = currentUser !== 'guest@aeiou.com';

    const [state, dispatch] = useReducer(auctionReducer, initialAuctionState);
    const { auction, placeBidHref, error, successMessage } = state;

    const [bidValue, setBidValue] = useState('');
    const [loading, setLoading] = useState(true);

    // useEffect(() => {
    //     async function load() {
    //         setLoading(true);
    //         await getAuction(dispatch, auctionId);  // chama o backend
    //         setLoading(false);
    //     }
    //     load();
    // }, [auctionId]);

    // for testing
    useEffect(() => {
        dispatch(getAuctionSuccess({
            title: '1984 - George Orwell - First Edition',
            description: 'First edition of George Orwell\'s classic dystopian novel. Hardcover, good condition with minor wear on the spine. Published by Secker & Warburg, 1949.',
            status: 'Active',
            startingPrice: 50.00,
            priceCurrency: 'EUR',
            highestBid: 75.00,
            endDate: '2025-07-15T23:59:59',
            author: 'George Orwell',
            genre: 'Fiction',
            condition: 'Good',
            items: [
                { name: '1984 - Hardcover', genre: 'Fiction' },
                { name: '1984 - Paperback', genre: 'Fiction' },
            ],
            _links: {
                'place-bid': {
                    href: 'http://localhost:8081/auctions/test-123/bids'
                }
            }
        }));
        setLoading(false);
    }, [auctionId]);

    async function handlePlaceBid() {
        dispatch(clearAuctionMessages());

        const success = await placeBid(dispatch, placeBidHref, {
            bidValue: parseFloat(bidValue),
            currency: auction?.priceCurrency ?? 'EUR',
        });

        if (success) {
            setBidValue('');
            await getAuction(dispatch, auctionId);
        }
    }

    if (loading) {
        return (
            <DefaultLayout title="Auction" subtitle="AUCTION DETAIL:">
                <Text>Loading auction...</Text>
            </DefaultLayout>
        );
    }

    if (!auction) {
        return (
            <DefaultLayout title="Auction" subtitle="AUCTION DETAIL:">
                <Text c="red">Auction not found.</Text>
            </DefaultLayout>
        );
    }

    return (
        <DefaultLayout title={auction.title ?? 'Auction'} subtitle="">
            <Stack gap="md">
                {error && (
                    <Alert color="red" title="Error">{error}</Alert>
                )}
                {successMessage && (
                    <Alert color="green" title="Success">{successMessage}</Alert>
                )}

                <Grid>
                    {/* Left — Image */}
                    <Grid.Col span={4}>
                        <Image
                            src={auction.imageUrl ?? null}
                            fallbackSrc="https://placehold.co/400x500?text=No+Image"
                            radius="md"
                            h={400}
                            fit="cover"
                        />
                    </Grid.Col>

                    {/* Right — Description, Details, Actions */}
                    <Grid.Col span={8}>
                        <Stack gap="lg">

                            {/* Description */}
                            <div>
                                <Text size="sm" fw={700} c="dimmed" mb="xs">
                                    DESCRIPTION:
                                </Text>
                                <Card padding="md" radius="md" withBorder>
                                    <Text size="sm">
                                        {auction.description ?? 'No description available.'}
                                    </Text>
                                </Card>
                            </div>

                            {/* Details */}
                            <div>
                                <Text size="sm" fw={700} c="dimmed" mb="xs">
                                    DETAILS:
                                </Text>
                                <Table withTableBorder withColumnBorders>
                                    <Table.Tbody>
                                        <Table.Tr>
                                            <Table.Td fw={600}>Author</Table.Td>
                                            <Table.Td>{auction.author ?? 'N/A'}</Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Td fw={600}>Genre</Table.Td>
                                            <Table.Td>{auction.genre ?? 'N/A'}</Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Td fw={600}>Condition</Table.Td>
                                            <Table.Td>{auction.condition ?? 'N/A'}</Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Td fw={600}>Starting price</Table.Td>
                                            <Table.Td>{auction.startingPrice} {auction.priceCurrency}</Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Td fw={600}>Highest bid</Table.Td>
                                            <Table.Td fw={700}>
                                                {auction.highestBid ?? 'No bids yet'} {auction.highestBid ? auction.priceCurrency : ''}
                                            </Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Td fw={600}>Deadline</Table.Td>
                                            <Table.Td>{auction.endDate ?? 'N/A'}</Table.Td>
                                        </Table.Tr>
                                        <Table.Tr>
                                            <Table.Td fw={600}>Status</Table.Td>
                                            <Table.Td>
                                                <Badge color="blue" variant="light">
                                                    {auction.status ?? 'Active'}
                                                </Badge>
                                            </Table.Td>
                                        </Table.Tr>
                                    </Table.Tbody>
                                </Table>
                            </div>

                            {/* Items */}
                            {auction.items && auction.items.length > 0 && (
                                <div>
                                    <Text size="sm" fw={700} c="dimmed" mb="xs">
                                        ITEMS:
                                    </Text>
                                    <Table withTableBorder withColumnBorders highlightOnHover>
                                        <Table.Thead>
                                            <Table.Tr>
                                                <Table.Th>Item</Table.Th>
                                                <Table.Th>Genre</Table.Th>
                                            </Table.Tr>
                                        </Table.Thead>
                                        <Table.Tbody>
                                            {auction.items.map((item, index) => (
                                                <Table.Tr key={index}>
                                                    <Table.Td>{item.name}</Table.Td>
                                                    <Table.Td>{item.genre}</Table.Td>
                                                </Table.Tr>
                                            ))}
                                        </Table.Tbody>
                                    </Table>
                                </div>
                            )}

                            {/* Bid Actions */}
                            {isLoggedIn && placeBidHref && (
                                <Group justify="flex-end">
                                    <Stack gap="xs">
                                        <Group>
                                            <TextInput
                                                placeholder="Enter bid value"
                                                value={bidValue}
                                                onChange={(e) => setBidValue(e.target.value)}
                                                type="number"
                                                min="0"
                                                step="0.01"
                                                style={{ width: 180 }}
                                            />
                                            <Button
                                                onClick={handlePlaceBid}
                                                disabled={!bidValue || parseFloat(bidValue) <= 0}
                                                size="md"
                                            >
                                                Place Bid
                                            </Button>
                                        </Group>
                                    </Stack>
                                </Group>
                            )}
                        </Stack>
                    </Grid.Col>
                </Grid>
            </Stack>
        </DefaultLayout>
    );
}