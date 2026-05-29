import { useState } from 'react';
import { Modal, NumberInput, Button, Stack, Text } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconEye, IconEyeOff } from '@tabler/icons-react';
import { ActionIcon, Tooltip } from '@mantine/core';

interface ShareListModalProps {
    listName: string;
    visibility: 'public' | 'private';
}

export function ShareListModal({ listName, visibility }: ShareListModalProps) {
    const [opened, { open, close }] = useDisclosure(false);
    const [days, setDays] = useState<number | string>(7);

    return (
        <>
            <Tooltip label={visibility === 'public' ? 'Make private' : 'Make public'} withArrow>
                <ActionIcon
                    variant="filled"
                    color={visibility === 'public' ? 'teal' : 'gray'}
                    size="md"
                    radius="sm"
                    onClick={visibility === 'private' ? open : () => {/* TODO: request to make private */}}
                >
                    {visibility === 'public'
                        ? <IconEye size={16} stroke={1.5} />
                        : <IconEyeOff size={16} stroke={1.5} />
                    }
                </ActionIcon>
            </Tooltip>

            <Modal
                opened={opened}
                onClose={close}
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
                    <Button fullWidth mt="sm" color="teal" radius="xl" onClick={close}>
                        Make Public
                    </Button>
                </Stack>
            </Modal>
        </>
    );
}