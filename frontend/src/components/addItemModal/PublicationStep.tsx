import { NumberInput, Select, Stack, TextInput } from '@mantine/core';
import type { Dispatch, SetStateAction } from 'react';

interface PublicationData {
    title: string;
    authorName: string;
    releaseYear: number;
    genreName: string;
}

interface PublicationStepProps {
    data: PublicationData;
    setData: Dispatch<SetStateAction<PublicationData>>;
}

export function PublicationStep({
                                    data,
                                    setData,
                                }: PublicationStepProps) {
    return (
        <Stack>
            <TextInput
                label="Title"
                placeholder="Insert publication title"
                value={data.title}
                onChange={(event) => {
                    const value = event.currentTarget.value;

                    setData((current) => ({
                        ...current,
                        title: value,
                    }));
                }}
                required
            />

            <TextInput
                label="Author"
                placeholder="Insert author name"
                value={data.authorName}
                onChange={(event) => {
                const value = event.currentTarget.value;

                setData((current) => ({
                    ...current,
                    authorName: value,
                }));
            }}
                required
                />

            <NumberInput
                label="Release Year"
                placeholder="Insert release year"
                value={data.releaseYear}
                onChange={(value) =>
                    setData((current) => ({
                        ...current,
                        releaseYear: Number(value) || 0,
                    }))
                }
                required
            />

            <Select
                label="Genre"
                placeholder="Select genre"
                data={[
                    'Fiction',
                    'Science Fiction',
                    'Fantasy',
                    'Romance',
                    'History',
                    'Biography',
                    'Other',
                ]}
                value={data.genreName}
                onChange={(value) =>
                    setData((current) => ({
                        ...current,
                        genreName: value || '',
                    }))
                }
                required
            />
        </Stack>
    );
}