import { Group, TextInput, Select } from "@mantine/core";

interface FiltersBarProps {
    search: string;
    onSearchChange: (value: string) => void;
    genre: string | null;
    onGenreChange: (value: string | null) => void;
    genres: string[];
}

export function FiltersBar({
                               search,
                               onSearchChange,
                               genre,
                               onGenreChange,
                               genres,
                           }: FiltersBarProps) {
    return (
        <Group grow mt="md" mb="md">
            <TextInput
                placeholder="Search by title"
                value={search}
                onChange={(e) => onSearchChange(e.currentTarget.value)}
            />

            <Select
                placeholder="Filter by genre"
                data={genres}
                value={genre}
                onChange={onGenreChange}
                clearable
            />
        </Group>
    );
}
