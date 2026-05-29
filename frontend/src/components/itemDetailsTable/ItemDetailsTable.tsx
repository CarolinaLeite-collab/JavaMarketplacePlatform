import { Table } from '@mantine/core';

interface ItemDetail {
    publicationType: string;
    authorName: string;
    identifier: string | null;
}

interface Props {
    item: ItemDetail;
}

export function ItemDetailTable({ item }: Props) {

    const identifierLabel =
        item.publicationType === 'Book'     ? 'ISBN' :
        item.publicationType === 'Magazine' ? 'ISSN' :
        'Identifier';

    return (
        <Table withTableBorder withColumnBorders>
            <Table.Tbody>

                <Table.Tr>
                    <Table.Td fw={700} w={120}>Type</Table.Td>
                    <Table.Td>{item.publicationType}</Table.Td>
                </Table.Tr>

                <Table.Tr>
                    <Table.Td fw={700}>Author</Table.Td>
                    <Table.Td>{item.authorName}</Table.Td>
                </Table.Tr>

                {item.identifier && (
                    <Table.Tr>
                        <Table.Td fw={700}>
                            {identifierLabel}
                        </Table.Td>
                        <Table.Td>{item.identifier}</Table.Td>
                    </Table.Tr>
                )}

            </Table.Tbody>
        </Table>
    );
}