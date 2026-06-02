import { useDisclosure } from '@mantine/hooks';
import { Button, Group } from "@mantine/core";


import {DefaultLayout} from "../../components/layout/DefaultLayout.tsx";
import {ItemAccordion} from "@/components/accordion/ItemAccordion.js";
import {AddItemModal} from "@/components/addItemModal/AddItemModal.tsx";
import {IconPlus} from "@tabler/icons-react";
import {useState} from "react";



export default function MyLibraryPage() {

    const [opened, { open, close }] = useDisclosure(false);

    const [items, setItems] = useState([

                    {
                    itemId: 'ITEM-001',
                    title: 'The War of the Worlds',
                    imageUrl: null,
                    publicationType: 'Book',
                    authorName: 'H.G. Wells',
                    identifier: '9781784872113'
                },
                {
                    itemId: 'ITM-002',
                    title: 'Duna',
                    imageUrl: null,
                    publicationType: 'Book',
                    authorName: 'Frank Herbert',
                    identifier: '9780593099322'
                }
    ])

return (
    <DefaultLayout title="My Library" subtitle="CHECK OUT YOUR ITEMS:">
        <ItemAccordion items={items} />


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
            onItemAdded={(newItem) => {
                setItems((currentItems) => [...currentItems, newItem]);
                close();
            }}
            />
        </DefaultLayout>
    );
}