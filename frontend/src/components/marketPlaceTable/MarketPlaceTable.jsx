import { useState } from 'react';
import {
    IconChevronDown,
    IconChevronUp,
    IconSearch,
    IconSelector,
} from '@tabler/icons-react';
import {
    Center,
    Checkbox,
    Group,
    ScrollArea,
    Select,
    Stack,
    Table,
    Text,
    TextInput,
    UnstyledButton,
} from '@mantine/core';
import classes from './MarketPlaceTable.module.css';


function Th({ children, reversed, sorted, onSort, width }) {
    const Icon = sorted ? (reversed ? IconChevronUp : IconChevronDown) : IconSelector;

    return (
        <Table.Th style={{ width }}>
            <UnstyledButton onClick={onSort} className={classes.control}>
                <Group justify="space-between">
                    <Text fw={500} fz="sm">{children}</Text>
                    <Center className={classes.icon}>
                        <Icon size={16} stroke={1.5} />
                    </Center>
                </Group>
            </UnstyledButton>
        </Table.Th>
    );
}

function matchesGenreFilter(item, selectedGenre) {
    return selectedGenre === 'all' || item.genreId === selectedGenre;
}

function matchesTypeFilter(item, showDirectSales, showAuctions) {
    if (!showDirectSales && !showAuctions) return true;
    if (showDirectSales && showAuctions) return true;
    if (showDirectSales) return item.type === 'Direct Sale';
    return item.type === 'Auction';
}

function filterItems(items, selectedGenre, showDirectSales, showAuctions, search) {
    const query = search.toLowerCase().trim();

    return items.filter((item) => {
        const matchesFilters =
            matchesGenreFilter(item, selectedGenre) &&
            matchesTypeFilter(item, showDirectSales, showAuctions);

        if (!matchesFilters) return false;
        if (!query) return true;

        return ['item', 'genreName', 'type', 'price'].some((key) =>
            String(item[key]).toLowerCase().includes(query)
        );
    });
}

function sortItems(items, { sortBy, reversed, search, selectedGenre, showDirectSales, showAuctions }) {
    const filteredItems = filterItems(items, selectedGenre, showDirectSales, showAuctions, search);

    if (!sortBy) return filteredItems;

    return [...filteredItems].sort((a, b) => {
        const aValue = String(a[sortBy] ?? '');
        const bValue = String(b[sortBy] ?? '');

        return reversed
            ? bValue.localeCompare(aValue)
            : aValue.localeCompare(bValue);
    });
}

export function MarketPlaceTable({
    items,
    genres,
    selectedGenre,
    onGenreChange,
    showDirectSales,
    showAuctions,
    onShowDirectSalesChange,
    onShowAuctionsChange,
    canSeePrice,
}) {
    const [search, setSearch] = useState('');
    const [sortBy, setSortBy] = useState(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);

    const sortedItems = sortItems(items, {
        sortBy,
        reversed: reverseSortDirection,
        search,
        selectedGenre,
        showDirectSales,
        showAuctions,
    });

    const setSorting = (field) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
    };

    const rows = sortedItems.map((item) => (
        <Table.Tr key={item.id}>
            <Table.Td>{item.item}</Table.Td>
            <Table.Td>{item.genreName}</Table.Td>
            <Table.Td>{item.type}</Table.Td>
            {canSeePrice && <Table.Td>{item.price}</Table.Td>}
        </Table.Tr>
    ));

    return (
        <Stack gap="md">
            <Group justify="center" gap="xl">
                <Checkbox
                    label="Auction"
                    checked={showAuctions}
                    onChange={(event) => onShowAuctionsChange(event.currentTarget.checked)}
                />
                <Checkbox
                    label="Direct Sale"
                    checked={showDirectSales}
                    onChange={(event) => onShowDirectSalesChange(event.currentTarget.checked)}
                />
            </Group>

            <Select
                label="Genre"
                data={genres}
                value={selectedGenre}
                onChange={(value) => onGenreChange(value ?? 'all')}
            />

            <ScrollArea>
                <TextInput
                    placeholder="Search by item, genre, type or price"
                    mb="md"
                    leftSection={<IconSearch size={16} stroke={1.5} />}
                    value={search}
                    onChange={(event) => setSearch(event.currentTarget.value)}
                />

                <Table horizontalSpacing="md" verticalSpacing="xs" miw={900} layout="fixed">
                    <Table.Thead>
                        <Table.Tr>
                            <Th
                                width="32%"
                                sorted={sortBy === 'item'}
                                reversed={reverseSortDirection}
                                onSort={() => setSorting('item')}
                            >
                                Item
                            </Th>
                            <Th
                                width="22%"
                                sorted={sortBy === 'genreName'}
                                reversed={reverseSortDirection}
                                onSort={() => setSorting('genreName')}
                            >
                                Genre
                            </Th>
                            <Th
                                width="24%"
                                sorted={sortBy === 'type'}
                                reversed={reverseSortDirection}
                                onSort={() => setSorting('type')}
                            >
                                Type
                            </Th>
                            {canSeePrice && (
                                <Th
                                    width="22%"
                                    sorted={sortBy === 'price'}
                                    reversed={reverseSortDirection}
                                    onSort={() => setSorting('price')}
                                >
                                    Price
                                </Th>
                            )}
                        </Table.Tr>
                    </Table.Thead>
                    <Table.Tbody>
                        {rows.length > 0 ? (
                            rows
                        ) : (
                            <Table.Tr>
                                <Table.Td colSpan={canSeePrice ? 4 : 3}>
                                    <Text fw={500} ta="center">Nothing found</Text>
                                </Table.Td>
                            </Table.Tr>
                        )}
                    </Table.Tbody>
                </Table>
            </ScrollArea>
        </Stack>
    );
}
