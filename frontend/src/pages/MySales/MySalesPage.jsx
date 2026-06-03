import { DefaultLayout } from '../../components/layout/DefaultLayout.tsx';
import { Affix } from "@mantine/core";
import { useEffect, useState } from "react";
import { TableSales } from "@/components/tablesales/TableSales.tsx";
import { CreateSaleModal } from "@/components/createSaleModal/CreateSaleModal.tsx";

export default function MySalesPage() {
    const [footerHeight, setFooterHeight] = useState(0);

    useEffect(() => {
        const footer = document.querySelector("footer");
        if (footer) setFooterHeight(footer.offsetHeight);
    }, []);

    return (
        <DefaultLayout title="My Sales" subtitle="CHECK OUT YOUR SALES:">
            <TableSales />
            <Affix position={{ bottom: footerHeight + 76, right: 24 }} zIndex={90}>
                <CreateSaleModal />
            </Affix>
        </DefaultLayout>
    );
}