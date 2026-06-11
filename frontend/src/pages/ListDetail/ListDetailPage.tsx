import {useContext, useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {ActionIcon, Center, ScrollArea, Table, Text, Tooltip} from '@mantine/core';
import {IconArrowLeft, IconTrash} from '@tabler/icons-react';
import AppContext from '../../context/AppContext';
import {removeItemFromList} from '../../context/lists/ListsActions';
import {apiClient} from '../../services/apiClient';
import {DefaultLayout} from '../../components/layout/DefaultLayout.tsx';

interface ItemDetail {
    itemId: string;
    title: string;
    authorName: string;
    identifier: string;
    publicationTypeName: string;
}

export default function ListDetailPage() {
    const {listId} = useParams<{ listId: string }>();
    const {state, dispatch} = useContext(AppContext);
    const navigate = useNavigate();

    const list = state.lists.lists.find(l => l.listId === listId);

    const [items, setItems] = useState<ItemDetail[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!list) return;

        setLoading(true);
        Promise.all(
            list.itemIds.map(id =>
                apiClient.getItemById(id)
                    .then(data => ({
                        itemId: id,
                        title: data?.title ?? '—',
                        authorName: data?.authorName ?? '—',
                        identifier: data?.identifier ?? '—',
                        publicationTypeName: data?.publicationTypeName ?? '—',
                    }))
                    .catch(() => ({itemId: id, title: '—', authorName: '—', identifier: '—', publicationTypeName: '—'}))
            )
        ).then(results => {
            setItems(results);
            setLoading(false);
        });
    }, [list?.itemIds.join(',')]);

    const canRemove = list?.links?.some(l => l.rel === 'remove-item') ?? false;

    const handleRemove = (itemId: string) => {
        if (!list) return;
        removeItemFromList(dispatch, list.links, itemId, list.listId).then(() => {
            setItems(prev => prev.filter(i => i.itemId !== itemId));
        });
    };

    if (!list) {
        return (
            <DefaultLayout title="List not found" subtitle="">
                <Text>List not found.</Text>
            </DefaultLayout>
        );
    }

    const rows = items.map(item => (
        <Table.Tr key={item.itemId}>
            <Table.Td>{item.title}</Table.Td>
            <Table.Td>{item.authorName}</Table.Td>
            <Table.Td>{item.identifier}</Table.Td>
            <Table.Td>{item.publicationTypeName}</Table.Td>
            <Table.Td w={60}>
                <Center>
                    {canRemove ? (
                        <ActionIcon
                            color="red"
                            variant="subtle"
                            onClick={() => handleRemove(item.itemId)}
                            aria-label="Remove item"
                        >
                            <IconTrash size={16}/>
                        </ActionIcon>
                    ) : (
                        <Tooltip label="Remove not available yet">
                            <ActionIcon color="red" variant="subtle" disabled>
                                <IconTrash size={16}/>
                            </ActionIcon>
                        </Tooltip>
                    )}
                </Center>
            </Table.Td>
        </Table.Tr>
    ));

    return (
        <DefaultLayout title={list.name} subtitle={`${list.genre} · ${list.visibility}`}>
            <ActionIcon variant="subtle" mb="md" onClick={() => navigate('/my-lists')} aria-label="Back">
                <IconArrowLeft size={18}/>
            </ActionIcon>
            <ScrollArea>
                <Table horizontalSpacing="md" verticalSpacing="xs" miw={700} layout="fixed">
                    <Table.Thead>
                        <Table.Tr>
                            <Table.Th>Title</Table.Th>
                            <Table.Th>Author</Table.Th>
                            <Table.Th>ISBN</Table.Th>
                            <Table.Th>Type</Table.Th>
                            <Table.Th w={60}><Center><Text fw={500} fz="sm">Remove</Text></Center></Table.Th>
                        </Table.Tr>
                    </Table.Thead>
                    <Table.Tbody>
                        {loading ? (
                            <Table.Tr>
                                <Table.Td colSpan={5}>
                                    <Text ta="center">Loading...</Text>
                                </Table.Td>
                            </Table.Tr>
                        ) : rows.length > 0 ? rows : (
                            <Table.Tr>
                                <Table.Td colSpan={5}>
                                    <Text fw={500} ta="center">No items in this list</Text>
                                </Table.Td>
                            </Table.Tr>
                        )}
                    </Table.Tbody>
                </Table>
            </ScrollArea>
        </DefaultLayout>
    );
}
