import { Modal, ScrollArea, Group, Text, Button, Divider, Image, Paper, Box, } from '@mantine/core';
import AppContext from '../../context/AppContext';
import { useContext, useEffect } from 'react';
import { REMOVE_FROM_CART, loadCart,
} from '../../context/cart/CartActions';
import { apiClient } from '../../services/apiClient';

interface ShoppingCartProps {
    opened: boolean;
    onClose: () => void;
}

export function ShoppingCart({ opened, onClose }: ShoppingCartProps) {
    const { state, dispatch } = useContext(AppContext);
    const cartCount = state.cart?.items?.length ?? 0;
    const shoppingCartHref = state.app?.shoppingCartHref;
    useEffect(() => {
        if (opened && shoppingCartHref) {
            loadCart(dispatch, shoppingCartHref)
                .catch(error => {
                    console.error(
                        'Could not load shopping cart',
                        error
                    );
                });
        }
    }, [opened, shoppingCartHref, dispatch]);

    const handleRemove = async (item) => {
        if (!item.deleteHref) {
            return;
        }

        try {
            await apiClient.deleteByHref(item.deleteHref);

            dispatch({
                type: REMOVE_FROM_CART,
                payload: { id: item.id },
            });
        } catch (error) {
            console.error('Could not remove item from cart', error);
        }
    };

    return (
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
                    {(state.cart?.items ?? []).map((item) => (
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
                                    {item.price}
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

            <Divider my="sm" />

            <Group justify="flex-end">
                <Button color="var(--mantine-color-indigo-7)" radius="xl">Checkout</Button>
            </Group>
        </Modal>
    );
}
