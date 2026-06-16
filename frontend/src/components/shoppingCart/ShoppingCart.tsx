import { Modal, ScrollArea, Group, Text, Button, Divider } from '@mantine/core';
import AppContext from '../../context/AppContext';
import { useContext } from 'react';

interface ShoppingCartProps {
    opened: boolean;
    onClose: () => void;
}

export function ShoppingCart({ opened, onClose }: ShoppingCartProps) {
    const { state } = useContext(AppContext);
    const cartCount = state.cart?.items?.length ?? 0;

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
                        <Group key={item.id} justify="space-between" py="sm">
                            <Text>{item.name}</Text>
                            <Text fw={600}>{item.price}</Text>
                        </Group>
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