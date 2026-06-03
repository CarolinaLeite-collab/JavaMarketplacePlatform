import { Modal, Button, Stack, Text, Group } from '@mantine/core';
import { IconTrash } from '@tabler/icons-react';
import { ActionIcon, Tooltip } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { useContext } from 'react';
import AppContext from '../../context/AppContext';
import { deleteList } from '../../context/lists/ListsActions';

interface DeleteListModalProps {
    listName: string;
    links: { rel: string; href: string }[];
    myListsHref: string | null;
}

export function DeleteListModal({ listName, links }: DeleteListModalProps) {
    const [opened, { open, close }] = useDisclosure(false);
    const { dispatch, state } = useContext(AppContext);
    const { myListsHref } = state.app;


    const handleDelete = async () => {
        await deleteList(dispatch, links, myListsHref);
        close();
    };

    return (
        <>
            <Modal
                opened={opened}
                onClose={close}
                title={`Delete "${listName}"`}
                overlayProps={{ backgroundOpacity: 0.55, blur: 3 }}
                centered
                size="sm"
            >
                <Stack>
                    <Text fz="sm" c="dimmed">Are you sure you want to delete "{listName}"? This action cannot be undone.</Text>
                    <Group justify="flex-end" grow>
                        <Button variant="default" radius="xl" onClick={close}>No</Button>
                        <Button color="red" radius="xl" onClick={handleDelete}>Yes</Button>
                    </Group>
                </Stack>
            </Modal>

            <Tooltip label="Delete list" withArrow>
                <ActionIcon
                    variant="filled"
                    color="red"
                    size="md"
                    radius="sm"
                    onClick={open}
                >
                    <IconTrash size={20} stroke={1.5} />
                </ActionIcon>
            </Tooltip>
        </>
    );
}