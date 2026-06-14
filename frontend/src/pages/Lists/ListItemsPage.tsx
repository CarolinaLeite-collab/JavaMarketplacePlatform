import { DefaultLayout } from "../../components/layout/DefaultLayout.tsx";
import { Table, ActionIcon, Affix, Container, Center, Text } from "@mantine/core";
import { IconPlus } from "@tabler/icons-react";
import { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import AppContext from "../../context/AppContext.tsx";
import { addItemToList } from "../../context/lists/ListsActions.jsx";
import { AddItemToListDropDown } from "../../components/addItemToListModal/AddItemToListDropDown.tsx";
import { DeleteItemFromListModal } from "../../components/lists/DeleteItemFromListModal.tsx";
import { apiClient } from "../../services/apiClient";

interface ItemDTO {
    itemId: string;
    title: string;
    authorName: string;
    publishingYear: number;
    condition: string;
    description: string;
}

export default function ListItemsPage() {
    const { listId } = useParams();
    const { state, dispatch } = useContext(AppContext);
    const { libraryHref } = state.app;

    const [footerHeight, setFooterHeight] = useState(0);
    const [listName, setListName] = useState("");
    const [items, setItems] = useState<ItemDTO[]>([]);
    const [links, setLinks] = useState<{ rel: string; href: string }[]>([]);

    // Footer height for floating button
    useEffect(() => {
        const footer = document.querySelector("footer");
        if (footer) setFooterHeight(footer.offsetHeight);
    }, []);

    // Fetch list + items + HAL links
    useEffect(() => {
        if (!listId) return;

        async function fetchList() {
            try {
                const data = await apiClient.getByHref(
                    `http://localhost:8081/my-lists/${listId}`);

                setListName(data.name);

                // Extract HAL links
                const halLinks= data._links
                    ? Object.entries(data._links).map(([rel, val]: [string, any]) => ({
                        rel,
                        href: val.href,
                    }))
                    : [];
                setLinks(halLinks);

                // Check if there's an items href (public list path)
                const itemsLink = halLinks.find(l => l.rel === 'items');

                if (itemsLink) {
                    // Public list — fetch item IDs from the items endpoint
                    const itemIds = await apiClient.getByHref(itemsLink.href) as string[];
                    const itemIds2 = (itemIds ?? []) as string[];
                    const itemObjects = await Promise.all(
                        itemIds2.map((id) => apiClient.getItemById(id))
                    );
                    setItems(itemObjects);

                } else {
                    // Owner — item IDs are embedded directly in the list response
                    const itemIds = (data.itemIds ?? []) as string[];
                    const itemObjects = await Promise.all(
                        itemIds.map((id) => apiClient.getItemById(id))
                    );
                    setItems(itemObjects);
                }

            } catch (err) {
                console.error("Failed to fetch list", err);
            }
        }

        void fetchList();
    }, [listId]);

    return (
        <DefaultLayout
            title={listName || "List Items"}
            subtitle="CHECK OUT THE ITEMS IN THIS LIST:"
        >
            <Container>

                <Table striped highlightOnHover mt="md">
                    <Table.Thead>
                        <Table.Tr>
                            <Table.Th>Title</Table.Th>
                            <Table.Th>Author</Table.Th>
                            <Table.Th>Year</Table.Th>
                            <Table.Th>Remove</Table.Th>
                        </Table.Tr>
                    </Table.Thead>

                    <Table.Tbody>
                        {items.length > 0 ? (
                            items.map((item) => (
                                <Table.Tr key={item.itemId}>
                                    <Table.Td>{item.title}</Table.Td>
                                    <Table.Td>{item.authorName}</Table.Td>
                                    <Table.Td>{item.publishingYear}</Table.Td>

                                    <Table.Td>
                                        <Center>
                                            <DeleteItemFromListModal
                                                itemName={item.title}
                                                itemId={item.itemId}
                                                links={links}
                                            />
                                        </Center>
                                    </Table.Td>
                                </Table.Tr>
                            ))
                        ) : (
                            <Table.Tr>
                                <Table.Td colSpan={4}>
                                    <Text ta="center" fw={500}>
                                        No items in this list
                                    </Text>
                                </Table.Td>
                            </Table.Tr>
                        )}
                    </Table.Tbody>
                </Table>

                {/* Floating Add Item Button */}
                <Affix position={{ bottom: footerHeight + 76, right: 24 }} zIndex={90}>
                    <AddItemToListDropDown
                        listName={listName}
                        libraryHref={libraryHref}
                        existingItemIds={items.map((i) => i.itemId)}
                        onConfirm={(ids) =>
                            ids.forEach((id) => addItemToList(dispatch, links, id))
                        }
                    >
                        <ActionIcon
                            size="xl"
                            radius="xl"
                            color="blue"
                            variant="filled"
                        >
                            <IconPlus size={24} />
                        </ActionIcon>
                    </AddItemToListDropDown>
                </Affix>

            </Container>
        </DefaultLayout>
    );
}


