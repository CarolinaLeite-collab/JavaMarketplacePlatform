import {Table, useComputedColorScheme} from '@mantine/core';

interface ItemDetail {
    publicationType: string;
    authorName: string;
    identifier: string | null;
}

interface Props {
    item: ItemDetail;
}

export function ItemDetailTable({ item }: Props) {
    const colorScheme = useComputedColorScheme('light');

    const identifierLabel =
        item.publicationType === 'Book'     ? 'ISBN' :
        item.publicationType === 'Magazine' ? 'ISSN' :
        'Identifier';

    return (
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
                <Table.Tr>
                    <Table.Td fw={700} w={150}
                      style={{
                          backgroundColor:
                              colorScheme === 'light'
                                  ? 'var(--mantine-color-gray-3)'
                                  : 'var(--mantine-color-dark-4)',
                      }}
                    >Type</Table.Td>
                    <Table.Td>{item.publicationType}</Table.Td>
                </Table.Tr>

                <Table.Tr>
                    <Table.Td fw={700}
                      style={{
                          backgroundColor:
                              colorScheme === 'light'
                                  ? 'var(--mantine-color-gray-3)'
                                  : 'var(--mantine-color-dark-4)',
                      }}
                    >Author</Table.Td>
                    <Table.Td>{item.authorName}</Table.Td>
                </Table.Tr>

                {item.identifier && (
                    <Table.Tr>
                        <Table.Td fw={700}
                          style={{
                              backgroundColor:
                                  colorScheme === 'light'
                                      ? 'var(--mantine-color-gray-3)'
                                      : 'var(--mantine-color-dark-4)',
                          }}
                        >
                        {identifierLabel}
                        </Table.Td>
                        <Table.Td>{item.identifier}</Table.Td>
                    </Table.Tr>
                )}
            </Table.Tbody>
        </Table>
    );
}