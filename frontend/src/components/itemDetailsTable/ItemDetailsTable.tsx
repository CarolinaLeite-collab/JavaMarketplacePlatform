import {Table, Paper, useComputedColorScheme} from '@mantine/core';

interface ItemDetail {
    authorName: string;
    identifier: string | null;
    publicationType: string;
}

interface Props {
    item: ItemDetail;
}

export function ItemDetailTable({ item }: Props) {
    const colorScheme = useComputedColorScheme('light');

    const labelStyle = {
        backgroundColor:
            colorScheme === 'light'
                ? 'var(--mantine-color-gray-3)'
                : 'var(--mantine-color-dark-4)',
    };

    const identifierLabel =
        item.publicationType === 'Book'     ? 'ISBN' :
        item.publicationType === 'Magazine' ? 'ISSN' :
        'Identifier';

    return (
        <Paper
            radius="md"
            withBorder
            style={{
                overflow: 'hidden'
            }}
        >
            <Table withTableBorder withColumnBorders
                   styles={{
                       td: {
                           borderColor:
                               colorScheme === 'light'
                                   ? 'var(--mantine-color-gray-5)'
                                   : 'var(--mantine-color-dark-2)',
                       },
                       tr: {
                           borderColor:
                               colorScheme === 'light'
                                   ? 'var(--mantine-color-gray-4)'
                                   : 'var(--mantine-color-dark-3)',
                       },
                   }}
            >
                <Table.Tbody>

                    {/* 1 — Author */}
                    <Table.Tr>
                        <Table.Td fw={700} w={150} style={labelStyle}>Author</Table.Td>
                        <Table.Td>{item.authorName}</Table.Td>
                    </Table.Tr>

                    {/* 2 — ISBN/ISSN (só aparece se existir) */}
                    {item.identifier && (
                        <Table.Tr>
                            <Table.Td fw={700} style={labelStyle}>{identifierLabel}</Table.Td>
                            <Table.Td>{item.identifier}</Table.Td>
                        </Table.Tr>
                    )}

                    {/* 3 — Type */}
                    <Table.Tr>
                        <Table.Td fw={700} style={labelStyle}>Type</Table.Td>
                        <Table.Td>{item.publicationType}</Table.Td>
                    </Table.Tr>

                </Table.Tbody>
            </Table>
        </Paper>
    );
}