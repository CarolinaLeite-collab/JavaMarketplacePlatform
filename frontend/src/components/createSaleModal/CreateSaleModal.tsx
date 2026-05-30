import { useDisclosure } from "@mantine/hooks";
import {
    Button,
    Group,
    Modal,
    NumberInput,
    Select,
    Stack,
} from "@mantine/core";
import { IconPlus } from "@tabler/icons-react";

export function CreateSaleModal() {
    const [opened, { open, close }] = useDisclosure(false);

    return (
        <>
            <Modal
                opened={opened}
                onClose={close}
                title="Create New Sale"
                overlayProps={{
                    backgroundOpacity: 0.55,
                    blur: 3,
                }}
                centered
            >
                <Stack>
                    <Select
                        label="Sale type"
                        placeholder="Select the type of sale"
                        data={[
                            "Direct sale"
                        ]}
                        searchable
                    />
                    <Select
                        label="Item"
                        placeholder="Select an item from your library"
                        data={[
                            "The War of the Worlds",
                            "Dune"
                        ]}
                        searchable
                    />

                    <Group grow align="flex-end">
                        <NumberInput
                            label="Price value"
                            placeholder="Enter price"
                            min={0}
                            decimalScale={2}
                            fixedDecimalScale
                        />

                        <Select
                            label="Currency"
                            placeholder="Currency"
                            data={["EUR", "USD", "GBP"]}
                        />
                    </Group>

                    <NumberInput
                        label="Duration (days)"
                        placeholder="Enter duration"
                        min={1}
                    />

                    <Button fullWidth mt="sm" color="indigo" radius="xl" onClick={close}>
                        Create Sale
                    </Button>
                </Stack>
            </Modal>

            <Button
                color="indigo"
                leftSection={<IconPlus size={16} />}
                radius="xl"
                onClick={open}
            >
                CREATE A SALE
            </Button>
        </>
    );
}