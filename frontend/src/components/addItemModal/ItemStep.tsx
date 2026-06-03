import {
    Select,
    Stack,
    Textarea,
    TextInput,
} from '@mantine/core';
import type { Dispatch, SetStateAction } from 'react';

interface ItemData {
    condition: string;
    description: string;
    picture: string;
}

interface ItemStepProps {
    data: ItemData;
    setData: Dispatch<SetStateAction<ItemData>>;
}

export function ItemStep({
                             data,
                             setData,
                         }: ItemStepProps) {
    return (
        <Stack>

            <Select
                label="Condition"
                placeholder="Select item condition"
                data={[
                    'NEW',
                    'LIKE_NEW',
                    'VERY_GOOD',
                    'GOOD',
                    'ACCEPTABLE',
                ]}
                value={data.condition}
                onChange={(value) =>
                    setData((current) => ({
                        ...current,
                        condition: value || '',
                    }))
                }
                required
            />

            <Textarea
                label="Description"
                placeholder="Describe the item condition"
                minRows={4}
                value={data.description}
                onChange={(event) =>{
                    const value = event.currentTarget.value;

                    setData((current) => ({
                        ...current,
                        description: value,
                    }));
                }}
                required
            />

            <TextInput
                label="Picture URL"
                placeholder="https://example.com/image.jpg"
                value={data.picture}
                onChange={(event) =>
                    setData((current) => ({
                        ...current,
                        picture: event.currentTarget.value,
                    }))
                }
            />

        </Stack>
    );
}