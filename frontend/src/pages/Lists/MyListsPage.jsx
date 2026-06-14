import {DefaultLayout} from "../../components/layout/DefaultLayout.tsx";
import {TableList} from "../../components/tablelist/TableList.tsx";
import {Affix} from '@mantine/core';
import {NewListModal} from "../../components/newlistmodal/NewListModal.tsx";
import {useEffect, useState} from "react";

export default function MyListsPage() {
    const [footerHeight, setFooterHeight] = useState(0);

    useEffect(() => {
        const footer = document.querySelector('footer');
        if (footer) setFooterHeight(footer.offsetHeight);
    }, [])

    return (
        <DefaultLayout title="My Lists" subtitle="CHECK OUT YOUR LISTS:">
            <TableList></TableList>
            <Affix position={{ bottom: footerHeight + 76, right: 24 }} zIndex={90}>
                <NewListModal />
            </Affix>
        </DefaultLayout>
    );
}