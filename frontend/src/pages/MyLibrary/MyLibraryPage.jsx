import {DefaultLayout} from "../../components/layout/DefaultLayout.tsx";
import {ItemDetailTable} from "../../components/itemDetailsTable/ItemDetailsTable.js";

export default function MyLibraryPage() {


    return (
        <DefaultLayout title="My Library" subtitle="CHECK OUT YOUR ITEMS:">
            <p>Page content goes here.</p>
            <ItemDetailTable
                item={{
                    publicationType: 'Book',
                    authorName: 'H.G. Wells',
                    identifier: '9781784872113'
                }}
            />
        </DefaultLayout>
    );
}