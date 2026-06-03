import { useState, useContext, useEffect } from 'react';
import { Modal, Button, TextInput, Select, Stack, Text } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconPlus } from '@tabler/icons-react';
import AppContext from '../../context/AppContext';
import { createList, getGenres } from '../../context/lists/ListsActions';

export function NewListModal() {
    const [opened, { open, close }] = useDisclosure(false);
    const [name, setName] = useState('');
    const [genreId, setGenreId] = useState(null);
    const { state, dispatch } = useContext(AppContext);
    const { genres, error } = state.lists;
    const { createListHref, genresHref, myListsHref } = state.app;


    useEffect(() => {
        if (opened && genresHref) getGenres(dispatch, genresHref);
    }, [opened]);

    const handleCreate = async () => {
        const success = await createList(dispatch, createListHref, { name, genreId }, myListsHref);
        if (success) close();
    };

    return (
        <>
            <Modal
                opened={opened}
                onClose={close}
                title="Create New List"
                overlayProps={{ backgroundOpacity: 0.55, blur: 3 }}
                centered
            >
                <Stack>
                    {error && <Text fz="sm" c="red">{error}</Text>}
                    <TextInput
                        label="List Name"
                        placeholder="Enter a name"
                        value={name}
                        onChange={(e) => setName(e.currentTarget.value)}
                    />
                    <Select
                        label="Genre"
                        placeholder="Select a genre"
                        data={genres}
                        value={genreId}
                        onChange={setGenreId}
                    />
                    <Button fullWidth mt="sm" color="indigo" radius="xl" onClick={handleCreate}>
                        Create List
                    </Button>
                </Stack>
            </Modal>

            <Button
                color="indigo"
                leftSection={<IconPlus size={16} />}
                radius="xl"
                onClick={open}
                disabled={!createListHref}
            >
                NEW LIST
            </Button>
        </>
    );
}