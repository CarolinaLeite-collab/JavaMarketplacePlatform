import { Modal, ScrollArea, Group, Text, Button, Divider, Image, Paper, Box, Alert, Stack, } from '@mantine/core';
import AppContext from '../../context/AppContext';
import { useContext, useEffect, useState, } from 'react';
import { REMOVE_FROM_CART, CLEAR_CART, loadCart,
} from '../../context/cart/CartActions';
import { apiClient } from '../../services/apiClient';
import { useDisclosure } from '@mantine/hooks';

interface ShoppingCartProps {
    opened: boolean;
    onClose: () => void;
}

export function ShoppingCart({ opened, onClose }: ShoppingCartProps) {
    const { state, dispatch } = useContext(AppContext);
    const cartCount = state.cart?.items?.length ?? 0;
    const shoppingCartHref = state.app?.shoppingCartHref;
    const [pendingSale, setPendingSale] = useState(null);
    const [checkoutError, setCheckoutError] = useState('');
    const [checkoutLoading, setCheckoutLoading] = useState(false);
    const [checkoutOpened, {open: openCheckout, close: closeCheckout,},] = useDisclosure(false);
    const cartItems = state.cart?.items ?? [];
    const totalPrice = cartItems.reduce(
        (total, item) =>
            total + Number(item.priceValue ?? 0),
        0);
    const currency = cartItems[0]?.currency ?? '';

    useEffect(() => {
        if (shoppingCartHref) {
            loadCart(dispatch, shoppingCartHref)
                .catch(error => {
                    console.error(
                        'Could not load shopping cart',
                        error
                    );
                });
        }
    }, [shoppingCartHref, dispatch]);

    const handleRemove = async (item) => {
        if (!item.deleteHref) {
            return;
        }

        try {
            //Hateoas
            const allowedMethods =
                await apiClient.getAllowedMethodsByHref(
                    item.deleteHref
                );

            if (!allowedMethods.includes('DELETE')) {
                console.error(
                    'User is not allowed to remove this cart item'
                );
                return;
            }
            //
            await apiClient.deleteByHref(item.deleteHref);

            dispatch({
                type: REMOVE_FROM_CART,
                payload: { id: item.id },
            });
        } catch (error) {
            console.error('Could not remove item from cart', error);
        }
    };

    const handleClearCart = async () => {
        if (!shoppingCartHref || cartCount === 0) {
            return;
        }

        try {
            const cartDiscovery =
                await apiClient.getByHref(
                    shoppingCartHref
                );

            const cartHref =
                cartDiscovery?._links?.self?.href;

            if (!cartHref) {
                return;
            }
            const allowedMethods = await apiClient.getAllowedMethodsByHref(cartHref);

            if (!allowedMethods.includes('PATCH')) {
                console.error('User is not allowed to clear this cart');
                return;
            }

            await apiClient.patchNoBodyByHref(
                cartHref
            );

            dispatch({
                type: CLEAR_CART,
            });
        } catch (error) {
            console.error(
                'Could not clear shopping cart',
                error
            );
        }
    };

    const handleCheckout = async () => {
        if (cartCount === 0 || pendingSale || !shoppingCartHref) {
            return;
        }

        setCheckoutError('');
        setCheckoutLoading(true);

        try {
            const cartDiscovery = await apiClient.getByHref(shoppingCartHref);
            const cartHref = cartDiscovery?._links?.self?.href;

            if (!cartHref) {
                throw new Error('Shopping cart link is unavailable.');
            }

            const cart = await apiClient.getByHref(cartHref);
            const saleHref = cart?._links?.sale?.href;

            if (!saleHref) {
                throw new Error('Checkout link is unavailable.');
            }

            const allowedMethods = await apiClient.getAllowedMethodsByHref(saleHref);
            if (!allowedMethods.includes('POST')) {
                throw new Error('Checkout is not allowed.');
            }

            const shoppingCartId = new URL(cartHref).pathname
                .split('/')
                .filter(Boolean)
                .pop();
            const createdSale = await apiClient.postByHref(saleHref, {
                shoppingCartId,
            });
            const createdSaleHref = createdSale?._links?.self?.href;
            const saleId = createdSaleHref
                ? new URL(createdSaleHref).pathname.split('/').filter(Boolean).pop()
                : 'completed';

            setPendingSale({ saleId });
            dispatch({ type: CLEAR_CART });
            closeCheckout();
        } catch (error) {
            console.error('Could not complete purchase', error);
            setCheckoutError('Could not complete your purchase. Please try again.');
        } finally {
            setCheckoutLoading(false);
        }
    };

    return (
        <>
        <Modal
            opened={opened}
            onClose={onClose}
            title="Shopping Cart"
            size="lg"
            centered
        >
            {cartCount === 0 ? (
                <Text c="dimmed" ta="center" py="xl">Your cart is empty.</Text>
            ) : (
                <ScrollArea h={400}>
                    {cartItems.map((item) => (
                        <Paper key={item.id} withBorder radius="md" p="sm" mb="sm">
                            <Group wrap="nowrap">
                                <Image
                                    src={item.image}
                                    fallbackSrc="https://placehold.co/80x100?text=No+Image"
                                    w={65}
                                    h={85}
                                    radius="sm"
                                    fit="cover"
                                />

                                <Box style={{ flex: 1 }}>
                                    <Text fw={600}>{item.name}</Text>
                                </Box>

                                <Text fw={700} miw={100} ta="right">
                                    {Number(item.priceValue).toFixed(2)} {item.currency}
                                </Text>

                                <Button
                                    variant="subtle"
                                    color="red"
                                    aria-label={`Remove ${item.name} from cart`}
                                    onClick={() => handleRemove(item)}
                                >
                                    Remove
                                </Button>
                            </Group>
                        </Paper>
                    ))}
                </ScrollArea>
            )}

            {pendingSale && (
                <Alert
                    color="green"
                    title="Purchase completed"
                    mb="md"
                >
                    Sale {pendingSale.saleId} was completed
                    successfully.
                </Alert>
            )}

            {checkoutError && (
                <Alert color="red" title="Purchase failed" mb="md">
                    {checkoutError}
                </Alert>
            )}

            <Divider my="sm" />

            <Group justify="space-between">
                <Text fw={700} size="lg">
                    Total: {totalPrice.toFixed(2)} {currency}
                </Text>

                <Group>
                    <Button
                        variant="subtle"
                        color="red"
                        disabled={cartCount === 0}
                        onClick={handleClearCart}
                    >
                        Clear Cart
                    </Button>

                    <Button
                        color="var(--mantine-color-indigo-7)"
                        radius="xl"
                        disabled={
                            cartCount === 0 || !shoppingCartHref }
                        onClick={openCheckout}
                    >
                        {pendingSale
                            ? 'Purchase Completed'
                            : 'Checkout'}
                    </Button>
                </Group>
            </Group>
        </Modal>

            <Modal
                opened={checkoutOpened}
                onClose={closeCheckout}
                title="Confirm purchase"
                centered
            >
                <Stack>
                    <Text>
                        You are purchasing {cartCount} item(s).
                    </Text>

                    <Text fw={700} size="lg">
                        Total: {totalPrice.toFixed(2)} {currency}
                    </Text>

                    <Alert color="blue">
                        Payment simulation: no real payment
                        will be processed.
                    </Alert>

                    <Group justify="flex-end">
                        <Button
                            variant="default"
                            onClick={closeCheckout}
                        >
                            Cancel
                        </Button>

                        <Button
                            color="var(--mantine-color-indigo-7)"
                            onClick={handleCheckout}
                            loading={checkoutLoading}
                        >
                            Confirm Purchase
                        </Button>
                    </Group>
                </Stack>
            </Modal>
        </>
    );
}
