import {Box, Flex, Stack, Text, Image, Button, ActionIcon, Modal} from "@mantine/core";
import { IconX } from "@tabler/icons-react";

interface SaleItemDetail {
    cover: string;
    title: string;
    author: string;
    genre: string;
    condition: string;
    price: string;
    seller: string;
}

interface SaleDetailsModalProps {
    opened: boolean
    item: SaleItemDetail;
    onClose?: () => void;
    onSeeMore?: () => void;
}
export function SaleDetailsModal({ opened, item, onClose, onSeeMore }: SaleDetailsModalProps) {
    if (!item) {
        return (
            <Modal
                opened={opened}
                onClose={onClose}
                centered
                padding={0}
                radius="md"
                size={540}
                withCloseButton={false}
            >
                <Text>Loading sale details</Text>
            </Modal>
        );
    }

    const handleClose = () => {
        onClose?.();
    };

    const handleSeeMore = () => {
        onSeeMore?.();
    };

    return (
        <Modal
            opened={opened}              // ✅ use prop, not `open`
            onClose={handleClose}
            centered
            padding={0}
            radius="md"
            size={540}
            withCloseButton={false}
            overlayProps={{ backgroundOpacity: 0.55, blur: 3 }}
            styles={{
                content: {
                    borderRadius: '10px',
                    overflow: 'hidden',
                    backgroundColor: '#ffffff',
                    boxShadow: '0 6px 20px rgba(0,0,0,0.12)',
                },
                body: {
                    padding: '28px 26px 22px 26px',
                },
            }}
        >
            <Box pos="relative">
                <ActionIcon
                    variant="subtle"
                    color="gray"
                    onClick={handleClose}
                    aria-label="close"
                    style={{
                        position: 'absolute',
                        top: -8,
                        right: -8,
                        zIndex: 2,
                    }}
                >
                    <IconX size={28} stroke={1.8} />
                </ActionIcon>

                <Flex align="flex-start" gap={28} mb={18}>
                    <Stack gap={10} w={210}>
                        <Image
                            src={item.cover || ''}
                            alt={item.title}
                            radius={0}
                            style={{
                                width: 190,
                                height: 275,
                                objectFit: 'cover',
                                boxShadow: '0 4px 12px rgba(0,0,0,0.25)',
                            }}
                        />

                        <Text size="sm" c="#8a8a8a" tt="uppercase" fw={500}>
                            Sold by {item.seller}
                        </Text>
                    </Stack>

                    <Stack gap={10} flex={1} pt={8}>
                        <Text fw={700} size="xl" c="#111">
                            {item.title}
                        </Text>

                        <Box>
                            <Text size="sm" c="#9a9a9a" tt="uppercase" fw={500}>
                                Author
                            </Text>
                            <Text size="xl" c="#8a8a8a">
                                {item.author}
                            </Text>
                        </Box>

                        <Box>
                            <Text size="sm" c="#9a9a9a" tt="uppercase" fw={500}>
                                Genre
                            </Text>
                            <Text size="xl" c="#8a8a8a">
                                {item.genre}
                            </Text>
                        </Box>

                        <Box>
                            <Text size="sm" c="#9a9a9a" tt="uppercase" fw={500}>
                                Condition
                            </Text>
                            <Text size="xl" c="#8a8a8a">
                                {item.condition}
                            </Text>
                        </Box>

                        <Box
                            mt={18}
                            mx="auto"
                            px={32}
                            py={10}
                            style={{
                                borderRadius: '999px',
                                backgroundColor: '#eef0ff',
                                minWidth: 135,
                                textAlign: 'center',
                            }}
                        >
                            <Text fw={700} size="xl" c="#4f6df5">
                                {item.price}
                            </Text>
                        </Box>
                    </Stack>
                </Flex>

                <Button
                    fullWidth
                    radius="sm"
                    size="lg"
                    onClick={handleSeeMore}
                    style={{
                        backgroundColor: '#4f6df5',
                        height: 44,
                        boxShadow: '0 4px 10px rgba(79,109,245,0.35)',
                        fontWeight: 700,
                        fontSize: '18px',
                    }}
                >
                    See more
                </Button>
            </Box>
        </Modal>
    );
}