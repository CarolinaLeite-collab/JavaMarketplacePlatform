import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { MantineProvider } from "@mantine/core";
import { FiltersBar } from "../../components/lists/FiltersBar";

// --- Helpers ---

const mockGenres = [
    { value: "fiction", label: "Fiction" },
    { value: "horror", label: "Horror" },
    { value: "sci-fi", label: "Sci-Fi" },
];

const renderFiltersBar = (props = {}) =>
    render(
        <MantineProvider>
            <FiltersBar
                search=""
                onSearchChange={vi.fn()}
                genre={null}
                onGenreChange={vi.fn()}
                genres={mockGenres}
                {...props}
            />
        </MantineProvider>
    );

// --- Tests ---

describe("FiltersBar", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    // --- Unit: rendering ---

    it("renders the search input", () => {
        renderFiltersBar();
        expect(screen.getByPlaceholderText(/search by title/i)).toBeInTheDocument();
    });

    it("renders the genre select", () => {
        renderFiltersBar();
        expect(screen.getByPlaceholderText(/filter by genre/i)).toBeInTheDocument();
    });

    it("renders with the provided search value", () => {
        renderFiltersBar({ search: "Gatsby" });
        expect(screen.getByDisplayValue("Gatsby")).toBeInTheDocument();
    });

    it("renders with no genre selected when genre is null", () => {
        renderFiltersBar({ genre: null });
        const select = screen.getByPlaceholderText(/filter by genre/i);
        expect(select).toHaveValue("");
    });

    // --- Unit: search interaction ---

    it("calls onSearchChange when user types in the search input", async () => {
        const onSearchChange = vi.fn();
        renderFiltersBar({ onSearchChange });
        await userEvent.type(screen.getByPlaceholderText(/search by title/i), "Dune");
        expect(onSearchChange).toHaveBeenCalled();
        expect(onSearchChange).toHaveBeenLastCalledWith("Dune");
    });

    it("calls onSearchChange with empty string when input is cleared", async () => {
        const onSearchChange = vi.fn();
        renderFiltersBar({ search: "Dune", onSearchChange });
        await userEvent.clear(screen.getByPlaceholderText(/search by title/i));
        expect(onSearchChange).toHaveBeenLastCalledWith("");
    });

    // --- Unit: genre interaction ---

    it("calls onGenreChange when a genre is selected", async () => {
        const onGenreChange = vi.fn();
        renderFiltersBar({ onGenreChange });

        await userEvent.click(screen.getByPlaceholderText(/filter by genre/i));
        await waitFor(() => screen.getByText("Fiction"));
        await userEvent.click(screen.getByText("Fiction"));

        expect(onGenreChange).toHaveBeenCalledWith("fiction");
    });

    it("calls onGenreChange with null when genre is cleared", async () => {
        const onGenreChange = vi.fn();
        renderFiltersBar({ genre: "fiction", onGenreChange });

        // Mantine's clearable Select renders a clear button when a value is set
        const clearButton = screen.getByRole("button", { name: /clear/i });
        await userEvent.click(clearButton);

        expect(onGenreChange).toHaveBeenCalledWith(null);
    });

    it("renders all genre options in the dropdown", async () => {
        renderFiltersBar();
        await userEvent.click(screen.getByPlaceholderText(/filter by genre/i));
        await waitFor(() => {
            expect(screen.getByText("Fiction")).toBeInTheDocument();
            expect(screen.getByText("Horror")).toBeInTheDocument();
            expect(screen.getByText("Sci-Fi")).toBeInTheDocument();
        });
    });

    it("renders no options when genres list is empty", async () => {
        renderFiltersBar({ genres: [] });
        await userEvent.click(screen.getByPlaceholderText(/filter by genre/i));
        await waitFor(() => {
            expect(screen.queryByText("Fiction")).not.toBeInTheDocument();
        });
    });
});