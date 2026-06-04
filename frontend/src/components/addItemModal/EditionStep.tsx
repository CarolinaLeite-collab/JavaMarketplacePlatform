import {NumberInput, SimpleGrid, Stack, TextInput,} from '@mantine/core';
import type {Dispatch, SetStateAction} from 'react';

interface EditionData {
    identifier: string;
    publicationType: string;
    editionLanguage: string;
    publishingCompany: string;
    publishingYear: number;
    weight: string;
    dimension: string;
    binding: string;
    numberOfPages: number;
    editionNumber: number;
}

interface EditionStepProps {
    data: EditionData;
    setData: Dispatch<SetStateAction<EditionData>>;
}

export function EditionStep({
                                data,
                                setData,
                            }: EditionStepProps) {
    return (
        <Stack>

            <SimpleGrid cols={2}>
                <TextInput
                    label="Publication Type"
                    placeholder="Type of publication"
                    value={data.publicationType}
                    onChange={(event) =>{
                        const value = event.currentTarget.value;

                        setData((current) => ({
                        ...current,
                        publicationType: value,
                    }));
                    }}
                    required
                />

                <TextInput
                    label="ISBN/ISSN"
                    placeholder="Identifier"
                    value={data.identifier}
                    onChange={(event) =>{
                        const value = event.currentTarget.value;

                        setData((current) => ({
                            ...current,
                            identifier: value,
                        }));
                    }}
                    required
                />
            </SimpleGrid>

            <SimpleGrid cols={3}>
                <TextInput
                    label="Language"
                    placeholder="Enter language"
                    value={data.editionLanguage}
                    onChange={(event) =>{
                        const value = event.currentTarget.value;

                        setData((current) => ({
                            ...current,
                            editionLanguage: value,
                        }));
                    }}
                    required
                />

                <TextInput
                    label="Publishing Company"
                    placeholder="Publishing Company name"
                    value={data.publishingCompany}
                    onChange={(event) =>{
                        const value = event.currentTarget.value;

                        setData((current) => ({
                            ...current,
                            publishingCompany: value,
                        }));
                    }}
                    required
                />

                <NumberInput
                    label="Publishing Year"
                    placeholder="Enter year"
                    value={data.publishingYear}
                    onChange={(value) =>
                        setData((current) => ({
                            ...current,
                            publishingYear: Number(value) || 0,
                        }))
                    }
                    required
                />
            </SimpleGrid>

            <SimpleGrid cols={3}>
                <TextInput
                    label="Weight"
                    placeholder="Enter weight"
                    value={data.weight}
                    onChange={(event) =>{
                        const value = event.currentTarget.value;

                        setData((current) => ({
                            ...current,
                            weight: value,
                        }));
                    }}
                />

                <TextInput
                    label="Dimension"
                    placeholder="Enter dimension"
                    value={data.dimension}
                    onChange={(event) =>{
                        const value = event.currentTarget.value;

                        setData((current) => ({
                            ...current,
                            dimension: value,
                        }));
                    }}
                />

                <TextInput
                    label="Binding"
                    placeholder="Enter binding"
                    value={data.binding}
                    onChange={(event) =>{
                        const value = event.currentTarget.value;

                        setData((current) => ({
                            ...current,
                            binding: value,
                        }));
                    }}
                />
            </SimpleGrid>

            <SimpleGrid cols={2}>
                <NumberInput
                    label="Number of Pages"
                    placeholder="Enter number of pages"
                    value={data.numberOfPages}
                    onChange={(value) =>
                        setData((current) => ({
                            ...current,
                            numberOfPages: Number(value) || 0,
                        }))
                    }
                />

                <NumberInput
                    label="Edition Number"
                    placeholder="Enter edition number"
                    value={data.editionNumber}
                    onChange={(value) =>
                        setData((current) => ({
                            ...current,
                            editionNumber: Number(value) || 0,
                        }))
                    }
                />
            </SimpleGrid>

        </Stack>
    );
}