import {Accordion, Box, Image} from '@mantine/core';
import {ItemDetailTable} from "../itemDetailsTable/ItemDetailsTable.tsx";

interface ItemAccordion{
    itemId: string;
    title: string;
    imageUrl: string | null;
    publicationType: string;
    authorName: string;
    identifier: string | null;
}

interface Props {
    items: ItemAccordion [];
}

export function ItemAccordion({ items }: Props) {
    return (
        <Box
            w="60%"
            mx="auto"
        >
            <Accordion chevronPosition="right" variant="separeted"
                       styles={{
                           item: {
                               border: '1px solid light-dark(var(--mantine-color-gray-4), '
                                   + 'var(--mantine-color-dark-3))',
                               borderRadius: 'var(--mantine-radius-md)',
                               marginBottom: '6px',
                           }
                       }}
            >
                    {items.map(item => (
                    <Accordion.Item key={item.itemId} value={item.itemId}>
                        <Accordion.Control icon={<Image
                        src={item.imageUrl}
                        alt={item.title}
                        w={32}
                        h={32}
                        fit="cover"
                        radius="sm"
                        fallbackSrc="/book-placeholder.png"/>
                        }>
                        {item.title}
                        </Accordion.Control>

                        <Accordion.Panel>
                            <ItemDetailTable
                                item={{
                                    publicationType: 'Book',        //item.publicationType
                                    authorName: 'H.G. Wells',       //item.authorName
                                    identifier: '9781784872113'     //item.identifier
                                }}
                            />
                        </Accordion.Panel>
                    </Accordion.Item>
            ))}
            </Accordion>
        </Box>
    );
}