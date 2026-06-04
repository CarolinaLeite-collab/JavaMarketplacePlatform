import { useState, useEffect } from 'react';
import { useDisclosure } from '@mantine/hooks';
import {
    Popover, ActionIcon, ScrollArea, Checkbox,
    Stack, Button, Group, Text, Divider, Loader, Center, Tooltip,
} from '@mantine/core';
import { IconPlus } from '@tabler/icons-react';
import { apiClient } from '../../services/apiClient';

interface LibraryItem {
    itemId: string;
    title: string;
}

interface Props {
    listName: string;
    libraryHref: string | null;
    existingItemIds: string[];
    onConfirm: (selectedIds: string[]) => void;
}

export function AddItemToListDropDown({ listName, libraryHref, existingItemIds, onConfirm }: Props) {
    const [opened, { open, close }] = useDisclosure(false);
    const [selected, setSelected] = useState<string[]>([]);
    const [items, setItems] = useState<LibraryItem[]>([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (!opened || !libraryHref) return;
        setLoading(true);
        apiClient.getByHref(libraryHref)
            .then((data: any) => {
                const embedded = data._embedded;
                const key = Object.keys(embedded)[0];
                setItems(embedded[key] ?? []);
            })
            .catch(() => setItems([]))
            .finally(() => setLoading(false));
    }, [opened, libraryHref]);

    const toggle = (id: string) =>
        setSelected(prev =>
            prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
        );

    const handleClose = () => {
        setSelected([]);
        close();
    };

    const handleConfirm = () => {
        onConfirm(selected);
        handleClose();
    };

    return (
        <Popover
            opened={opened}
            onClose={handleClose}
            position="bottom"
            withArrow
            shadow="md"
            width={320}
        >
            <Popover.Target>
                <ActionIcon
                    variant="filled"
                    color="indigo"
                    size="md"
                    radius="sm"
                    onClick={opened ? close : open}
                >
                    <IconPlus size={20} stroke={1.5} />
                </ActionIcon>
            </Popover.Target>

            <Popover.Dropdown p="sm">
                <Text fz="sm" fw={500} mb="xs">Add item to &quot;{listName}&quot;</Text>
                <Divider mb="xs" />

                <ScrollArea h={220} scrollbarSize={8}>
                    {loading ? (
                        <Center h={200}><Loader size="sm" /></Center>
                    ) : (
                        <Stack gap="xs" pr="xs">
                            {items.map(item => {
                                const alreadyAdded = existingItemIds.includes(item.itemId);
                                return (
                                    <Tooltip
                                        key={item.itemId}
                                        label="Already in this list"
                                        disabled={!alreadyAdded}
                                        position="right"
                                        withArrow
                                    >
                                        <div>
                                            <Checkbox
                                                label={item.title}
                                                checked={selected.includes(item.itemId)}
                                                onChange={() => toggle(item.itemId)}
                                                disabled={alreadyAdded}
                                            />
                                        </div>
                                    </Tooltip>
                                );
                            })}
                        </Stack>
                    )}
                </ScrollArea>

                <Divider mt="xs" mb="xs" />

                <Group justify="flex-end" gap="xs">
                    <Button variant="default" radius="xl" size="xs" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button
                        color="indigo"
                        radius="xl"
                        size="xs"
                        onClick={handleConfirm}
                        disabled={selected.length === 0}
                    >
                        Confirm
                    </Button>
                </Group>
            </Popover.Dropdown>
        </Popover>
    );
}
