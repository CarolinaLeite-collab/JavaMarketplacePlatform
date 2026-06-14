import {NumberInput, Select, Stack, TextInput} from '@mantine/core';
import type {Dispatch, SetStateAction} from 'react';

interface PublicationData {
    title: string;
    authorName: string;
    releaseYear: number;
    genreName: string;
}

interface PublicationStepProps {
    data: PublicationData;
    setData: Dispatch<SetStateAction<PublicationData>>;
    authors: { value: string; label: string }[];
    genres: { value: string; label: string }[];
}

/**
 * Step responsible for collecting publication data required by the item registration flow.
 *
 * Author and genre values are selected from backend-loaded options instead of being
 * entered as free text, ensuring that the submitted payload references existing
 * backend resources.
 */

export function PublicationStep({
                                    data,
                                    setData,
                                    authors,
                                    genres,
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

            <Select
                label="Author"
                placeholder="Select author"
                data={authors}
                value={data.authorName}
                onChange={(value) =>
                    setData((current) => ({
                        ...current,
                        authorName: value || '',
                    }))
                }
                searchable
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
                data={genres}
                value={data.genreName}
                onChange={(value) =>
                    setData((current) => ({
                        ...current,
                        genreName: value || '',
                    }))
                }
                searchable
                required
            />
        </Stack>
    );
}