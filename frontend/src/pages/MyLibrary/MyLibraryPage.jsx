import { useEffect } from "react";
import { useDisclosure } from "@mantine/hooks";
import { Button, Group } from "@mantine/core";
import { IconPlus } from "@tabler/icons-react";

import { DefaultLayout } from "@/components/layout/DefaultLayout";
import { ItemAccordion } from "@/components/accordion/ItemAccordion";
import { AddItemModal } from "@/components/addItemModal/AddItemModal";

import { useLibrary } from "@/context/AppContext";
import { getLibrary } from "@/context/library/LibraryActions";

export default function MyLibraryPage() {

    const { state, dispatch } = useLibrary();

    const [opened, { open, close }] = useDisclosure(false);

    useEffect(() => {
        getLibrary(dispatch);
        }, [dispatch]);

    if (state.loading) return <p>Loading...</p>;
    if (state.error) return <p>Error: {state.error}
    </p>;

return (
    <DefaultLayout title="My Library" subtitle="CHECK OUT YOUR ITEMS:">
        <ItemAccordion
            items={state.items}
            details={state.details}
            dispatch={dispatch}
        />

            <Group justify="center" mt="xl">
                <Button
                    color="indigo"
                    radius="xl"
                    leftSection={<IconPlus size={16} />}
                    onClick={open}
                >
                    ADD ITEM
                </Button>
            </Group>

        <AddItemModal
            opened={opened}
            onClose={close}
            onItemAdded={() => {
                close();
                getLibrary(dispatch);
            }}
            />
        </DefaultLayout>
    );
}