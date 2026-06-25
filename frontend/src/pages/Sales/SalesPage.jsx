import { useContext, useEffect, useState } from 'react';
import {
    Accordion, Alert, Badge, Box, Divider, Group, Image, Loader, Stack, Table, Text
} from '@mantine/core';
import AppContext from '../../context/AppContext';
import { apiClient } from '../../services/apiClient';
import { DefaultLayout } from '../../components/layout/DefaultLayout.tsx';
import { IconChevronDown, IconReceipt } from '@tabler/icons-react';

function normalizeLinks(link) {
    if (!link) return [];
    return Array.isArray(link) ? link : [link];
}

function formatDate(date) {
    if (!date) return 'Pending';
    return date.split('T')[0];
}

function StatusBadge({ completedAt }) {
    if (completedAt) {
        return <Badge color="green" variant="light" size="sm">Completed</Badge>;
    }
    return <Badge color="yellow" variant="light" size="sm">Pending</Badge>;
}

async function loadSaleWithItemNames(sale) {
    const saleLineLinks = normalizeLinks(sale?._links?.['sale-line']);

    const itemNames = await Promise.all(
        saleLineLinks
            .filter(link => link?.href)
            .map(async link => {
                const saleLine = await apiClient.getByHref(link.href);
                const directSaleOptions = await apiClient.getDirectSalesOptions();
                const directSaleTemplate = directSaleOptions?._links?.['direct-sale']?.href;
                const directSaleHref = directSaleTemplate?.replace(
                    '{id}',
                    encodeURIComponent(saleLine.directSaleId),
                );

                if (!directSaleHref) return saleLine.directSaleId;

                const directSale = await apiClient.getByHref(directSaleHref);
                const itemLinks = normalizeLinks(directSale?._links?.item);
                const itemHref = itemLinks[0]?.href;

                if (!itemHref) return saleLine.directSaleId;

                const item = await apiClient.getByHref(itemHref);
                return item?.title ?? saleLine.directSaleId;
            }),
    );

    return {
        ...sale,
        itemName: itemNames.join(', '),
    };
}

function SaleLineDetails({ line }) {
    const [itemDetails, setItemDetails] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function loadDetails() {
            try {
                const directSale = await apiClient.getDirectSaleById(line.directSaleId);
                const itemId = directSale?.itemsId?.[0];
                if (itemId) {
                    const item = await apiClient.getItemById(itemId);
                    setItemDetails(item);
                }
            } catch (e) {
                console.warn('Could not load item details', e);
            } finally {
                setLoading(false);
            }
        }
        loadDetails();
    }, [line.directSaleId]);

    if (loading) {
        return (
            <Table.Tr>
                <Table.Td colSpan={4}><Text size="sm" c="dimmed">Loading...</Text></Table.Td>
            </Table.Tr>
        );
    }

    return (
        <Table.Tr>
            <Table.Td>
                <Group wrap="nowrap" gap="sm">
                    <Image
                        src={itemDetails?.picture}
                        fallbackSrc="https://placehold.co/50x65?text=No+Image"
                        w={50}
                        h={65}
                        radius="sm"
                        fit="cover"
                    />
                    <Stack gap={2}>
                        <Text size="sm" fw={500}>{itemDetails?.title ?? 'Unknown'}</Text>
                        <Text size="xs" c="dimmed">{itemDetails?.authorName ?? ''}</Text>
                    </Stack>
                </Group>
            </Table.Td>
            <Table.Td>
                <Text size="sm">{itemDetails?.condition ?? 'N/A'}</Text>
            </Table.Td>
            <Table.Td>
                <Text size="sm">{line.sellerId?.split('@')[0] ?? 'Unknown'}</Text>
            </Table.Td>
            <Table.Td>
                <Text size="sm" fw={600}>{line.price} {line.currency}</Text>
            </Table.Td>
        </Table.Tr>
    );
}

export default function SalesPage() {
    const { state } = useContext(AppContext);
    const salesHref = state.app.salesHref;

    const [sales, setSales] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!salesHref) {
            setSales([]);
            setLoading(false);
            return;
        }

        async function loadSales() {
            setLoading(true);
            setError(null);
            setSales([]);

            try {
                const collection = await apiClient.getByHref(salesHref);
                const links = normalizeLinks(collection?._links?.sale);

                const loadedSales = await Promise.all(
                    links
                        .filter(link => link?.href)
                        .map(async link => {
                            const sale = await apiClient.getByHref(link.href);
                            return loadSaleWithItemNames(sale);
                        })
                );

                setSales(loadedSales);
            } catch {
                setError('Could not load your purchases.');
            } finally {
                setLoading(false);
            }
        }

        loadSales();
    }, [salesHref]);

    if (loading) {
        return (
            <DefaultLayout title="Purchases" subtitle="CHECK OUT YOUR PURCHASES:">
                <Stack align="center" py="xl">
                    <Loader />
                    <Text c="dimmed">Loading purchases...</Text>
                </Stack>
            </DefaultLayout>
        );
    }

    if (error) {
        return (
            <DefaultLayout title="Purchases" subtitle="CHECK OUT YOUR PURCHASES:">
                <Alert color="red" title="Unable to load purchases">{error}</Alert>
            </DefaultLayout>
        );
    }

    return (
        <DefaultLayout title="Purchases" subtitle="CHECK OUT YOUR PURCHASES:">
            <Box w={{ base: '100%', sm: '80%', md: '65%' }} mx="auto">
                {sales.length === 0 && (
                    <Text c="dimmed" ta="center" py="xl">
                        You have no purchases yet.
                    </Text>
                )}

                <Accordion
                    variant="separated"
                    radius="md"
                    chevron={null}
                    styles={{
                        control: {
                            position: 'relative',
                            paddingBottom: 40,
                        },
                        content: {
                            paddingTop: 0,
                        },
                    }}
                >
                    {sales.map(sale => (
                        <Accordion.Item key={sale.saleId} value={sale.saleId}>
                            <Accordion.Control>
                                <Group wrap="wrap" justify="space-between">
                                    <Group wrap="nowrap">
                                        <IconReceipt size={30} stroke={1.5} />
                                        <Stack gap={3}>
                                            <Group gap="sm">
                                                <Text fw={600}>{sale.itemName}</Text>
                                                <StatusBadge completedAt={sale.completedAt} />
                                            </Group>
                                            <Text size="sm" c="dimmed">Sale ID: {sale.saleId}</Text>
                                            <Text size="sm" c="dimmed">
                                                Completed: {formatDate(sale.completedAt)}
                                            </Text>
                                        </Stack>
                                    </Group>
                                    <Text fw={700}>
                                        Total: {Number(sale.totalAmount).toFixed(2)} {sale.currency}
                                    </Text>
                                </Group>
                                <Box
                                    style={{
                                        position: 'absolute',
                                        bottom: 1,
                                        left: 20,
                                        right: 20,
                                        display: 'flex',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                    }}
                                >
                                    <Divider style={{ flex: 1 }} />
                                    <Box
                                        style={{
                                            width: 28,
                                            height: 28,
                                            borderRadius: '50%',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            margin: '0 8px',
                                        }}
                                    >
                                        <IconChevronDown size={14} color="var(--mantine-color-gray-5)" />
                                    </Box>
                                    <Divider style={{ flex: 1 }} />
                                </Box>
                            </Accordion.Control>
                            <Accordion.Panel>
                                <Table withTableBorder withColumnBorders>
                                    <Table.Thead>
                                        <Table.Tr>
                                            <Table.Th>Item</Table.Th>
                                            <Table.Th>Condition</Table.Th>
                                            <Table.Th>Seller</Table.Th>
                                            <Table.Th>Price</Table.Th>
                                        </Table.Tr>
                                    </Table.Thead>
                                    <Table.Tbody>
                                        {(sale.saleLines ?? []).map(line => (
                                            <SaleLineDetails key={line.saleLineId} line={line} />
                                        ))}
                                    </Table.Tbody>
                                </Table>
                            </Accordion.Panel>
                        </Accordion.Item>
                    ))}
                </Accordion>
            </Box>
        </DefaultLayout>
    );
}