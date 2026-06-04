import { useEffect, useContext, useState } from 'react';
import { IconChevronDown, IconChevronUp, IconSelector, IconSearch, IconTrash } from '@tabler/icons-react';
import { Center, Group, ActionIcon, ScrollArea, Table, Text, TextInput, UnstyledButton } from '@mantine/core';
import classes from './TableList.module.css';
import { ShareListModal } from "../sharelistmodal/ShareListModal.tsx";
import AppContext from '../../context/AppContext';
import { getMyLists, getListsOptions, addItemToList } from '../../context/lists/ListsActions';
import { DeleteListModal } from '../deletelistmodal/DeleteListModal.tsx';
import { AddItemToListDropDown } from '../addItemToListModal/AddItemToListDropDown.tsx';
import { useUser } from '../../context/UserContext';


interface RowData {
    listId: string;
    name: string;
    genre: string;
    visibility: 'public' | 'private';
    sharedUntil: number | null;
    links: { rel: string; href: string }[];
    itemIds: string[];
}

interface ThProps {
    children: React.ReactNode;
    reversed: boolean;
    sorted: boolean;
    onSort: () => void;
    width?: string;
}

function Th({ children, reversed, sorted, onSort, width }: ThProps) {
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

function filterData(data: RowData[], search: string) {
    const query = search.toLowerCase().trim();
    return data.filter((item) =>
        (['name', 'genre', 'visibility'] as (keyof RowData)[]).some((key) =>
            String(item[key]).toLowerCase().includes(query)
        ) ||
        (item.sharedUntil !== null && String(item.sharedUntil).includes(query))
    );
}

function sortData(data: RowData[], payload: { sortBy: keyof RowData | null; reversed: boolean; search: string }) {
    const { sortBy } = payload;
    if (!sortBy) return filterData(data, payload.search);
    return filterData(
        [...data].sort((a, b) => {
            const aVal = String(a[sortBy] ?? '');
            const bVal = String(b[sortBy] ?? '');
            return payload.reversed ? bVal.localeCompare(aVal) : aVal.localeCompare(bVal);
        }),
        payload.search
    );
}

export function TableList() {
    const { state, dispatch } = useContext(AppContext);
    const { lists } = state.lists;
    const { myListsHref, libraryHref } = state.app;
    console.log('TableList render - myListsHref:', myListsHref, 'lists:', lists);
    const [search, setSearch] = useState('');
    const [sortBy, setSortBy] = useState<keyof RowData | null>(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);

    useEffect(() => {
        getListsOptions(dispatch);
    }, [dispatch]);

    useEffect(() => {
        if (myListsHref) getMyLists(dispatch, myListsHref);
    }, [myListsHref]);

    const sortedData = sortData(lists, { sortBy, reversed: reverseSortDirection, search });

    const setSorting = (field: keyof RowData) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
    };

    const rows = sortedData.map((row) => {
        const canDelete = row.links?.some(l => l.rel === 'delete');
        const canShare = row.links?.some(l => l.rel === 'make-public' || l.rel === 'make-private');

        return (
            <Table.Tr key={row.listId}>
                <Table.Td>{row.name}</Table.Td>
                <Table.Td>{row.genre}</Table.Td>
                <Table.Td>
                    {row.sharedUntil !== null
                        ? <Text fz="sm">{row.sharedUntil} day{row.sharedUntil !== 1 ? 's' : ''} left</Text>
                        : <Text fz="sm" c="dimmed">—</Text>
                    }
                </Table.Td>
                <Table.Td w={120}>
                    <Center>
                        {canShare && <ShareListModal listName={row.name} visibility={row.visibility} links={row.links} />}
                    </Center>
                </Table.Td>
                <Table.Td w={50}>
                    <Center>
                        <AddItemToListDropDown
                            listName={row.name}
                            libraryHref={libraryHref}
                            existingItemIds={row.itemIds}
                            onConfirm={(ids) => ids.forEach(id => addItemToList(dispatch, row.links, id))}
                        />
                    </Center>
                </Table.Td>
                <Table.Td w={50}>
                    <Center>
                        {canDelete && (<DeleteListModal listName={row.name} links={row.links} myListsHref={myListsHref} />)}
                    </Center>
                </Table.Td>
            </Table.Tr>
        );
    });

    return (
        <ScrollArea>
            <TextInput
                placeholder="Search by name, genre or visibility"
                mb="md"
                leftSection={<IconSearch size={16} stroke={1.5} />}
                value={search}
                onChange={(e) => setSearch(e.currentTarget.value)}
            />
            <Table horizontalSpacing="md" verticalSpacing="xs" miw={900} layout="fixed">
                <Table.Thead>
                    <Table.Tr>
                        <Th width="25%" sorted={sortBy === 'name'} reversed={reverseSortDirection} onSort={() => setSorting('name')}>List Name</Th>
                        <Th width="15%" sorted={sortBy === 'genre'} reversed={reverseSortDirection} onSort={() => setSorting('genre')}>Genre</Th>
                        <Th width="15%" sorted={sortBy === 'sharedUntil'} reversed={reverseSortDirection} onSort={() => setSorting('sharedUntil')}>Shared Until</Th>
                        <Table.Th w={78}><Center><Text fw={500} fz="sm">Visibility</Text></Center></Table.Th>
                        <Table.Th w={78}><Center><Text fw={500} fz="sm">Add Items</Text></Center></Table.Th>
                        <Table.Th w={78}><Center><Text fw={500} fz="sm">Delete</Text></Center></Table.Th>
                    </Table.Tr>
                </Table.Thead>
                <Table.Tbody>
                    {rows.length > 0 ? rows : (
                        <Table.Tr>
                            <Table.Td colSpan={6}>
                                <Text fw={500} ta="center">Nothing found</Text>
                            </Table.Td>
                        </Table.Tr>
                    )}
                </Table.Tbody>
            </Table>
        </ScrollArea>
    );
}