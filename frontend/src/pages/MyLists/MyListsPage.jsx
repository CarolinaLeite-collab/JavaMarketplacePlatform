import {DefaultLayout} from "../../components/layout/DefaultLayout.tsx";
import {TableList} from "@/components/tablelist/TableList.tsx";

export default function MyListsPage() {
    return (
        <DefaultLayout title="My Lists" subtitle="CHECK OUT YOUR LISTS:">
            <TableList></TableList>
        </DefaultLayout>
    );
}