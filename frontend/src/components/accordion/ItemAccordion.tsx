import {Accordion, Box, Image} from '@mantine/core';
import {ItemDetailTable} from "../itemDetailsTable/ItemDetailsTable.tsx";
import { getLibraryDetail } from '../../context/library/LibraryActions';

export function ItemAccordion({ items, details, dispatch }) {

    const loadDetail = (href, itemId) => {
        if (details[itemId]) return;

        getLibraryDetail(dispatch, href, itemId);
    };

    return (
        <Box
            w="60%"
            mx="auto"
        >
            <Accordion chevronPosition="right"
                       variant="separated"
                       onChange={(itemId) => {
                            if (!itemId) return;

                            const item = items.find(i => i.itemId === itemId);
                            const href = item?.links.find(l => l.rel === 'self')?.href;

                            if (href) {
                                loadDetail(href, itemId);
                            }
                       }}
                       styles={{
                           item: {
                               border: '1px solid light-dark(var(--mantine-color-gray-4), '
                                   + 'var(--mantine-color-dark-3))',
                               borderRadius: 'var(--mantine-radius-md)',
                               marginBottom: '6px',
                           },
                           control: {
                               paddingTop: '6px',
                               paddingBottom: '6px',
                           }
                       }}
            >
                    {items.map(item => (
                    <Accordion.Item key={item.itemId} value={item.itemId}>
                        <Accordion.Control icon={<Image
                        src={item.picture}
                        alt={item.title}
                        w={52}
                        h={70}
                        fit="cover"
                        radius="sm"
                        fallbackSrc="/book-placeholder.png"/>
                        }>
                        {item.title}
                        </Accordion.Control>

                        <Accordion.Panel>
                            {details[item.itemId]
                                ? (
                                    <ItemDetailTable
                                        item={details[item.itemId]}
                                    />
                                )
                                : (
                                    <p>Loading...</p>
                                )}
                        </Accordion.Panel>
                    </Accordion.Item>
            ))}
            </Accordion>
        </Box>
    );
}