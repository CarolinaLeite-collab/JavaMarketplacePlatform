import {NumberInput, Select, SimpleGrid, Stack, TextInput,} from '@mantine/core';
import type {Dispatch, SetStateAction} from 'react';

interface EditionData {
    publicationTypeId: string;
    publishingCompanyId: string;
    publishingYear: number;
    language: string;
    identifier: string;
    dimension: {
        width: number;
        height: number;
        thickness: number;
        unit: string;
    };
    weight: {
        value: number;
        unit: string;
    };
    numberOfPages: number;
    editionNumber: number;
    binding: string;
}

interface EditionStepProps {
    data: EditionData;
    setData: Dispatch<SetStateAction<EditionData>>;
    publicationTypes: { value: string; label: string }[];
    publishingCompanies: { value: string; label: string }[];
}

/**
 * Step responsible for collecting edition data for the selected publication.
 *
 * Publication type and publishing company values are provided by backend-loaded
 * options. Optional edition fields are collected independently and are only sent
 * by the parent modal when they contain valid values.
 */

export function EditionStep({
                                 data,
                                 setData,
                                 publicationTypes,
                                 publishingCompanies,
                             }: EditionStepProps) {
    return (
        <Stack>

            <SimpleGrid cols={2}>
                <Select
                    label="Publication Type"
                    placeholder="Select publication type"
                    data={publicationTypes}
                    value={data.publicationTypeId}
                    onChange={(value) =>
                        setData((current) => ({
                            ...current,
                            publicationTypeId: value || '',
                        }))
                    }
                    searchable
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
                />
            </SimpleGrid>

            <SimpleGrid cols={3}>
                <Select
                    label="Language"
                    placeholder="Select language"
                    data={[
                        'ENGLISH',
                        'PORTUGUESE',
                        'SPANISH',
                        'FRENCH',
                        'GERMAN',
                        'ITALIAN',
                        'DUTCH',
                        'SWEDISH',
                        'NORWEGIAN',
                        'DANISH',
                        'FINNISH',
                        'POLISH',
                        'CZECH',
                        'SLOVAK',
                        'HUNGARIAN',
                        'ROMANIAN',
                        'BULGARIAN',
                        'GREEK',
                        'CROATIAN',
                        'SERBIAN',
                        'UKRAINIAN',
                        'RUSSIAN',
                        'CATALAN',
                        'ARABIC',
                        'HEBREW',
                        'TURKISH',
                        'PERSIAN',
                        'CHINESE',
                        'JAPANESE',
                        'KOREAN',
                        'HINDI',
                        'BENGALI',
                        'THAI',
                        'VIETNAMESE',
                        'INDONESIAN',
                        'MALAY',
                        'SWAHILI',
                        'AFRIKAANS',
                        'AMHARIC',
                        'HAUSA',
                        'YORUBA',
                        'ENGLISH_US',
                        'HAITIAN_CREOLE',
                        'NAHUATL',
                        'MAYA',
                        'GUARANI',
                        'PORTUGUESE_BR',
                        'LATIN',
                        'ANCIENT_GREEK',
                        'SANSKRIT',
                        'CLASSICAL_ARABIC',
                        'OLD_ENGLISH',
                        'OLD_NORSE',
                        'ARAMAIC',
                        'COPTIC',
                        'SUMERIAN',
                        'MIDDLE_ENGLISH',
                        'OLD_PORTUGUESE'
                    ]}
                    value={data.language}
                    onChange={(value) =>
                        setData((current) => ({
                            ...current,
                            language: value || '',
                        }))
                    }
                    required
                />

                <Select
                    label="Publishing Company"
                    placeholder="Select publishing company"
                    data={publishingCompanies}
                    value={data.publishingCompanyId}
                    onChange={(value) =>
                        setData((current) => ({
                            ...current,
                            publishingCompanyId: value || '',
                        }))
                    }
                    searchable
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
                <NumberInput
                    label="Weight"
                    placeholder="Enter weight"
                    value={data.weight.value}
                    onChange={(value) =>
                        setData((current) => ({
                            ...current,
                            weight: {
                                ...current.weight,
                                value: Number(value) || 0,
                            },
                        }))
                    }
                />

                <TextInput
                    label="Weight Unit"
                    placeholder="GRAMS"
                    value={data.weight.unit}
                    onChange={(event) =>
                        setData((current) => ({
                            ...current,
                            weight: {
                                ...current.weight,
                                unit: event.currentTarget.value,
                            },
                        }))
                    }
                />

                <NumberInput
                    label="Width"
                    value={data.dimension.width}
                    onChange={(value) =>
                    setData((current) => ({
                        ...current,
                        dimension: {
                            ...current.dimension,
                            width: Number(value) || 0,
                        },
                    }))
                }
                    />

                <Select
                    label="Binding"
                    placeholder="Select binding"
                    data={[
                        'PUR',
                        'PAPERBACK',
                        'SADDLE_STITCH',
                        'HARDCOVER',
                        'SINGER_SEWN',
                        'SECTION_SEWN',
                        'COPTIC_STITCH',
                        'WIRO',
                        'INTERSCREW',
                    ]}
                    value={data.binding}
                    onChange={(value) =>
                        setData((current) => ({
                            ...current,
                            binding: value || '',
                        }))
                    }
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
