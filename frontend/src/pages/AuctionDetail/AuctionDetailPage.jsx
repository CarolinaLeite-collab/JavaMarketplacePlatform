import { useEffect, useReducer, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
    Text, Group, Stack, Button, Badge, Alert,
    Grid, Table, Image, Title, Card, Modal, TextInput,SimpleGrid
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
import { PlaceBidModal } from '../../components/placeBidModal/PlaceBidModal.tsx';

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
            endDate: '3 days, 15-07-2025, 16:42',
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

    async function handlePlaceBid(bidAmount) {
        dispatch(clearAuctionMessages());

        const success = await placeBid(dispatch, placeBidHref, {
            bidValue: bidAmount,
            currency: auction?.priceCurrency ?? 'EUR',
        });

        if (success) {
            closeBidModal();
            await getAuction(dispatch, auctionId);
        }
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
                <Title order={1} fz={50} fw={600} style={{ fontFamily: 'EB Garamond, serif' }}>
                    <Text span fz={30} fw={500} >{auction.publicationType ?? 'N/A'}:</Text>{' '}
                    {auction.title ?? 'Auction'}
                </Title>

                {/* Image + Info */}
                <Grid>
                    <Grid.Col span={6}>
                        <Image
                            src={auction.imageUrl ?? null}
                            fallbackSrc="https://placehold.co/500x550?text=No+Image"
                            radius="md"
                            h={550}
                            fit="cover"
                        />
                    </Grid.Col>

                    <Grid.Col span={6}>
                        <Stack gap="md" h="100%" justify="space-between">
                            <Stack gap="sm">

                                <Stack gap={1}>
                                    {/* Current Price */}
                                    <Text fz={30} fw={700} c="var(--mantine-color-indigo-7)">
                                        {auction.highestBid ?? 'No bids'} {auction.highestBid ? auction.priceCurrency : ''}
                                    </Text>

                                    {/* Starting price and Number of bids*/}
                                    <Group gap="lg" align="baseline">
                                        <Text size="sm" c="dimmed">
                                            Starting price: {auction.startingPrice} {auction.priceCurrency}
                                        </Text>

                                        <Text td="underline" size="sm" c="dimmed">
                                            {auction.bids} bids
                                        </Text>
                                    </Group>

                                    {/* Seller */}
                                    <Text size="xs" c="dimmed" >
                                        Sold by {auction.seller ?? 'Unknown'}
                                    </Text>
                                </Stack>

                                    {/* Deadline + Status */}
                                    <Group gap="lg" c="dimmed" mt="xs">
                                        <Text size="sm">
                                            Ends in {auction.endDate ?? 'N/A'}
                                        </Text>
                                        <Badge color="var(--mantine-color-indigo-7)" variant="light" size="sm">
                                            {auction.status ?? 'Active'}
                                        </Badge>
                                    </Group>


                                {/* Seller's description */}
                                <Stack gap={4} mt="xs">
                                    <Text size="m" fw={700} c="dimmed">Seller's description:</Text>
                                    <Text size="sm">
                                        {auction.description ?? 'No description available.'}
                                    </Text>
                                </Stack>

                            </Stack>

                            <Stack>
                                {/* Quick info */}
                                <SimpleGrid cols={4} mt="sm">
                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">Author</Text>
                                        <Text size="sm" fw={400}>{auction.author}</Text>
                                    </Card>
                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">Edition</Text>
                                        <Text size="sm" fw={400}>{auction.edition}</Text>
                                    </Card>
                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">ISBN</Text>
                                        <Text size="sm" fw={400}>{auction.identifier}</Text>
                                    </Card>
                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">Condition</Text>
                                        <Text size="sm" fw={400}>{auction.condition}</Text>
                                    </Card>
                                </SimpleGrid>

                                <Button
                                    onClick={openBidModal}
                                    disabled={!isLoggedIn || !placeBidHref}
                                    fullWidth
                                    size="md"
                                >
                                    Place Bid
                                </Button>
                                <Button
                                    variant="outline"
                                    color="var(--mantine-color-indigo-7)"
                                    fullWidth
                                    size="md"
                                >
                                    Add to Cart
                                </Button>
                                <Button
                                    variant=""
                                    color="var(--mantine-color-indigo-7)"
                                    fullWidth
                                    size="md"
                                >
                                    Buy Now
                                </Button>
                            </Stack>
                        </Stack>
                    </Grid.Col>
                </Grid>

                {/* Synopsis */}
                <Stack gap="md">
                    <Stack gap={4}>
                        <Text size="m" fw={700} c="dimmed">Synopsis:</Text>
                        <Card padding="md" radius="md" withBorder bg="gray.0">
                            <Text size="sm" fs="italic">
                                {auction.synopsis ?? 'No synopsis available.'}
                            </Text>
                        </Card>
                    </Stack>

                    <Stack gap={4}>
                        <Text size="m" fw={700} c="dimmed">Details:</Text>
                        <Table withTableBorder withColumnBorders>
                            <Table.Tbody>
                                <Table.Tr>
                                    <Table.Td fw={600} w="15%">Publisher</Table.Td>
                                    <Table.Td w="35%" bg="gray.0">{auction.publisher ?? 'N/A'}</Table.Td>
                                    <Table.Td fw={600}>Genre</Table.Td>
                                    <Table.Td bg="gray.0">{auction.genre ?? 'N/A'}</Table.Td>
                                </Table.Tr>
                                <Table.Tr>
                                    <Table.Td fw={600}>Year</Table.Td>
                                    <Table.Td bg="gray.0">{auction.year ?? 'N/A'}</Table.Td>
                                    <Table.Td fw={600}>Language</Table.Td>
                                    <Table.Td bg="gray.0">{auction.language ?? 'N/A'}</Table.Td>
                                </Table.Tr>
                                <Table.Tr>
                                    <Table.Td fw={600}>Pages</Table.Td>
                                    <Table.Td bg="gray.0">{auction.pages ?? 'N/A'}</Table.Td>
                                    <Table.Td fw={600}>Binding</Table.Td>
                                    <Table.Td bg="gray.0">{auction.binding ?? 'N/A'}</Table.Td>
                                </Table.Tr>
                                <Table.Tr>
                                    <Table.Td fw={600}>Weight</Table.Td>
                                    <Table.Td bg="gray.0">{auction.weight ?? 'N/A'}</Table.Td>
                                    <Table.Td fw={600}>Dimensions</Table.Td>
                                    <Table.Td bg="gray.0">{auction.dimensions ?? 'N/A'}</Table.Td>
                                </Table.Tr>
                            </Table.Tbody>
                        </Table>
                    </Stack>
                </Stack>
            </Stack>
            <PlaceBidModal
                opened={bidModalOpened}
                currentPrice={auction.highestBid ?? auction.startingPrice}
                currency={auction.priceCurrency ?? 'EUR'}
                onClose={closeBidModal}
                onConfirm={handlePlaceBid}
            />
        </DefaultLayout>
    );
}