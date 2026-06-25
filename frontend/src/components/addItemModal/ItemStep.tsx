import {Select, Stack, Textarea, TextInput,} from '@mantine/core';
import type {Dispatch, SetStateAction} from 'react';

interface ItemData {
    condition: string;
    description: string;
    picture: string;
}

interface ItemStepProps {
    data: ItemData;
    setData: Dispatch<SetStateAction<ItemData>>;
    errors?: Record<string, string>;
}

/**
 * Step responsible for collecting item-specific information.
 *
 * The condition options are aligned with the backend item condition enum. The
 * picture URL is kept as UI data but is not currently submitted in the item
 * creation request. Validation errors, when provided, are shown inline under
 * each corresponding required field.
 */

export function ItemStep({
                             data,
                             setData,
                             errors = {},
                         }: ItemStepProps) {
    return (
        <Stack>

            <Select
                label="Condition"
                placeholder="Select item condition"
                data={[
                    'MINT',
                    'GOOD',
                    'FAIR',
                    'POOR',
                ]}
                value={data.condition}
                onChange={(value) =>
                    setData((current) => ({
                        ...current,
                        condition: value || '',
                    }))
                }
                required
                error={errors.condition}
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
                error={errors.description}
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