import {Modal, Group, Stack, Text, Image, Badge, Divider, Button, Box} from "@mantine/core";

interface SaleItemDetail {
    cover: string;
    title: string;
    author: string;
    genre: string;
    condition: string;
    price: string | null;
    seller: string;
    saleType: "Direct Sale" | "Auction";
    directSaleId: string | null;
    auctionId: string | null;
    selfHref?: string | null;
}

interface SaleDetailsModalProps {
    opened: boolean;
    item: SaleItemDetail | null;
    canSeePrice: boolean;
    onClose?: () => void;
    onSeeMore?: () => void;
}

export function SaleDetailsModal({ opened, item, canSeePrice, onClose, onSeeMore }: SaleDetailsModalProps) {
    if (!item) return null;

    const handleSeeMore = () => {
        onSeeMore?.();
    };

    return (
        <Modal
            opened={opened}
            onClose={onClose}
            title={<Text fw={700} c="dark.8">{item.title}</Text>}
            centered
            overlayProps={{ backgroundOpacity: 0.55, blur: 4 }}
            size="md"
            transitionProps={{ duration: 0 }}
            closeButtonProps={{ "aria-label": "Close" }}
        >

            <Group align="flex-start" wrap="nowrap" >
                <Stack gap={10} w={155} align="center">
                    <Image
                        src={item.cover}
                        fallbackSrc="https://placehold.co/500x550?text=No+Image"
                        w={150}
                        radius="sm"
                        fit="cover"
                    />

                    <Text size="sm" c="dimmed">
                        Sold by: {item.seller}
                    </Text>
                </Stack>

                <Stack gap="lg" flex={1}>
                    <Text fz="sm"><b>Author:</b> {item.author}</Text>
                    <Text fz="sm"><b>Genre:</b> {item.genre}</Text>
                    <Text fz="sm"><b>Type:</b> {item.saleType}</Text>

                    <Group gap="xs">
                        <Text fz="sm"><b>Condition:</b></Text>
                        <Badge
                            ml="xs"
                            color={
                                item.condition === 'GOOD' ? 'teal'
                                    : item.condition === 'FAIR' ? 'yellow'
                                        : 'gray'
                            }
                            variant="light"
                        >
                            {item.condition}
                        </Badge>
                    </Group>

                    {canSeePrice ? (
                        item.price && (
                            <Box
                                mt={6}
                                px={10}
                                py={4}
                                style={{
                                    borderRadius: '999px',
                                    backgroundColor: '#eef0ff',
                                    display: 'inline-block',
                                    textAlign: 'center',
                                }}
                            >
                                <Text fw={600} size="md" c="#4f6df5">
                                    {item.price}
                                </Text>
                            </Box>
                        )
                    ) : (
                        <Text c="dimmed">
                            Register or log in to see price
                        </Text>
                    )}
                </Stack>
            </Group>


            <Divider my="xs" />
            <Button
                fullWidth
                radius="sm"
                size="md"
                onClick={handleSeeMore}
                style={{
                    backgroundColor: '#4f6df5',
                    height: 44,
                    boxShadow: '0 4px 10px rgba(79,109,245,0.35)',
                    fontWeight: 700,
                    fontSize: '16px',
                }}
            >
                See more
            </Button>
        </Modal>
    );
}
