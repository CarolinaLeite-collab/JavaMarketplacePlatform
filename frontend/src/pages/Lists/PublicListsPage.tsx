import { DefaultLayout } from "../../components/layout/DefaultLayout.tsx";
import { Container, Table, ActionIcon, Text, Loader, Center } from "@mantine/core";
import { IconHeart, IconHeartFilled } from "@tabler/icons-react";
import { useNavigate } from "react-router-dom";
import { useState, useEffect, useContext, useCallback } from "react";
import { FiltersBar } from "../../components/lists/FiltersBar.tsx";
import AppContext from "../../context/AppContext.tsx";
import { getPublicLists, getListsOptions } from "../../context/lists/ListsActions.jsx";
import { getGenres } from "../../context/lists/ListsActions.jsx";
import { RowData } from "../../components/lists/tablelist/TableList.tsx";

export default function PublicListsPage() {
    const navigate = useNavigate();
    const { state, dispatch } = useContext(AppContext);
    const { publicListsHref } = state.app;
    const publicLists = state.lists.publicLists ?? [];
    const genres = state.lists.genres;

    const [search, setSearch]   = useState("");
    const [genre, setGenre]     = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    // Favourites are local UI state only — no backend logic
    const [favourites, setFavourites] = useState<Set<string>>(new Set());

    useEffect(() => { getListsOptions(dispatch); }, [dispatch]);

    useEffect(() => {
        if (state.app.genresHref) {
            getGenres(dispatch, state.app.genresHref);
        }
    }, [state.app.genresHref]);

    useEffect(() => {
        if (!publicListsHref) return;
        setLoading(true);
        getPublicLists(dispatch, publicListsHref).finally(() => setLoading(false));
    }, [publicListsHref]);

    const toggleFavourite = useCallback((id: string) => {
        setFavourites(prev => {
            const next = new Set(prev);
            next.has(id) ? next.delete(id) : next.add(id);
            return next;
        });
    }, []);

    const handleListClick = (list: RowData) => {
        if (list.itemsHref) {
            // Extract listId from the HAL href: /my-lists/{listId}/items
            const parts = list.itemsHref.split('/');
            const idx = parts.indexOf('my-lists');
            const id = idx !== -1 ? parts[idx + 1] : list.listId;
            navigate(`/lists/${id}/items`);
        } else {
            navigate(`/lists/${list.listId}/items`);
        }
    };

    const filtered = publicLists.filter(list => {
        const matchesSearch = !search.trim() || list.name.toLowerCase().includes(search.toLowerCase());
        const matchesGenre  = !genre || list.genre === genre;
        return matchesSearch && matchesGenre;
    });

    return (
        <DefaultLayout title="Public Lists" subtitle="CHECK OUT OTHER'S COLLECTIONS:">
            <Container size="lg">
                <FiltersBar
                    search={search}
                    onSearchChange={setSearch}
                    genre={genre}
                    onGenreChange={setGenre}
                    genres={genres}
                />

                {loading ? (
                    <Center mt="xl"><Loader data-testid="loader" /></Center>
                ) : (
                    <Table highlightOnHover mt="md" highlightOnHoverColor="var(--mantine-color-white)"
                           tableLayout="fixed"
                    >
                        <Table.Thead>
                            <Table.Tr>
                                <Table.Th style={{ width: 200, fontWeight: 500 }}>List name</Table.Th>
                                <Table.Th style={{ width: 100, fontWeight: 500 }}>Genre</Table.Th>
                                <Table.Th style={{ width: 80, textAlign: "center", fontWeight: 500 }}>Add to Favourites</Table.Th>
                            </Table.Tr>
                        </Table.Thead>
                        <Table.Tbody>
                            {filtered.length > 0 ? filtered.map((list) => (
                                <Table.Tr key={list.listId} style={{ cursor: "pointer" }}>
                                    <Table.Td onClick={() => handleListClick(list)}>
                                        {list.name}
                                    </Table.Td>
                                    <Table.Td>{list.genre}</Table.Td>
                                    <Table.Td>
                                        <Center>
                                            <ActionIcon
                                                color="red"
                                                variant="subtle"
                                                onClick={() => toggleFavourite(list.listId)}
                                            >
                                                {favourites.has(list.listId)
                                                    ? <IconHeartFilled />
                                                    : <IconHeart />}
                                            </ActionIcon>
                                        </Center>
                                    </Table.Td>
                                </Table.Tr>
                            )) : (
                                <Table.Tr>
                                    <Table.Td colSpan={3}>
                                        <Text ta="center" fw={500}>No public lists found</Text>
                                    </Table.Td>
                                </Table.Tr>
                            )}
                        </Table.Tbody>
                    </Table>
                )}
            </Container>
        </DefaultLayout>
    );
}


