import {useContext, useEffect, useState} from 'react';
import {IconChevronDown, IconChevronUp, IconSelector} from '@tabler/icons-react';
import {Center, Group, ScrollArea, Table, Text, UnstyledButton} from '@mantine/core';
import classes from './TableList.module.css';
import {ShareListModal} from "../sharelistmodal/ShareListModal.tsx";
import AppContext from '../../../context/AppContext.tsx';
import {addItemToList, getListsOptions, getMyLists} from '../../../context/lists/ListsActions.jsx';
import {DeleteListModal} from '../../deletelistmodal/DeleteListModal.tsx';
import {AddItemToListDropDown} from '../../addItemToListModal/AddItemToListDropDown.tsx';
import {useNavigate} from "react-router-dom";

export interface RowData {
    listId: string;
    name: string;
    genre: string;
    isPrivate: boolean;
    sharedUntil: number | null;
    links: { rel: string; href: string }[];
    itemsId: string[];
    itemsHref: string | null;
}

interface TableListProps {
    search: string;
    genre: string | null;
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

function sortData(
    data: RowData[],
    payload: { sortBy: keyof RowData | null; reversed: boolean }
) {
    const { sortBy } = payload;
    if (!sortBy) return data;
    return [...data].sort((a, b) => {
        const aVal = String(a[sortBy] ?? '');
        const bVal = String(b[sortBy] ?? '');
        return payload.reversed ? bVal.localeCompare(aVal) : aVal.localeCompare(bVal);
    });
}

export function TableList({ search, genre }: TableListProps) {
    const navigate = useNavigate();
    const { state, dispatch } = useContext(AppContext);
    const { lists } = state.lists;
    const { myListsHref, libraryHref } = state.app;

    const [sortBy, setSortBy] = useState<keyof RowData | null>(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);

    useEffect(() => {
        getListsOptions(dispatch);
    }, [dispatch]);

    useEffect(() => {
        if (myListsHref) getMyLists(dispatch, myListsHref);
    }, [myListsHref]);

    const setSorting = (field: keyof RowData) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
    };

    // Filter by search and genre, then sort
    const processedData = sortData(
        lists.filter(row => {
            const q = search.toLowerCase().trim();
            const matchesSearch = !q ||
                row.name.toLowerCase().includes(q) ||
                row.genre.toLowerCase().includes(q);
            const matchesGenre = !genre || row.genre === genre;
            return matchesSearch && matchesGenre;
        }),
        { sortBy, reversed: reverseSortDirection }
    );

    const rows = processedData.map((row) => {
        const canDelete = row.links?.some(l => l.rel === 'delete');
        const canShare  = row.links?.some(l => l.rel === 'make-public' || l.rel === 'make-private');

        return (
            <Table.Tr
                key={row.listId}
                onClick={() => navigate(`/lists/${row.listId}/items`)}
                style={{
                        cursor: "pointer",
                        whiteSpace: "nowrap"
                }}
            >
                <Table.Td>{row.name}</Table.Td>
                <Table.Td>{row.genre}</Table.Td>
                <Table.Td>
                    {row.sharedUntil !== null
                        ? <Text fz="sm">{row.sharedUntil} day{row.sharedUntil !== 1 ? 's' : ''} left</Text>
                        : <Text fz="sm" c="dimmed">—</Text>
                    }
                </Table.Td>
                <Table.Td w={78} onClick={(e) => e.stopPropagation()}>
                    <Center>
                        {canShare && (
                            <ShareListModal
                                listName={row.name}
                                visibility={row.isPrivate ? 'private' : 'public'}
                                links={row.links}
                            />
                        )}
                    </Center>
                </Table.Td>
                <Table.Td w={50} onClick={(e) => e.stopPropagation()}>
                    <Center>
                        <AddItemToListDropDown
                            listName={row.name}
                            libraryHref={libraryHref}
                            existingItemIds={row.itemsId}
                            onConfirm={(ids) => ids.forEach(id => addItemToList(dispatch, row.links, id))}
                        />
                    </Center>
                </Table.Td>
                <Table.Td w={50} onClick={(e) => e.stopPropagation()}>
                    <Center>
                        {canDelete && (
                            <DeleteListModal
                                listName={row.name}
                                links={row.links}
                                myListsHref={myListsHref}
                            />
                        )}
                    </Center>
                </Table.Td>
            </Table.Tr>
        );
    });

    return (
        <ScrollArea>
            <Table horizontalSpacing="md"
                   verticalSpacing="xs"
                   miw={900}
                   highlightOnHover
                   highlightOnHoverColor="var(--mantine-color-white)"
                   tableLayout="fixed"
            >
                <Table.Thead>
                    <Table.Tr>
                        <Th width="200" sorted={sortBy === 'name'} reversed={reverseSortDirection} onSort={() => setSorting('name')}>List Name</Th>
                        <Th width="80" sorted={sortBy === 'genre'} reversed={reverseSortDirection} onSort={() => setSorting('genre')}>Genre</Th>
                        <Th width="30" sorted={sortBy === 'sharedUntil'} reversed={reverseSortDirection} onSort={() => setSorting('sharedUntil')}>Shared Until</Th>
                        <Table.Th w={100}><Center><Text fw={500} fz="sm">Visibility</Text></Center></Table.Th>
                        <Table.Th w={100}><Center><Text fw={500} fz="sm">Add Items</Text></Center></Table.Th>
                        <Table.Th w={100}><Center><Text fw={500} fz="sm">Delete</Text></Center></Table.Th>
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