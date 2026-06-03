import { useEffect } from "react";
import { useDisclosure } from "@mantine/hooks";
import { Button, Group } from "@mantine/core";
import {IconPlus, IconTag} from "@tabler/icons-react";

import {DefaultLayout} from "../../components/layout/DefaultLayout.tsx";
import {ItemAccordion} from "@/components/accordion/ItemAccordion.js";
import {AddItemModal} from "@/components/addItemModal/AddItemModal.tsx";
import { CreateSaleModal } from "@/components/createSaleModal/CreateSaleModal.tsx";

import { useLibrary } from '../../context/AppContext';
import { getLibrary } from '../../context/library/LibraryActions';

export default function MyLibraryPage() {

    const { state, dispatch } = useLibrary();

    const [addItemOpened, { open: openAddItem, close: closeAddItem }] = useDisclosure(false);
    const [createSaleOpened, { open: openCreateSale, close: closeCreateSale }] = useDisclosure(false);

    useEffect(() => {
        getLibrary(dispatch);
    }, [dispatch]);

    return (
        <DefaultLayout title="My Library" subtitle="CHECK OUT YOUR ITEMS:">
            <Group justify="center" mt="xl">
                <Button
                    color="indigo"
                    radius="xl"
                    leftSection={<IconPlus size={16} />}
                    onClick={openAddItem}
                >
                    ADD ITEM
                </Button>

                <Button
                    color="grape"
                    radius="xl"
                    leftSection={<IconTag size={16} />}
                    onClick={openCreateSale}
                >
                    CREATE A SALE
                </Button>

            </Group>

            <ItemAccordion
                items={state.items}
                details={state.details}
                dispatch={dispatch}
            />

            <AddItemModal
                opened={addItemOpened}
                onClose={closeAddItem}
                onItemAdded={() => {
                    closeAddItem();
                    getLibrary(dispatch);
                }}
            />

            <CreateSaleModal
                opened={createSaleOpened}
                onClose={closeCreateSale}
            />

        </DefaultLayout>
    );
}