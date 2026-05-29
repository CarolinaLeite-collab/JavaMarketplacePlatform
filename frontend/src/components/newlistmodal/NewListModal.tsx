import { useDisclosure } from '@mantine/hooks';
import { Modal, Button, TextInput, Select, Stack } from '@mantine/core';
import { IconPlus } from '@tabler/icons-react';

export function NewListModal() {
    const [opened, { open, close }] = useDisclosure(false);

    return (
        <>
            <Modal
                opened={opened}
                onClose={close}
                title="Create New List"
                overlayProps={{
                    backgroundOpacity: 0.55,
                    blur: 3,
                }}
                centered
            >
                <Stack>
                    <TextInput label="List Name" placeholder="Enter a name" />
                    <Select
                        label="Genre"
                        placeholder="Select a genre"
                        data={['Fantasy', 'Mystery', 'Science Fiction', 'Classic', 'Romance', 'Thriller']}
                    />
                    <Button fullWidth mt="sm" color="indigo" radius="xl" onClick={close}>
                        Create List
                    </Button>
                </Stack>
            </Modal>

            <Button
                color="indigo"
                leftSection={<IconPlus size={16} />}
                radius="xl"
                onClick={open}
            >
                NEW LIST
            </Button>
        </>
    );
}