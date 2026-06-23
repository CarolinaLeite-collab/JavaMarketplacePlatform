import { useContext, useEffect, useState, useMemo } from "react";
import { useDisclosure } from "@mantine/hooks";
import { ActionIcon, Button, Group, Select, Text } from "@mantine/core";
import { IconPlus, IconTag, IconSortAscending, IconSortDescending } from "@tabler/icons-react";

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

/**
 * Sorts items by the given field using locale-aware, case-insensitive comparison.
 * Items with null/missing values for the field are treated as empty strings,
 * so they sort first in ascending order and last in descending order.
 *
 * @param {Array<Object>} items - the library items to sort
 * @param {string|null} sortBy - one of the SORT_OPTIONS values, or null/falsy for no sorting
 * @param {'asc'|'desc'} direction - sort direction, defaults to ascending semantics
 * @returns {Array<Object>} a new sorted array; the input array is never mutated
 */
function sortItems(items, sortBy, direction) {
    if (!sortBy) return items;

    const sorted = [...items].sort((a, b) => {
        const valA = a[sortBy] ?? "";
        const valB = b[sortBy] ?? "";
        return String(valA).localeCompare(String(valB), undefined, { sensitivity: "base" });
    });

    return direction === "desc" ? sorted.reverse() : sorted;
}

/**
 * Checks whether at least one item has a populated (non-null, non-empty) value
 * for the given sort field. Used to warn the user when a sort criterion currently
 * has no usable data — e.g. while the backend summary endpoint doesn't yet return
 * authorName/publicationType/identifier (see issue #1122).
 *
 * @param {Array<Object>} items - the library items to check
 * @param {string|null} sortBy - one of the SORT_OPTIONS values, or null/falsy
 * @returns {boolean} true if sortBy is falsy, or if any item has a usable value
 */
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
    const [sortDirection, setSortDirection] = useState("asc");

    useEffect(() => {
        if (libraryHref) {
            getLibrary(dispatch, libraryHref);
        }
    }, [dispatch, libraryHref]);

    const sortedItems = useMemo(
        () => sortItems(state.items, sortBy, sortDirection),
        [state.items, sortBy, sortDirection]
    );

    const sortHasData = useMemo(
        () => hasDataForSort(state.items, sortBy),
        [state.items, sortBy]
    );

    const toggleSortDirection = () => {
        setSortDirection(current => (current === "asc" ? "desc" : "asc"));
    };

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

                {sortBy && (
                    <ActionIcon
                        variant="default"
                        radius="xl"
                        size="lg"
                        onClick={toggleSortDirection}
                        aria-label={sortDirection === "asc" ? "Sort ascending" : "Sort descending"}
                        title={sortDirection === "asc" ? "Ascending" : "Descending"}
                    >
                        {sortDirection === "asc" ? <IconSortAscending size={18} /> : <IconSortDescending size={18} />}
                    </ActionIcon>
                )}

            </Group>

            <Text size="xs" c="dimmed" ta="center" mt={6} aria-live="polite">
                {sortBy && !sortHasData ? "No data available to sort by this field yet" : ""}
            </Text>

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