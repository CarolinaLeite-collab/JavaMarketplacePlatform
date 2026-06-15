import { Text, Modal, Stack, Badge, Image, Group, Divider } from "@mantine/core";

export interface ItemDTO {
    itemId: string;
    title: string;
    authorName: string;
    publishingYear: number;
    condition: string;
    description: string;
    identifier: string;
    publicationTypeName: string;
    picture: string | null;
}

export function ItemDetailModal({ item, opened, onClose }: {
    item: ItemDTO | null;
    opened: boolean;
    onClose: () => void;
}) {
    if (!item) return null;

    return (
        <Modal
            opened={opened}
            onClose={onClose}
            title={<Text fw={600} c="dark.8">{item.title}</Text>}
            centered
            overlayProps={{ backgroundOpacity: 0.55, blur: 4 }}
            size="md"
        >
            <Group align="flex-start" gap="md">
                {item.picture ? (
                    <Image
                        src={item.picture}
                        alt={item.title}
                        w={120}
                        h={160}
                        fit="contain"
                        radius="sm"
                    />
                ) : (
                    <div style={{
                        width: 120,
                        height: 160,
                        background: 'var(--mantine-color-gray-2)',
                        borderRadius: 8,
                        flexShrink: 0
                    }} />
                )}

                <Stack gap="xs" style={{ flex: 1 }}>
                    <Text fz="sm"><b>Author:</b> {item.authorName}</Text>
                    <Text fz="sm"><b>Year:</b> {item.publishingYear}</Text>
                    <Text fz="sm"><b>Identifier:</b> {item.identifier || '--'}</Text>
                    <Text fz="sm"><b>Type:</b> {item.publicationTypeName || '--'}</Text>

                    <Group gap="xs">
                        <Text fz="sm"><b>Condition:</b></Text>
                        <Badge ml="xs" color={
                            item.condition === 'GOOD' ? 'teal' :
                                item.condition === 'FAIR' ? 'yellow' : 'gray'
                        } variant="light">
                            {item.condition}
                        </Badge>
                    </Group>
                </Stack>
            </Group>

            <Divider my="md" />

            <Text fz="sm">
                <b>Description:</b><br />
                {item.description}
            </Text>
        </Modal>
    );
}