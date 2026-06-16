import { DefaultLayout } from "../../components/layout/DefaultLayout.tsx";
import { Table, Affix, ActionIcon, Container, Center, Text, Modal, Stack, Badge, Image, Group, Divider } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { IconPlus } from "@tabler/icons-react";
import { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import AppContext from "../../context/AppContext.tsx";
import { addItemToList } from "../../context/lists/ListsActions.jsx";
import { AddItemToListDropDown } from "../../components/addItemToListModal/AddItemToListDropDown.tsx";
import { DeleteItemFromListModal } from "../../components/lists/DeleteItemFromListModal.tsx";
import { apiClient } from "../../services/apiClient";
import { ItemDetailModal, ItemDTO } from "../../components/lists/ItemDetailModal.tsx";


export default function ListItemsPage() {
    const { listId } = useParams();
    const { state, dispatch } = useContext(AppContext);
    const { libraryHref } = state.app;

    const [footerHeight, setFooterHeight] = useState(0);
    const [listName, setListName] = useState("");
    const [items, setItems] = useState<ItemDTO[]>([]);
    const [links, setLinks] = useState<{ rel: string; href: string }[]>([]);
    const [selectedItem, setSelectedItem] = useState<ItemDTO | null>(null);
    const [modalOpened, { open: openModal, close: closeModal }] = useDisclosure(false);

    // True if the user owns this list — determined by presence of remove-item link
    const isOwner = links.some(l => l.rel === 'remove-item');

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
                const halLinks: { rel: string; href: string }[] = data._links
                    ? Object.entries(data._links).map(([rel, val]) => ({
                        rel,
                        href: (val as any).href,
                    }))
                    : [];
                setLinks(halLinks);

                // Always read itemsId directly from the list response
                const rawIds = (data.itemsId ?? []) as string[];

                if (rawIds.length === 0) {
                    setItems([]);
                    return;
                }

                const results = await Promise.allSettled(
                    rawIds.map((id) => apiClient.getItemById(id))
                );

                const resolved = results
                    .filter((r): r is PromiseFulfilledResult<any> => r.status === 'fulfilled')
                    .map(r => r.value);

                setItems(resolved);

            } catch (err) {
                console.error("Failed to fetch list", err);
            }
        }

        void fetchList();
    }, [listId]);

    const handleRowClick = (item: ItemDTO) => {
        setSelectedItem(item);
        openModal();
    };

    return (
        <DefaultLayout
            title={listName || "List Items"}
            subtitle="CHECK OUT THE ITEMS IN THIS LIST:"
        >
            <Container size="lg">

                <Table highlightOnHover
                       mt="md"
                       highlightOnHoverColor="var(--mantine-color-white)"
                       tableLayout="fixed"
                >
                    <Table.Thead>
                        <Table.Tr>
                            <Table.Th style={{ width: 80, fontWeight: 500 }}>Cover</Table.Th>
                            <Table.Th style={{ width: 250, fontWeight: 500 }}>Title</Table.Th>
                            <Table.Th style={{ width: 200, fontWeight: 500 }}>Author</Table.Th>
                            <Table.Th style={{ width: 80, fontWeight: 500 }}>Year</Table.Th>
                            {isOwner && (
                                <Table.Th style={{ width: 50, fontWeight: 500 }}>
                                    Remove
                                </Table.Th>
                            )}
                        </Table.Tr>
                    </Table.Thead>

                    <Table.Tbody>
                        {items.length > 0 ? (
                            items.map((item) => (
                                <Table.Tr
                                    key={item.itemId}
                                    style={{ cursor: "pointer" }}
                                    onClick={() => handleRowClick(item)}
                                >
                                    <Table.Td w={80}>
                                        {item.picture ? (
                                            <img
                                                src={item.picture}
                                                alt={item.title}
                                                style={{
                                                    width: 45,
                                                    height: 62,
                                                    objectFit: "contain",
                                                    borderRadius: 4,
                                                    display: "block"
                                                }}
                                            />
                                        ) : (
                                            <div style={{
                                                width: 45,
                                                height: 60,
                                                background: "var(--mantine-color-gray-2)",
                                                borderRadius: 4
                                            }} />
                                        )}
                                    </Table.Td>
                                    <Table.Td>{item.title}</Table.Td>
                                    <Table.Td>{item.authorName}</Table.Td>
                                    <Table.Td>{item.publishingYear}</Table.Td>
                                    {isOwner && (
                                        <Table.Td
                                            onClick={(e) => e.stopPropagation()}
                                        >
                                            <Center>
                                                <DeleteItemFromListModal
                                                    itemName={item.title}
                                                    itemId={item.itemId}
                                                    links={links}
                                                />
                                            </Center>
                                        </Table.Td>
                                    )}
                                </Table.Tr>
                            ))
                        ) : (
                            <Table.Tr>
                                <Table.Td colSpan={isOwner ? 5 : 4}>
                                    <Text ta="center" fw={500}>
                                        No items in this list
                                    </Text>
                                </Table.Td>
                            </Table.Tr>
                        )}
                    </Table.Tbody>
                </Table>

                {/* Floating Add Item Button */}
                {isOwner && (
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
                                aria-label="Add item"
                            >
                                <IconPlus size={24} />
                            </ActionIcon>
                        </AddItemToListDropDown>
                    </Affix>
                )}
            </Container>

            <ItemDetailModal
                item={selectedItem}
                opened={modalOpened}
                onClose={closeModal}
            />

        </DefaultLayout>
    );
}


