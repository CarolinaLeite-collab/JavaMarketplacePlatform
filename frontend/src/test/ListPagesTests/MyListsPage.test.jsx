import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { MantineProvider } from "@mantine/core";
import { MemoryRouter } from "react-router-dom";
import MyListsPage from "../../pages/lists/MyListsPage";
import AppContext from "../../context/AppContext";
import { getGenres } from "../../context/lists/ListsActions";

// --- Mocks ---

vi.mock("../../context/lists/ListsActions", () => ({
    getGenres: vi.fn(),
}));

vi.mock("../../components/layout/DefaultLayout", () => ({
    DefaultLayout: ({ children, title }) => (
        <div>
            <h1>{title}</h1>
            <footer style={{ height: "60px" }} />
            {children}
        </div>
    ),
}));

vi.mock("../../components/lists/tablelist/TableList", () => ({
    TableList: ({ search, genre }) => (
        <div data-testid="table-list" data-search={search} data-genre={genre ?? ""} />
    ),
}));

vi.mock("../../components/lists/newlistmodal/NewListModal", () => ({
    NewListModal: () => <button>New List</button>,
}));

vi.mock("../../components/lists/FiltersBar", () => ({
    FiltersBar: ({ search, onSearchChange, genre, onGenreChange, genres }) => (
        <div>
            <input
                data-testid="search-input"
                value={search}
                onChange={(e) => onSearchChange(e.target.value)}
                placeholder="Search"
            />
            <select
                data-testid="genre-select"
                value={genre ?? ""}
                onChange={(e) => onGenreChange(e.target.value || null)}
            >
                <option value="">All</option>
                {genres?.map((g) => <option key={g} value={g}>{g}</option>)}
            </select>
        </div>
    ),
}));

// --- Helpers ---

const mockContextValue = (genresHref = "http://localhost:8081/genres", genres = []) => ({
    state: {
        app: { genresHref },
        lists: { genres },
    },
    dispatch: vi.fn(),
});

const renderPage = (contextValue = mockContextValue()) => {
    return render(
        <MantineProvider>
            <AppContext.Provider value={contextValue}>
                <MemoryRouter>
                    <MyListsPage />
                </MemoryRouter>
            </AppContext.Provider>
        </MantineProvider>
    );
};

// --- Tests ---

describe("MyListsPage", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    // --- Unit: rendering ---

    it("renders the page title", () => {
        renderPage();
        expect(screen.getByText("My Lists")).toBeInTheDocument();
    });

    it("renders the FiltersBar", () => {
        renderPage();
        expect(screen.getByTestId("search-input")).toBeInTheDocument();
        expect(screen.getByTestId("genre-select")).toBeInTheDocument();
    });

    it("renders the TableList", () => {
        renderPage();
        expect(screen.getByTestId("table-list")).toBeInTheDocument();
    });

    it("renders the New List button", () => {
        renderPage();
        expect(screen.getByRole("button", { name: /new list/i })).toBeInTheDocument();
    });

    // --- Unit: genres fetch ---

    it("calls getGenres when genresHref is available", () => {
        const ctx = mockContextValue("http://localhost:8081/genres");
        renderPage(ctx);
        expect(getGenres).toHaveBeenCalledWith(ctx.dispatch, "http://localhost:8081/genres");
    });

    it("does not call getGenres when genresHref is missing", () => {
        const ctx = mockContextValue("");
        renderPage(ctx);
        expect(getGenres).not.toHaveBeenCalled();
    });

    // --- Unit: filters state passed to TableList ---

    it("passes empty search and null genre to TableList initially", () => {
        renderPage();
        const tableList = screen.getByTestId("table-list");
        expect(tableList).toHaveAttribute("data-search", "");
        expect(tableList).toHaveAttribute("data-genre", "");
    });

    it("updates search value passed to TableList on input", async () => {
        renderPage();
        await userEvent.type(screen.getByTestId("search-input"), "Fantasy");
        await waitFor(() => {
            expect(screen.getByTestId("table-list")).toHaveAttribute("data-search", "Fantasy");
        });
    });

    it("updates genre value passed to TableList on select change", async () => {
        const ctx = mockContextValue("http://localhost:8081/genres", ["Fiction", "Horror"]);
        renderPage(ctx);
        await userEvent.selectOptions(screen.getByTestId("genre-select"), "Fiction");
        await waitFor(() => {
            expect(screen.getByTestId("table-list")).toHaveAttribute("data-genre", "Fiction");
        });
    });

    it("resets genre to null when All is selected", async () => {
        const ctx = mockContextValue("http://localhost:8081/genres", ["Fiction"]);
        renderPage(ctx);
        await userEvent.selectOptions(screen.getByTestId("genre-select"), "Fiction");
        await userEvent.selectOptions(screen.getByTestId("genre-select"), "");
        await waitFor(() => {
            expect(screen.getByTestId("table-list")).toHaveAttribute("data-genre", "");
        });
    });
});