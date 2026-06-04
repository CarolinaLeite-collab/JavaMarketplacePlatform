import { useState, useContext } from 'react';
import { Modal, NumberInput, Button, Stack, Text, Group } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconEye, IconEyeOff } from '@tabler/icons-react';
import { ActionIcon, Tooltip } from '@mantine/core';
import AppContext from '../../context/AppContext';
import { makeListPublic, makeListPrivate } from '../../context/lists/ListsActions';

interface ShareListModalProps {
    listName: string;
    visibility: 'public' | 'private';
    links: { rel: string; href: string }[];
}

export function ShareListModal({ listName, visibility, links }: ShareListModalProps) {
    const [shareOpened, { open: openShare, close: closeShare }] = useDisclosure(false);
    const [confirmOpened, { open: openConfirm, close: closeConfirm }] = useDisclosure(false);
    const [days, setDays] = useState<number | string>(7);
    const { dispatch } = useContext(AppContext);

    const handleMakePrivate = async () => {
        await makeListPrivate(dispatch, links);
        closeConfirm();
    };

    const handleMakePublic = async () => {
        await makeListPublic(dispatch, links, days);
        closeShare();
    };

    return (
        <>
            {/* Confirm make private modal */}
            <Modal
                opened={confirmOpened}
                onClose={closeConfirm}
                title={`Unshare "${listName}"`}
                overlayProps={{ backgroundOpacity: 0.55, blur: 3 }}
                centered
                size="sm"
            >
                <Stack>
                    <Text fz="sm" c="dimmed">Are you sure you want to make this list private?</Text>
                    <Group justify="flex-end" grow>
                        <Button variant="default" radius="xl" onClick={closeConfirm}>No</Button>
                        <Button color="teal" radius="xl" onClick={handleMakePrivate}>Yes</Button>
                    </Group>
                </Stack>
            </Modal>

            {/* Make public modal */}
            <Modal
                opened={shareOpened}
                onClose={closeShare}
                title={`Share "${listName}"`}
                overlayProps={{ backgroundOpacity: 0.55, blur: 3 }}
                centered
            >
                <Stack>
                    <Text fz="sm" c="dimmed">How many days do you want this list to be public?</Text>
                    <NumberInput
                        label="Days"
                        placeholder="Enter number of days"
                        min={1}
                        value={days}
                        onChange={setDays}
                    />
                    <Button fullWidth mt="sm" color="teal" radius="xl" onClick={handleMakePublic}>
                        Make Public
                    </Button>
                </Stack>
            </Modal>

            <Tooltip label={visibility === 'public' ? 'Make private' : 'Make public'} withArrow>
                <ActionIcon
                    variant="filled"
                    color={visibility === 'public' ? 'teal' : 'gray'}
                    size="md"
                    radius="sm"
                    onClick={visibility === 'private' ? openShare : openConfirm}
                >
                    {visibility === 'public'
                        ? <IconEye size={16} stroke={1.5} />
                        : <IconEyeOff size={16} stroke={1.5} />
                    }
                </ActionIcon>
            </Tooltip>
        </>
    );
}