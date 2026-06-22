import { useContext, useEffect, useState } from 'react';
import { Alert, Anchor, Loader, Stack, Text } from '@mantine/core';
import { Link } from 'react-router-dom';
import AppContext from '../../context/AppContext';
import { apiClient } from '../../services/apiClient';

function normalizeLinks(link) {
    if (!link) return [];
    return Array.isArray(link) ? link : [link];
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
                        .map(link => apiClient.getByHref(link.href))
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
            <Stack align="center">
                <Loader />
                <Text>Loading purchases...</Text>
            </Stack>
        );
    }
    if (error) return <Alert color="red">{error}</Alert>;

    return (
        <Stack>
            <Text component="h1" size="xl" fw={700}>
                My Purchases
            </Text>

            {sales.length === 0 && (
                <Text c="dimmed">You have no purchases yet.</Text>
            )}

            {sales.map(sale => (
                <Stack key={sale.saleId} gap="xs">
                    <Anchor
                        component={Link}
                        to={`/sales/${sale.saleId}`}
                    >
                        Sale ID: {sale.saleId}
                    </Anchor>

                    <Text>Created: {sale.createdAt}</Text>

                    <Text>
                        Completed: {sale.completedAt ?? 'Pending'}
                    </Text>

                    <Text>
                        Total: {sale.totalAmount} {sale.currency}
                    </Text>
                </Stack>
            ))}
        </Stack>
    );
}