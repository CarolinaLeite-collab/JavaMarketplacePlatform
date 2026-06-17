import { useContext, useEffect, useState, useMemo } from "react";
import { useDisclosure } from "@mantine/hooks";
import { Button, Group, Select, Text } from "@mantine/core";
import {IconPlus, IconTag} from "@tabler/icons-react";

import {DefaultLayout} from "../../components/layout/DefaultLayout.tsx";
import {ItemAccordion} from "@/components/accordion/ItemAccordion.js";
import {AddItemModal} from "@/components/addItemModal/AddItemModal.tsx";
import { CreateSaleModal } from "@/components/createSaleModal/CreateSaleModal.tsx";

import { useLibrary } from '../../context/AppContext';
import { getLibrary } from '../../context/library/LibraryActions';

import AppContext from "../../context/AppContext";

const SORT_OPTIONS = [
    { value: "title", label: "Title" },
    { value: "authorName", label: "Author" },
    { value: "publicationType", label: "Type" },
    { value: "identifier", label: "ISBN/ISSN" },
];

function sortItems(items, sortBy) {
    if (!sortBy) return items;

    return [...items].sort((a, b) => {
        const valA = a[sortBy] ?? "";
        const valB = b[sortBy] ?? "";
        return String(valA).localeCompare(String(valB), undefined, { sensitivity: "base" });
    });
}

function hasDataForSort(items, sortBy) {
    if (!sortBy) return true;
    return items.some(item => item[sortBy] != null && item[sortBy] !== "");
}

export default function MyLibraryPage() {

    const { state: appState } = useContext(AppContext);
    const { libraryHref } = appState.app;

    const { state, dispatch } = useLibrary();

    const [addItemOpened, { open: openAddItem, close: closeAddItem }] = useDisclosure(false);
    const [createSaleOpened, { open: openCreateSale, close: closeCreateSale }] = useDisclosure(false);
    const [sortBy, setSortBy] = useState(null);

    useEffect(() => {
        if (libraryHref) {
            getLibrary(dispatch, libraryHref);
        }
    }, [dispatch, libraryHref]);

    const sortedItems = useMemo(
        () => sortItems(state.items, sortBy),
        [state.items, sortBy]
    );

    const sortHasData = useMemo(
        () => hasDataForSort(state.items, sortBy),
        [state.items, sortBy]
    );

    return (
        <DefaultLayout title="My Library" subtitle="CHECK OUT YOUR ITEMS:">
            <Group justify="center" mt={0}>
                <Button
                    color="indigo"
                    radius="xl"
                    leftSection={<IconPlus size={16} />}
                    onClick={openAddItem}
                >
                    ADD ITEM
                </Button>

                <Button
                    color="grape"
                    radius="xl"
                    leftSection={<IconTag size={16} />}
                    onClick={openCreateSale}
                >
                    CREATE A SALE
                </Button>

                <Select
                    placeholder="Sort by"
                    data={SORT_OPTIONS}
                    value={sortBy}
                    onChange={setSortBy}
                    clearable
                    radius="xl"
                    w={160}
                />

            </Group>

            {sortBy && !sortHasData && (
                <Text size="xs" c="dimmed" ta="center" mt={6}>
                    No data available to sort by this field yet
                </Text>
            )}

            <ItemAccordion
                items={sortedItems}
                details={state.details}
                dispatch={dispatch}
            />

            <AddItemModal
                opened={addItemOpened}
                onClose={closeAddItem}
                onItemAdded={() => {
                    closeAddItem();
                    if (libraryHref) {
                        getLibrary(dispatch, libraryHref);
                    }
                }}
            />

            <CreateSaleModal
                opened={createSaleOpened}
                onClose={closeCreateSale}
            />

        </DefaultLayout>
    );
}