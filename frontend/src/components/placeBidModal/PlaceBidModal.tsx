import { useState } from 'react';
import { Modal, Stack, Text, NumberInput, Button, ActionIcon, Group } from '@mantine/core';
import { IconX } from '@tabler/icons-react';

interface PlaceBidModalProps {
    opened: boolean;
    currentPrice: number | null;
    currency: string;
    onClose?: () => void;
    onConfirm?: (bidValue: number) => void;
}

const presets = [5, 10, 20];

export function PlaceBidModal({
                                  opened,
                                  currentPrice,
                                  currency,
                                  onClose,
                                  onConfirm,
                              }: PlaceBidModalProps) {
    const [value, setValue] = useState<number | ''>('');
    const [error, setError] = useState<string | null>(null);

    const minBid = currentPrice ?? 0;

    const handleClose = () => {
        onClose?.();
    };

    const handleSubmit = () => {
        if (value === '' || value <= 0) {
            setError('Please enter a positive amount.');
            return;
        }
        if (value <= minBid) {
            setError(`Your bid must be greater than ${minBid} ${currency}.`);
            return;
        }

        setError(null);
        onConfirm?.(value);
    };

    return (
        <Modal
            opened={opened}
            onClose={handleClose}
            centered
            padding={24}
            radius="md"
            size={400}
            withCloseButton={false}
            overlayProps={{ backgroundOpacity: 0.55, blur: 3 }}
            styles={{
                content: {
                    borderRadius: '10px',
                    overflow: 'hidden',
                    backgroundColor: '#ffffff',
                    boxShadow: '0 6px 20px rgba(0,0,0,0.12)',
                },
            }}
        >
            <ActionIcon
                variant="subtle"
                color="gray"
                onClick={handleClose}
                aria-label="close"
                style={{
                    position: 'absolute',
                    top: 10,
                    right: 10,
                    zIndex: 2,
                }}
            >
                <IconX size={24} stroke={1.8} />
            </ActionIcon>

            <Stack gap="sm">
                <Text fw={700} size="xl" c="#111">
                    Place a bid
                </Text>

                <Text size="sm" c="dimmed">
                    Current bid: {minBid} {currency}
                </Text>

                <NumberInput
                    label="Your bid"
                    value={value}
                    onChange={setValue}
                    min={0}
                    decimalScale={2}
                    thousandSeparator=" "
                    suffix={` ${currency}`}
                    error={error}
                />
                <Group gap="sm">
                    {presets.map((increment) => (
                        <Button
                            key={increment}
                            variant="light"
                            radius="xl"
                            style={{ flex: 1 }}
                            onClick={() => {
                                const base = minBid || 0;
                                setValue(base + increment);
                                setError(null);
                            }}
                        >
                            +{increment} {currency}
                        </Button>
                    ))}
                </Group>


                <Group grow mt="sm">
                    <Button
                        variant="light"
                        radius="sm"
                        size="md"
                        onClick={handleClose}
                    >
                        Cancel
                    </Button>
                    <Button
                        radius="sm"
                        size="md"
                        onClick={handleSubmit}
                        style={{
                            backgroundColor: '#4f6df5',
                            height: 40,
                            boxShadow: '0 4px 10px rgba(79,109,245,0.35)',
                            fontWeight: 700,
                        }}
                    >
                        Confirm bid
                    </Button>
                </Group>
            </Stack>
        </Modal>
    );
}