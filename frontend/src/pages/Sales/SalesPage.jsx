import { useContext, useEffect, useState } from 'react';
import { Alert, Box, Group, Loader, Paper, Stack, Text } from '@mantine/core';
import { Link } from 'react-router-dom';
import AppContext from '../../context/AppContext';
import { apiClient } from '../../services/apiClient';
import { DefaultLayout } from '../../components/layout/DefaultLayout.tsx';
import { IconChevronRight, IconReceipt } from '@tabler/icons-react';

function normalizeLinks(link) {
    if (!link) return [];
    return Array.isArray(link) ? link : [link];
}

function formatDate(date) {
    if (!date) return 'Pending';

    return date.split('T')[0];
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
            <DefaultLayout title="My Purchases" subtitle="CHECK OUT YOUR PURCHASES:">
                <Alert color="red" title="Unable to load purchases">{error}</Alert>
            </DefaultLayout>
        );
    }

    return (
        <DefaultLayout title="My Purchases" subtitle="CHECK OUT YOUR PURCHASES:">
            <Box w={{ base: '100%', sm: '80%', md: '65%' }} mx="auto">
            {sales.length === 0 && (
                <Text c="dimmed" ta="center" py="xl">
                    You have no purchases yet.
                </Text>
            )}

            {sales.map(sale => (
                <Paper
                    key={sale.saleId}
                    component={Link}
                    to={`/sales/${sale.saleId}`}
                    withBorder
                    radius="md"
                    p="lg"
                    mb="sm"
                    style={{ color: 'inherit', textDecoration: 'none' }}
                >
                    <Group wrap="wrap" justify="space-between">
                        <Group wrap="nowrap">
                            <IconReceipt size={30} stroke={1.5} />
                            <Stack gap={3}>
                                <Text fw={600}>{sale.itemName}</Text>
                                <Text size="sm" c="dimmed">Sale ID: {sale.saleId}</Text>
                                <Text size="sm" c="dimmed">
                                    Completed: {formatDate(sale.completedAt)}
                                </Text>
                            </Stack>
                        </Group>

                        <Group wrap="nowrap" gap="sm">
                            <Text fw={700}>
                                Total: {sale.totalAmount} {sale.currency}
                            </Text>
                            <IconChevronRight size={20} />
                        </Group>
                    </Group>
                </Paper>
            ))}
            </Box>
        </DefaultLayout>
    );
}
