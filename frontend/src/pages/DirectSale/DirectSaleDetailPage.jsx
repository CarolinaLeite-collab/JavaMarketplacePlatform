import { useEffect, useState, useContext } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import {
    Text, Group, Stack, Button, Badge, Alert,
    Grid, Table, Image, Title, Card, SimpleGrid, Tooltip
} from '@mantine/core';
import { DefaultLayout } from '../../components/layout/DefaultLayout.tsx';
import { useUser } from '../../context/UserContext';
import AppContext from '../../context/AppContext';
import { ADD_TO_CART } from '../../context/cart/CartActions';
import { apiClient } from '../../services/apiClient';
import { notifications } from '@mantine/notifications';

function formatTimeRemaining(endDateIso, status) {
    if (!endDateIso) return 'N/A';

    if (status === 'COMPLETED') return 'Completed';
    if (status === 'CANCELLED') return 'Cancelled';
    if (status === 'EXPIRED') return 'Expired';

    const end = new Date(endDateIso);
    const now = new Date();
    const diffMs = end - now;

    if (diffMs <= 0) return 'Expired';

    const totalMinutes = Math.floor(diffMs / 60000);
    const days = Math.floor(totalMinutes / (60 * 24));
    const hours = Math.floor((totalMinutes % (60 * 24)) / 60);
    const minutes = totalMinutes % 60;

    if (days > 0) return `${days}d ${hours}h`;
    if (hours > 0) return `${hours}h ${minutes}m`;
    return `${minutes}m`;
}
    const detailsBackground = 'light-dark(var(--mantine-color-gray-0), var(--mantine-color-dark-6))';


export default function DirectSaleDetailPage() {
    const { directSaleId } = useParams();
    const location = useLocation();
    const { currentUser } = useUser();
    const { state, dispatch } = useContext(AppContext);

    const isLoggedIn = currentUser !== 'guest@aeiou.com';

    const [directSale, setDirectSale] = useState(null);
    const [item, setItem] = useState(null);
    const [edition, setEdition] = useState(null);
    const [publisherInfo, setPublisherInfo] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    async function loadDirectSale() {
        try {
            setLoading(true);
            setError('');
            setDirectSale(null);
            setItem(null);
            setEdition(null);
            setPublisherInfo(null);

            let directSaleData;

            if (!isLoggedIn) {
                directSaleData =
                    await apiClient.getDirectSaleWithoutPrice(directSaleId);
            } else {
                const options = await apiClient.getDirectSalesOptions();
                const template = options?._links?.['direct-sale']?.href;

                const directSaleHref =
                    location.state?.selfHref ??
                    template?.replace('{id}', encodeURIComponent(directSaleId));

                if (!directSaleHref) {
                    throw new Error('Direct Sale link not available');
                }

                directSaleData = await apiClient.getByHref(directSaleHref);
            }

            const itemLink = directSaleData?._links?.item;

            const itemHref = Array.isArray(itemLink)
                ? itemLink[0]?.href
                : itemLink?.href;

            const itemData = itemHref
                ? await apiClient.getByHref(itemHref)
                : null;

            let editionData = null;
            let publisherData = null;

            if (itemData?.editionId) {
                editionData = await apiClient.getEditionById(itemData.editionId);
            }

            if (editionData?.publishingCompanyId) {
                publisherData = await apiClient.getPublishingCompanyById(
                    editionData.publishingCompanyId
                );
            }

            setDirectSale(directSaleData);
            setItem(itemData);
            setEdition(editionData);
            setPublisherInfo(publisherData);
        } catch (loadError) {
            console.error(loadError);
            setError('Could not load Direct Sale.');
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadDirectSale();
    }, [directSaleId, isLoggedIn]);

    function sellerUsernameFromEmail(email) {
        if (!email) return 'unknown';
        return email.split('@')[0];
    }

    if (loading) {
        return (
            <DefaultLayout title="" subtitle="">
                <Text>Loading...</Text>
            </DefaultLayout>
        );
    }

    if (!directSale) {
        return (
            <DefaultLayout title="" subtitle="">
                <Alert color="red">Direct Sale not found.</Alert>
            </DefaultLayout>
        );
    }

    const price = directSale.priceValue;
    const priceCurrency = directSale.priceCurrency;
    const endDate = directSale.endDate;
    const status = directSale.status ?? 'ACTIVE';
    const addToCartHref = directSale?._links?.['shopping-cart']?.href ?? null;
    const isOwnSale = directSale.sellerId === currentUser;

    const isInCart = state.cart?.items?.some(cartItem => cartItem.id === directSale.directSaleId) ?? false;

    const addToCartDisabled =
        !isLoggedIn ||
        isOwnSale ||
        isInCart ||
        !addToCartHref ||
        status !== 'ACTIVE';

    const addToCartTooltip = isOwnSale
        ? 'Cannot buy own direct sale!'
        : isInCart
            ? 'This DirectSale is already present in the cart!'
            : '';

    const title = item?.title ?? 'DirectSale';
    const publicationType = item?.publicationTypeName
        ? item.publicationTypeName.charAt(0) + item.publicationTypeName.slice(1).toLowerCase()
        : 'N/A';

    const imageUrl = item?.picture ?? null;
    const author = item?.authorName ?? 'Unknown';
    const identifier = item?.identifier && item.identifier !== 'no identifier'
        ? item.identifier
        : 'N/A';
    const condition = item?.condition ?? 'N/A';
    const description = item?.description ?? 'No description available.';
    const genre = item?.genreName ?? 'N/A';
    const year = item?.publishingYear ?? item?.releaseYear ?? 'N/A';
    const language = item?.language ?? 'N/A';

    const publisher = publisherInfo?.publishingCompanyName ?? 'N/A';
    const pages = edition?.numberOfPages ?? 'N/A';
    const editionNumber = edition?.editionNumber ?? 'N/A';
    const binding = edition?.binding ?? 'N/A';

    const weight = edition?.weight
        ? `${edition.weight.value} ${edition.weight.unit}`
        : 'N/A';

    const dimensions = edition?.dimension
        ? `${edition.dimension.width} x ${edition.dimension.height} x ${edition.dimension.depth} ${edition.dimension.unit}`
        : 'N/A';

    const seller = sellerUsernameFromEmail(directSale.sellerId);
    const synopsis = item?.synopsis ?? 'N/A';

    async function handleAddToCart() {
        if (!addToCartHref) {
            notifications.show({
                title: 'Item could not be added to cart',
                message: 'Adding this item to the cart is not available.',
                color: 'red',
                autoClose: 3000,
            });
            return;
        }

        try {
            setError('');

            const response = await apiClient.postByHref(addToCartHref, {
                directSaleId: directSale.directSaleId,
            });

            dispatch({
                type: ADD_TO_CART,
                payload: {
                    id: directSale.directSaleId,
                    name: title,
                    price: `${price} ${priceCurrency}`,
                    priceValue: price,
                    currency: priceCurrency,
                    deleteHref: response?._links?.self?.href,
                    image: imageUrl,
                },
            });

            notifications.show({
                title: 'Item added to cart',
                message: 'The item was successfully added to your cart.',
                color: 'green',
                autoClose: 3000,
            });
        } catch (error) {
            console.error(error);
            notifications.show({
                title: 'Item could not be added to cart',
                message: 'There was an issue adding this item to your cart.',
                color: 'red',
                autoClose: 3000,
            });
        }
    }

    return (
        <DefaultLayout title="" subtitle="">
            <Stack gap="xl">
                {error && <Alert color="red" title="Error">{error}</Alert>}

                <Title order={1} fz={50} fw={600} style={{ fontFamily: 'EB Garamond, serif' }}>
                    <Text span fz={30} fw={500}>{publicationType}:</Text>{' '}
                    {title}
                </Title>

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
                                    {isLoggedIn && (
                                        <Text fz={30} fw={700} c="var(--mantine-color-indigo-7)">
                                        {price} {priceCurrency}
                                    </Text>
                                    )}

                                    <Text size="xs" c="dimmed">
                                        Sold by {seller}
                                    </Text>
                                </Stack>

                                <Group gap="lg" c="dimmed" mt="xs">
                                    <Text size="sm">
                                        Ends in {formatTimeRemaining(endDate, status)}
                                    </Text>

                                    <Badge color="blue" variant="light" size="sm">
                                        Direct Sale
                                    </Badge>

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
                                        <Text size="sm">{author}</Text>
                                    </Card>

                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">Edition</Text>
                                        <Text size="sm">{editionNumber}</Text>
                                    </Card>

                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">ISBN</Text>
                                        <Text size="sm">{identifier}</Text>
                                    </Card>

                                    <Card padding="xs" radius="md" withBorder>
                                        <Text size="xs" c="dimmed">Condition</Text>
                                        <Text size="sm">{condition}</Text>
                                    </Card>
                                </SimpleGrid>

                                <Tooltip
                                    label={addToCartTooltip}
                                    disabled={!isOwnSale && !isInCart}
                                    withArrow
                                >
                                    <div style={{ width: '100%' }}>
                                        <Button
                                            variant={isInCart ? 'outline' : 'filled'}
                                            color={
                                                isInCart
                                                    ? 'gray'
                                                    : 'var(--mantine-color-indigo-7)'
                                            }
                                            fullWidth
                                            size="md"
                                            disabled={addToCartDisabled}
                                            onClick={handleAddToCart}
                                        >
                                            {isInCart
                                                ? 'Already in Cart'
                                                : 'Add to Cart'}
                                        </Button>
                                    </div>
                                </Tooltip>
                            </Stack>
                        </Stack>
                    </Grid.Col>
                </Grid>

                <Stack gap="md">
                    <Stack gap={4}>
                        <Text size="m" fw={700} c="dimmed">Synopsis:</Text>
                        <Card padding="md" radius="md" withBorder bg={detailsBackground}>
                            <Text size="sm" fs="italic">{synopsis}</Text>
                        </Card>
                    </Stack>

                    <Stack gap={4}>
                        <Text size="m" fw={700} c="dimmed">Details:</Text>

                        <Table withTableBorder withColumnBorders>
                            <Table.Tbody>
                                <Table.Tr>
                                    <Table.Td fw={600} w="15%">Publisher</Table.Td>
                                    <Table.Td w="35%" bg={detailsBackground}>{publisher}</Table.Td>
                                    <Table.Td fw={600}>Genre</Table.Td>
                                    <Table.Td bg={detailsBackground}>{genre}</Table.Td>
                                </Table.Tr>

                                <Table.Tr>
                                    <Table.Td fw={600}>Year</Table.Td>
                                    <Table.Td bg={detailsBackground}>{year}</Table.Td>
                                    <Table.Td fw={600}>Language</Table.Td>
                                    <Table.Td bg={detailsBackground}>{language}</Table.Td>
                                </Table.Tr>

                                <Table.Tr>
                                    <Table.Td fw={600}>Pages</Table.Td>
                                    <Table.Td bg={detailsBackground}>{pages}</Table.Td>
                                    <Table.Td fw={600}>Binding</Table.Td>
                                    <Table.Td bg={detailsBackground}>{binding}</Table.Td>
                                </Table.Tr>

                                <Table.Tr>
                                    <Table.Td fw={600}>Weight</Table.Td>
                                    <Table.Td bg={detailsBackground}>{weight}</Table.Td>
                                    <Table.Td fw={600}>Dimensions</Table.Td>
                                    <Table.Td bg={detailsBackground}>{dimensions}</Table.Td>
                                </Table.Tr>
                            </Table.Tbody>
                        </Table>
                    </Stack>
                </Stack>
            </Stack>
        </DefaultLayout>
    );
}

