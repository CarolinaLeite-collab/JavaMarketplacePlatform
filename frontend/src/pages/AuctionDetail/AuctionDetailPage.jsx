import { useEffect, useReducer, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
    Text, Group, Stack, Button, Badge, Alert,
    Grid, Table, Image, Title, Card, Modal, TextInput,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { DefaultLayout } from '../../components/layout/DefaultLayout.tsx';
import { useUser } from '../../context/UserContext';
import {
    getAuction, placeBid, clearAuctionMessages, getAuctionSuccess,
} from '../../context/auctions/AuctionActions.jsx';
import {
    auctionReducer, initialAuctionState,
} from '../../context/auctions/AuctionReducer.jsx';

/**
 * Page component that displays the details of a specific auction.
 *
 * Renders auction information including title, image, pricing, seller,
 * deadline, description, synopsis, and a details table with publication
 * attributes. Logged-in users with the appropriate HATEOAS link can
 * place bids through a modal dialog.
 */

export default function AuctionDetailPage() {
    const { auctionId } = useParams();
    const { currentUser } = useUser();
    const isLoggedIn = currentUser !== 'guest@aeiou.com';

    const [state, dispatch] = useReducer(auctionReducer, initialAuctionState);
    const { auction, placeBidHref, error, successMessage } = state;

    const [bidValue, setBidValue] = useState('');
    const [loading, setLoading] = useState(true);
    const [bidModalOpened, { open: openBidModal, close: closeBidModal }] = useDisclosure(false);

    useEffect(() => {
        dispatch(getAuctionSuccess({
            title: '1984',
            description: 'First edition of George Orwell\'s classic dystopian novel. Hardcover, good condition with minor wear on the spine. Published by Secker & Warburg, 1949.',
            synopsis: 'Among the seminal texts of the 20th century, Nineteen Eighty-Four is a rare work that grows more haunting as its dystopian purgatory becomes more real.',
            status: 'Active',
            startingPrice: 50.00,
            highestBid: 75.00,
            priceCurrency: 'EUR',
            seller: 'Unknown',
            endDate: '3 days, 2025-07-15T23:59:59',
            author: 'George Orwell',
            genre: 'Fiction',
            condition: 'Good',
            publisher: 'Secker & Warburg',
            publicationType: 'Book',
            identifier: '978-0-451-52493-5',
            edition: '1st Edition',
            bids: "6",
            year: 1949,
            language: 'English',
            binding: 'Hardcover',
            pages: 328,
            dimensions: '20 x 13 x 2.5 cm',
            weight: '350 g',
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
            closeBidModal();
            await getAuction(dispatch, auctionId);
        }
    }

    if (loading) {
        return (
            <DefaultLayout title="Auction" subtitle="">
                <Text>Loading auction...</Text>
            </DefaultLayout>
        );
    }

    if (!auction) {
        return (
            <DefaultLayout title="Auction" subtitle="">
                <Text c="red">Auction not found.</Text>
            </DefaultLayout>
        );
    }

    return (
        <DefaultLayout title="" subtitle="">
            <Stack gap="xl">
                {error && (
                    <Alert color="red" title="Error">{error}</Alert>
                )}
                {successMessage && (
                    <Alert color="green" title="Success">{successMessage}</Alert>
                )}

                {/* Title */}
                <Title order={1} fz={50} style={{ fontFamily: 'EB Garamond, serif' }}>
                    {auction.title ?? 'Auction'}
                </Title>

                {/* Image + Info */}
                <Grid>
                    <Grid.Col span={5}>
                        <Image
                            src={auction.imageUrl ?? null}
                            fallbackSrc="https://placehold.co/500x550?text=No+Image"
                            radius="md"
                            h={550}
                            fit="cover"
                        />
                    </Grid.Col>

                    <Grid.Col span={7}>
                        <Stack gap="md" h="100%" justify="space-between">
                            <Stack gap="sm">

                                {/* Price line */}
                                <Text fz={30} fw={700} c="blue">
                                    {auction.highestBid ?? 'No bids'} {auction.highestBid ? auction.priceCurrency : ''}
                                </Text>





                                <Group gap="lg" align="baseline">
                                    <Text size="sm" c="dimmed">
                                        Starting price: {auction.startingPrice} {auction.priceCurrency}
                                    </Text>

                                    <Text size="sm" c="dimmed">
                                        {auction.bids} bids
                                    </Text>

                                </Group>

                                {/* Seller */}
                                <Text size="xs" c="dimmed" >
                                    Sold by {auction.seller ?? 'Unknown'}
                                </Text>

                                {/* Deadline + Status */}
                                <Group gap="md">
                                    <Text size="sm">
                                        Ends: {auction.endDate ?? 'N/A'}
                                    </Text>
                                    <Badge color="blue" variant="light" size="sm">
                                        {auction.status ?? 'Active'}
                                    </Badge>
                                </Group>

                                {/* Description */}
                                <Card padding="md" radius="md" withBorder mt="md">
                                    <Text size="sm">
                                        {auction.description ?? 'No description available.'}
                                    </Text>
                                </Card>
                            </Stack>

                            <Button
                                onClick={handlePlaceBid}
                                disabled={!bidValue || parseFloat(bidValue) <= 0}
                                fullWidth
                                size="md"
                            >
                                Place Bid
                            </Button>
                            <Button
                                variant="outline"
                                fullWidth
                                size="md"
                            >
                                Add to Cart
                            </Button>
                            <Button
                                variant=""
                                color="blue"
                                fullWidth
                                size="md"
                            >
                                Buy Now
                            </Button>
                        </Stack>
                    </Grid.Col>
                </Grid>

                {/* Details */}
                <Stack gap="md">
                    <Text size="sm" fw={700} c="dimmed">SYNOPSIS:</Text>

                    {/* Synopsis */}
                    <Card padding="md" radius="md" withBorder bg="white">
                        <Text size="sm" fs="italic">
                            {auction.synopsis ?? 'No synopsis available.'}
                        </Text>
                    </Card>

                    <Text size="sm" fw={700} c="dimmed">DETAILS:</Text>

                    {/* Details table — 4 columns */}
                    <Table withTableBorder withColumnBorders>
                        <Table.Tbody>
                            <Table.Tr>
                                <Table.Td fw={600} w="15%">Author</Table.Td>
                                <Table.Td w="35%">{auction.author ?? 'N/A'}</Table.Td>
                                <Table.Td fw={600} w="15%">Publisher</Table.Td>
                                <Table.Td w="35%">{auction.publisher ?? 'N/A'}</Table.Td>
                            </Table.Tr>
                            <Table.Tr>
                                <Table.Td fw={600}>Genre</Table.Td>
                                <Table.Td>{auction.genre ?? 'N/A'}</Table.Td>
                                <Table.Td fw={600}>Edition</Table.Td>
                                <Table.Td>{auction.edition ?? 'N/A'}</Table.Td>
                            </Table.Tr>
                            <Table.Tr>
                                <Table.Td fw={600}>Publication type</Table.Td>
                                <Table.Td>{auction.publicationType ?? 'N/A'}</Table.Td>
                                <Table.Td fw={600}>Identifier</Table.Td>
                                <Table.Td>{auction.identifier ?? 'N/A'}</Table.Td>
                            </Table.Tr>
                            <Table.Tr>
                                <Table.Td fw={600}>Year</Table.Td>
                                <Table.Td>{auction.year ?? 'N/A'}</Table.Td>
                                <Table.Td fw={600}>Language</Table.Td>
                                <Table.Td>{auction.language ?? 'N/A'}</Table.Td>
                            </Table.Tr>
                            <Table.Tr>
                                <Table.Td fw={600}>Pages</Table.Td>
                                <Table.Td>{auction.pages ?? 'N/A'}</Table.Td>
                                <Table.Td fw={600}>Binding</Table.Td>
                                <Table.Td>{auction.binding ?? 'N/A'}</Table.Td>
                            </Table.Tr>
                            <Table.Tr>
                                <Table.Td fw={600}>Condition</Table.Td>
                                <Table.Td>{auction.condition ?? 'N/A'}</Table.Td>
                                <Table.Td fw={600}>Weight</Table.Td>
                                <Table.Td>{auction.weight ?? 'N/A'}</Table.Td>
                            </Table.Tr>
                            <Table.Tr>
                                <Table.Td fw={600}>Dimensions</Table.Td>
                                <Table.Td>{auction.dimensions ?? 'N/A'}</Table.Td>
                                <Table.Td fw={600}></Table.Td>
                                <Table.Td></Table.Td>
                            </Table.Tr>
                        </Table.Tbody>
                    </Table>
                </Stack>


            </Stack>
        </DefaultLayout>
    );
}