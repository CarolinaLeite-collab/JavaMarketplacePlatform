import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
    Text, Group, Stack, Button, Badge, Alert,
    Grid, Table, Image, Title, Card, SimpleGrid
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { DefaultLayout } from '../../components/layout/DefaultLayout.tsx';
import { useUser } from '../../context/UserContext';
import { apiClient } from '../../services/apiClient';
import { PlaceBidModal } from '../../components/placeBidModal/PlaceBidModal.tsx';

/**
 * Formats an ISO end date into a short "in Xd Yh" string.
 * Returns "Ended" if the date is in the past.
 */
function formatTimeRemaining(endDateIso) {
    if (!endDateIso) return 'N/A';
    const end = new Date(endDateIso);
    const now = new Date();
    const diffMs = end - now;
    if (diffMs <= 0) return 'Ended';

    const totalMinutes = Math.floor(diffMs / 60000);
    const days = Math.floor(totalMinutes / (60 * 24));
    const hours = Math.floor((totalMinutes % (60 * 24)) / 60);
    const minutes = totalMinutes % 60;

    if (days > 0) return `${days}d ${hours}h`;
    if (hours > 0) return `${hours}h ${minutes}m`;
    return `${minutes}m`;
}

/**
 * Derives auction status from start/end dates.
 */
function deriveStatus(startDateIso, endDateIso) {
    const now = new Date();
    if (startDateIso && new Date(startDateIso) > now) return 'Scheduled';
    if (endDateIso && new Date(endDateIso) < now) return 'Ended';
    return 'Active';
}

/**
 * Page component that displays the details of a specific auction.
 *
 * Fetches the auction and its first item from the backend, and renders
 * pricing, deadline, condition, synopsis and a details table. Logged-in
 * users can place bids through a modal dialog when the backend exposes
 * a placeBid HATEOAS link.
 */
export default function AuctionDetailPage() {
    const { auctionId } = useParams();
    const { currentUser } = useUser();
    const isLoggedIn = currentUser !== 'guest@aeiou.com';

    const [auction, setAuction] = useState(null);
    const [item, setItem] = useState(null);
    const [edition, setEdition] = useState(null);
    const [publication, setPublication] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [publisherInfo, setPublisherInfo] = useState(null);

    const [bidModalOpened, { open: openBidModal, close: closeBidModal }] = useDisclosure(false);

    async function loadAuction() {
        try {
            setLoading(true);
            setError('');

            const auctionData = await apiClient.getAuctionById(auctionId);
            const itemId = auctionData?.itemIds?.[0];

            let itemData = null;
            let editionData = null;
            let publisherData = null;

            if (itemId) {
                try {
                    itemData = await apiClient.getItemById(itemId);
                } catch (e) {
                    console.warn('Could not load item details', e);
                }
            }

            if (itemData?.editionId) {
                try {
                    editionData = await apiClient.getEditionById(itemData.editionId);
                } catch (e) {
                    console.warn('Could not load edition details', e);
                }
            }

            if (editionData?.publishingCompanyId) {
                try {
                    publisherData = await apiClient.getPublishingCompanyById(editionData.publishingCompanyId);
                } catch (e) {
                    console.warn('Could not load publisher', e);
                }
            }

            setAuction(auctionData);
            setItem(itemData);
            setEdition(editionData);
            setPublisherInfo(publisherData);
        } catch (loadError) {
            console.error(loadError);
            setError('Could not load auction.');
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadAuction();
    }, [auctionId]);

    async function handlePlaceBid(bidAmount) {
        setError('');
        setSuccessMessage('');

        const placeBidHref = auction?._links?.placeBid?.href;
        if (!placeBidHref) {
            setError('Placing bids is not available for this auction.');
            return;
        }

        try {
            await apiClient.postByHref(placeBidHref, {
                bidValue: bidAmount,
                currency: auction?.priceCurrency ?? 'EUR',
            });

            setSuccessMessage('Bid placed successfully.');
            closeBidModal();
            await loadAuction();
        } catch (bidError) {
            console.error(bidError);
            setError('Could not place bid.');
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

    // Auction fields
    const startingPrice = auction.startingPrice;
    const priceCurrency = auction.priceCurrency ?? 'EUR';
    const bidsCount = auction.bidCount ?? 0;
    const highestBid = auction.highestBid ?? null;
    const endDate       = auction.endDate;
    const status        = deriveStatus(auction.startDate, auction.endDate);
    const placeBidHref  = auction?._links?.placeBid?.href ?? null;

    // Item fields
    const title           = item?.title ?? 'Auction';
    const publicationType     = item?.publicationTypeName
        ? item.publicationTypeName.charAt(0) + item.publicationTypeName.slice(1).toLowerCase()
        : 'N/A';
    const imageUrl         = item?.picture ?? null;
    const author          = item?.authorName ?? 'Unknown';
    const identifier      = item?.identifier && item.identifier !== 'no identifier'
        ? item.identifier : 'N/A';
    const condition       = item?.condition ?? 'N/A';
    const description     = item?.description ?? 'No description available.';
    const genre           = item?.genreName ?? 'N/A';
    const year            = item?.publishingYear ?? item?.releaseYear ?? 'N/A';
    const language        = item?.language ?? 'N/A';

    // Edition fields (from /editions/{id})
    const publisher       = publisherInfo?.publishingCompanyName ?? 'N/A';
    const pages           = edition?.numberOfPages ?? 'N/A';
    const editionNumber   = edition?.editionNumber ?? 'N/A';
    const binding         = edition?.binding ?? 'N/A';
    const weight = edition?.weight
        ? `${edition.weight.value} ${edition.weight.unit}`
        : 'N/A';
    const dimensions = edition?.dimension
        ? `${edition.dimension.width} x ${edition.dimension.height} x ${edition.dimension.depth} ${edition.dimension.unit}`
        : 'N/A';
    const seller          = auction.seller ?? 'Unknown';

    // Edition fields (from /publications/{id})
    const synopsis        = publication?.synopsis ?? 'N/A';

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
                    <Text span fz={30} fw={500}>{publicationType}:</Text>{' '}
                    {title}
                </Title>

                {/* Image + Info */}
                <Grid>
                    <Grid.Col span={4}>
                        <Image
                            src={imageUrl}
                            fallbackSrc="https://placehold.co/500x550?text=No+Image"
                            radius="md"
                            h={550}
                            fit="cover"
                        />
                    </Grid.Col>

                    <Grid.Col span={1} />

                    <Grid.Col span={7}>
                        <Stack gap="md" h="100%" justify="space-between">
                            <Stack gap="sm">
                                <Stack gap={1}>
                                    <Text fz={30} fw={700} c="var(--mantine-color-indigo-7)">
                                        {highestBid != null
                                            ? `${highestBid} ${priceCurrency}`
                                            : `${startingPrice} ${priceCurrency}`}
                                    </Text>

                                    <Group gap="lg" align="baseline">
                                        <Text size="sm" c="dimmed">
                                            Starting price: {startingPrice} {priceCurrency}
                                        </Text>

                                        <Button
                                            variant="transparent"
                                            size="compact-sm"
                                            c="dimmed"
                                            td="underline"
                                            p={0}
                                            onClick={() => { /* TODO: open bids modal */ }}
                                        >
                                            {bidsCount} bids
                                        </Button>
                                    </Group>

                                    <Text size="xs" c="dimmed">
                                        Sold by {seller}
                                    </Text>
                                </Stack>

                                <Group gap="lg" c="dimmed" mt="xs">
                                    <Text size="sm">
                                        Ends in {formatTimeRemaining(endDate)}
                                    </Text>
                                    <Badge color="var(--mantine-color-indigo-7)" variant="light" size="sm">
                                        {status}
                                    </Badge>
                                </Group>

                                <Stack gap={4} mt="xs">
                                    <Text size="m" fw={700} c="dimmed">Seller's description:</Text>
                                    <Text size="sm">{description}</Text>
                                </Stack>
                            </Stack>

                            <Stack>
                                <SimpleGrid cols={4} mt="sm">
                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">Author</Text>
                                        <Text size="sm" fw={400}>{author}</Text>
                                    </Card>
                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">Edition</Text>
                                        <Text size="sm" fw={400}>{editionNumber}</Text>
                                    </Card>
                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">ISBN</Text>
                                        <Text size="sm" fw={400}>{identifier}</Text>
                                    </Card>
                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">Condition</Text>
                                        <Text size="sm" fw={400}>{condition}</Text>
                                    </Card>
                                </SimpleGrid>

                                <Button
                                    onClick={openBidModal}
                                    disabled={!isLoggedIn || !placeBidHref || status !== 'Active'}
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

                {/* Synopsis + Details */}
                <Stack gap="md">
                    <Stack gap={4}>
                        <Text size="m" fw={700} c="dimmed">Synopsis:</Text>
                        <Card padding="md" radius="md" withBorder bg="gray.0">
                            <Text size="sm" fs="italic">{synopsis}</Text>
                        </Card>
                    </Stack>

                    <Stack gap={4}>
                        <Text size="m" fw={700} c="dimmed">Details:</Text>
                        <Table withTableBorder withColumnBorders>
                            <Table.Tbody>
                                <Table.Tr>
                                    <Table.Td fw={600} w="15%">Publisher</Table.Td>
                                    <Table.Td w="35%" bg="gray.0">{publisher}</Table.Td>
                                    <Table.Td fw={600}>Genre</Table.Td>
                                    <Table.Td bg="gray.0">{genre}</Table.Td>
                                </Table.Tr>
                                <Table.Tr>
                                    <Table.Td fw={600}>Year</Table.Td>
                                    <Table.Td bg="gray.0">{year}</Table.Td>
                                    <Table.Td fw={600}>Language</Table.Td>
                                    <Table.Td bg="gray.0">{language}</Table.Td>
                                </Table.Tr>
                                <Table.Tr>
                                    <Table.Td fw={600}>Pages</Table.Td>
                                    <Table.Td bg="gray.0">{pages}</Table.Td>
                                    <Table.Td fw={600}>Binding</Table.Td>
                                    <Table.Td bg="gray.0">{binding}</Table.Td>
                                </Table.Tr>
                                <Table.Tr>
                                    <Table.Td fw={600}>Weight</Table.Td>
                                    <Table.Td bg="gray.0">{weight}</Table.Td>
                                    <Table.Td fw={600}>Dimensions</Table.Td>
                                    <Table.Td bg="gray.0">{dimensions}</Table.Td>
                                </Table.Tr>
                            </Table.Tbody>
                        </Table>
                    </Stack>
                </Stack>
            </Stack>

            <PlaceBidModal
                opened={bidModalOpened}
                currentPrice={highestBid ?? startingPrice}
                currency={priceCurrency}
                onClose={closeBidModal}
                onConfirm={handlePlaceBid}
            />
        </DefaultLayout>
    );
}