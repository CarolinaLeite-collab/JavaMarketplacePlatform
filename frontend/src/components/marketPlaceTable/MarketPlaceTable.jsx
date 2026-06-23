import {useState} from 'react';
import {IconChevronDown, IconChevronUp, IconSearch, IconSelector,} from '@tabler/icons-react';
import {
    Center,
    Checkbox,
    Group,
    MultiSelect,
    ScrollArea,
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

function matchesMultiFilter(value, selected) {
    return selected.length === 0 || selected.includes(value);
}

function matchesGenreFilter(item, selectedGenres) {
    return selectedGenres.length === 0 || selectedGenres.includes(item.genreId);
}

function matchesTypeFilter(item, showDirectSales, showAuctions) {
    if (!showDirectSales && !showAuctions) return true;
    if (showDirectSales && showAuctions) return true;
    if (showDirectSales) return item.type === 'Direct Sale';
    return item.type === 'Auction';
}

function filterItems(items, selectedGenres, selectedAuthors, selectedPublications, selectedPublishers, showDirectSales, showAuctions, search) {
    const query = search.toLowerCase().trim();

    return items.filter((item) => {
        const matchesFilters =
            matchesGenreFilter(item, selectedGenres) &&
            matchesMultiFilter(item.author, selectedAuthors) &&
            matchesMultiFilter(item.publication, selectedPublications) &&
            matchesMultiFilter(item.publisher, selectedPublishers) &&
            matchesTypeFilter(item, showDirectSales, showAuctions);

        if (!matchesFilters) return false;
        if (!query) return true;

        return ['item', 'genreName', 'type', 'price'].some((key) =>
            String(item[key]).toLowerCase().includes(query)
        );
    });
}

function sortItems(items, { sortBy, reversed, search, selectedGenres, selectedAuthors, selectedPublications, selectedPublishers, showDirectSales, showAuctions }) {
    const filteredItems = filterItems(items, selectedGenres, selectedAuthors, selectedPublications, selectedPublishers, showDirectSales, showAuctions, search);

    if (!sortBy) return filteredItems;

    return [...filteredItems].sort((a, b) => {
        const aValue = String(a[sortBy] ?? '');
        const bValue = String(b[sortBy] ?? '');

        return reversed
            ? bValue.localeCompare(aValue)
            : aValue.localeCompare(bValue);
    });
}

function uniqueOptions(items, field) {
    return [...new Set(items.map((i) => i[field]).filter(Boolean))]
        .sort()
        .map((v) => ({ value: v, label: v }));
}

export function MarketPlaceTable({
                                     items,
                                     genres,
                                     showDirectSales,
                                     showAuctions,
                                     onShowDirectSalesChange,
                                     onShowAuctionsChange,
                                     canSeePrice,
                                     onSaleClick,
                                 }) {
    const [search, setSearch] = useState('');
    const [sortBy, setSortBy] = useState(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);
    const [selectedGenres, setSelectedGenres] = useState([]);
    const [selectedAuthors, setSelectedAuthors] = useState([]);
    const [selectedPublications, setSelectedPublications] = useState([]);
    const [selectedPublishers, setSelectedPublishers] = useState([]);

    const authorOptions = uniqueOptions(items, 'author');
    const publicationOptions = uniqueOptions(items, 'publication');
    const publisherOptions = uniqueOptions(items, 'publisher');

    const genreOptions = genres.filter((g) => g.value !== 'all');

    const sortedItems = sortItems(items, {
        sortBy,
        reversed: reverseSortDirection,
        search,
        selectedGenres,
        selectedAuthors,
        selectedPublications,
        selectedPublishers,
        showDirectSales,
        showAuctions,
    });

    const setSorting = (field) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
    };

    const rows = sortedItems.map((item) => (
        <Table.Tr
            key={item.id}
            onClick={() => onSaleClick && onSaleClick(item)}
            className={classes.row}
            style={{ cursor: 'pointer' }}
        >
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

            <Group grow align="flex-start">
                <MultiSelect
                    label="Genre"
                    placeholder="All genres"
                    data={genreOptions}
                    value={selectedGenres}
                    onChange={setSelectedGenres}
                    searchable
                    clearable
                />
                <MultiSelect
                    label="Author"
                    placeholder="All authors"
                    data={authorOptions}
                    value={selectedAuthors}
                    onChange={setSelectedAuthors}
                    searchable
                    clearable
                />
                <MultiSelect
                    label="Publication"
                    placeholder="All publications"
                    data={publicationOptions}
                    value={selectedPublications}
                    onChange={setSelectedPublications}
                    searchable
                    clearable
                />
                <MultiSelect
                    label="Publisher"
                    placeholder="All publishers"
                    data={publisherOptions}
                    value={selectedPublishers}
                    onChange={setSelectedPublishers}
                    searchable
                    clearable
                />
            </Group>

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
