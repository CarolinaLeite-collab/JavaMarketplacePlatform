import { Modal, Button, Stack, Text, Group } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { useContext } from "react";
import AppContext from "../../context/AppContext.tsx";
import { removeItemFromList } from "../../context/lists/ListsActions.jsx";

interface DeleteItemFromListModalProps {
    itemName: string;
    itemId: string;
    links: { rel: string; href: string }[];
}

export function DeleteItemFromListModal({ itemName, itemId, links }: DeleteItemFromListModalProps) {
    const [opened, { open, close }] = useDisclosure(false);
    const { dispatch } = useContext(AppContext);

    const handleDelete = async () => {
        await removeItemFromList(dispatch, links, itemId);
        close();
    };

    return (
        <>
            <Button color="red" variant="subtle" onClick={open}>
                Remove
            </Button>

            <Modal
                opened={opened}
                onClose={close}
                title={`Remove "${itemName}"`}
                centered
                overlayProps={{ backgroundOpacity: 0.55, blur: 3 }}
                closeButtonProps={{ "aria-label": "Close" }}
                transitionProps={{ duration: 0 }}
            >
                <Stack>
                    <Text fz="sm" c="dimmed">
                        Are you sure you want to remove this item from the list?
                    </Text>

                    <Group justify="flex-end" grow>
                        <Button variant="default" radius="xl" onClick={close}>
                            Cancel
                        </Button>
                        <Button color="red" radius="xl" onClick={handleDelete}>
                            Remove
                        </Button>
                    </Group>
                </Stack>
            </Modal>
        </>
    );
}
