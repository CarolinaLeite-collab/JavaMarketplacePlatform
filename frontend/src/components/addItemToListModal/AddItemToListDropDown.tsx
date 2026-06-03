import { useState } from 'react';
import { useDisclosure } from '@mantine/hooks';
import {
    Popover, ActionIcon, ScrollArea, Checkbox,
    Stack, Button, Group, Text, Divider,
} from '@mantine/core';
import { IconPlus } from '@tabler/icons-react';

interface LibraryItem {
    itemId: string;
    title: string;
    authorName: string;
}

const mockItems: LibraryItem[] = [
    { itemId: 'ITEM-001', title: 'The War of the Worlds', authorName: 'H.G. Wells' },
    { itemId: 'ITEM-002', title: 'Dune', authorName: 'Frank Herbert' },
    { itemId: 'ITEM-003', title: '1984', authorName: 'George Orwell' },
    { itemId: 'ITEM-004', title: 'Brave New World', authorName: 'Aldous Huxley' },
    { itemId: 'ITEM-005', title: 'Fahrenheit 451', authorName: 'Ray Bradbury' },
    { itemId: 'ITEM-006', title: 'The Hobbit', authorName: 'J.R.R. Tolkien' },
    { itemId: 'ITEM-007', title: 'Neuromancer', authorName: 'William Gibson' },
];

interface Props {
    listName: string;
    onConfirm: (selectedIds: string[]) => void;
}

export function AddItemToListDropDown({ listName, onConfirm }: Props) {
    const [opened, { open, close }] = useDisclosure(false);
    const [selected, setSelected] = useState<string[]>([]);

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
                    <Stack gap="xs" pr="xs">
                        {mockItems.map(item => (
                            <Checkbox
                                key={item.itemId}
                                label={`${item.title} — ${item.authorName}`}
                                checked={selected.includes(item.itemId)}
                                onChange={() => toggle(item.itemId)}
                            />
                        ))}
                    </Stack>
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