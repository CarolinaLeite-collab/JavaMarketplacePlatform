import { useState } from "react";
import {
    IconChevronDown,
    IconChevronUp,
    IconSelector,
    IconSearch,
    IconTrash,
} from "@tabler/icons-react";
import {
    ActionIcon,
    Center,
    Group,
    ScrollArea,
    Table,
    Text,
    TextInput,
    UnstyledButton,
} from "@mantine/core";
import classes from "./TableSales.module.css";

interface RowData {
    title: string;
    genre: string;
    expiresIn: number;
    price: string;
    saleType: "Direct sale" | "Auction";
}

interface ThProps {
    children: React.ReactNode;
    reversed: boolean;
    sorted: boolean;
    onSort: () => void;
    width?: string;
}

function Th({ children, reversed, sorted, onSort, width }: ThProps) {
    const Icon = sorted ? (reversed ? IconChevronUp : IconChevronDown) : IconSelector;

    return (
        <Table.Th style={{ width }}>
            <UnstyledButton onClick={onSort} className={classes.control}>
                <Group justify="space-between">
                    <Text fw={500} fz="sm">{children}</Text>
                    <Center className={classes.icon}>
                        <Icon size={16} stroke={1.5} />
                    </Center>
                </Group>
            </UnstyledButton>
        </Table.Th>
    );
}

function filterData(data: RowData[], search: string) {
    const query = search.toLowerCase().trim();

    return data.filter((item) =>
        (["title", "genre", "price", "saleType"] as (keyof RowData)[]).some((key) =>
            String(item[key]).toLowerCase().includes(query)
        ) || String(item.expiresIn).includes(query)
    );
}

function sortData(
    data: RowData[],
    payload: { sortBy: keyof RowData | null; reversed: boolean; search: string }
) {
    const { sortBy } = payload;

    if (!sortBy) return filterData(data, payload.search);

    return filterData(
        [...data].sort((a, b) => {
            const aVal = String(a[sortBy] ?? "");
            const bVal = String(b[sortBy] ?? "");

            return payload.reversed
                ? bVal.localeCompare(aVal)
                : aVal.localeCompare(bVal);
        }),
        payload.search
    );
}

const data: RowData[] = [
    {
        title: "1984",
        genre: "Dystopian Fiction",
        expiresIn: 7,
        price: "12.50 EUR",
        saleType: "Direct sale",
    },
    {
        title: "The Catcher in the Rye",
        genre: "Fiction",
        expiresIn: 3,
        price: "8.00 EUR",
        saleType: "Direct sale",
    },
    {
        title: "Pride and Prejudice",
        genre: "Romance",
        expiresIn: 10,
        price: "15.00 EUR",
        saleType: "Direct sale",
    },
];

export function TableSales() {
    const [search, setSearch] = useState("");
    const [sortedData, setSortedData] = useState(data);
    const [sortBy, setSortBy] = useState<keyof RowData | null>(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);

    const setSorting = (field: keyof RowData) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
        setSortedData(sortData(data, { sortBy: field, reversed, search }));
    };

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value } = event.currentTarget;
        setSearch(value);
        setSortedData(sortData(data, { sortBy, reversed: reverseSortDirection, search: value }));
    };

    const rows = sortedData.map((row) => (
        <Table.Tr key={`${row.title}-${row.price}`}>
            <Table.Td>{row.title}</Table.Td>
            <Table.Td>{row.genre}</Table.Td>
            <Table.Td>
                <Text fz="sm">{row.expiresIn} day{row.expiresIn !== 1 ? "s" : ""} left</Text>
            </Table.Td>
            <Table.Td>{row.price}</Table.Td>
            <Table.Td>{row.saleType}</Table.Td>
            <Table.Td w={50}>
                <Center>
                    <ActionIcon variant="filled" color="red" size="md" radius="sm">
                        <IconTrash size={20} stroke={1.5} />
                    </ActionIcon>
                </Center>
            </Table.Td>
        </Table.Tr>
    ));

    return (
        <ScrollArea>
            <TextInput
                placeholder="Search by title, genre, price or sale type"
                mb="md"
                leftSection={<IconSearch size={16} stroke={1.5} />}
                value={search}
                onChange={handleSearchChange}
            />

            <Table horizontalSpacing="md" verticalSpacing="xs" miw={900} layout="fixed">
                <Table.Thead>
                    <Table.Tr>
                        <Th
                            width="24%"
                            sorted={sortBy === "title"}
                            reversed={reverseSortDirection}
                            onSort={() => setSorting("title")}
                        >
                            Title
                        </Th>
                        <Th
                            width="16%"
                            sorted={sortBy === "genre"}
                            reversed={reverseSortDirection}
                            onSort={() => setSorting("genre")}
                        >
                            Genre
                        </Th>
                        <Th
                            width="16%"
                            sorted={sortBy === "expiresIn"}
                            reversed={reverseSortDirection}
                            onSort={() => setSorting("expiresIn")}
                        >
                            Expires In
                        </Th>
                        <Th
                            width="16%"
                            sorted={sortBy === "price"}
                            reversed={reverseSortDirection}
                            onSort={() => setSorting("price")}
                        >
                            Price
                        </Th>
                        <Th
                            width="18%"
                            sorted={sortBy === "saleType"}
                            reversed={reverseSortDirection}
                            onSort={() => setSorting("saleType")}
                        >
                            Type of Sale
                        </Th>
                        <Table.Th w={78}>
                            <Center>
                                <Text fw={500} fz="sm">Delete</Text>
                            </Center>
                        </Table.Th>
                    </Table.Tr>
                </Table.Thead>

                <Table.Tbody>
                    {rows.length > 0 ? (
                        rows
                    ) : (
                        <Table.Tr>
                            <Table.Td colSpan={6}>
                                <Text fw={500} ta="center">Nothing found</Text>
                            </Table.Td>
                        </Table.Tr>
                    )}
                </Table.Tbody>
            </Table>
        </ScrollArea>
    );
}