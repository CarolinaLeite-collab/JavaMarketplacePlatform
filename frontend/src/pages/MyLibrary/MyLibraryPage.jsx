import {DefaultLayout} from "../../components/layout/DefaultLayout.tsx";
import {ItemAccordion} from "@/components/accordion/ItemAccordion.js";

export default function MyLibraryPage() {
    return (
        <DefaultLayout title="My Library" subtitle="CHECK OUT YOUR ITEMS:">
            <ItemAccordion
                items={[
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
                }]
                }/>
        </DefaultLayout>
    );
}