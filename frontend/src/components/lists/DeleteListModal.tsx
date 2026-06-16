import { Modal, Button, Stack, Text } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { deleteList } from "../../context/lists/ListsActions.jsx";
import { useContext } from "react";
import AppContext from "../../context/AppContext.tsx";

interface DeleteListModalProps {
    listName: string;
    links: { rel: string; href: string }[];
    myListsHref: string | null;
}

export function DeleteListModal({ listName, links, myListsHref }: DeleteListModalProps) {
    const [opened, { open, close }] = useDisclosure(false);
    const { dispatch } = useContext(AppContext);

    const handleDelete = async () => {
        await deleteList(dispatch, links, myListsHref);
        close();
    };

    return (
        <>
            <Button color="red" variant="subtle" onClick={open}>
                Delete
            </Button>

            <Modal
                opened={opened}
                onClose={close}
                title={`Delete "${listName}"`}
                centered
                transitionProps={{ duration: 0 }}
                closeButtonProps={{ "aria-label": "Close" }}
            >
                <Stack>
                    <Text>Are you sure you want to delete this list?</Text>
                    <Button color="red" onClick={handleDelete}>Confirm</Button>
                </Stack>
            </Modal>
        </>
    );
}
