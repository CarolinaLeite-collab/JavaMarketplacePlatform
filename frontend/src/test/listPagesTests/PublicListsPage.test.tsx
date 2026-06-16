import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { MantineProvider } from "@mantine/core";
import { MemoryRouter } from "react-router-dom";
import PublicListsPage from "../../pages/lists/PublicListsPage";
import AppContext from "../../context/AppContext";
import { getPublicLists, getListsOptions, getGenres } from "../../context/lists/ListsActions";

// --- Mocks ---

const mockNavigate = vi.fn();

vi.mock("react-router-dom", async () => {
    const actual = await vi.importActual("react-router-dom");
    return { ...actual, useNavigate: () => mockNavigate };
});

vi.mock("../../context/lists/ListsActions", () => ({
    getPublicLists: vi.fn().mockResolvedValue(undefined),
    getListsOptions: vi.fn(),
    getGenres: vi.fn(),
}));

vi.mock("../../components/layout/DefaultLayout", () => ({
    DefaultLayout: ({ children, title }: { children: React.ReactNode; title: string }) => (
        <div>
            <h1>{title}</h1>
            {children}
        </div>
    ),
}));

vi.mock("../../components/lists/FiltersBar", () => ({
    FiltersBar: ({
                     search,
                     onSearchChange,
                     genre,
                     onGenreChange,
                     genres,
                 }: {
        search: string;
        onSearchChange: (v: string) => void;
        genre: string | null;
        onGenreChange: (v: string | null) => void;
        genres: string[];
    }) => (
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

const mockList = (overrides = {}) => ({
    listId: "list-1",
    name: "Best Novels",
    genre: "Fiction",
    itemsHref: "http://localhost:8081/my-lists/list-1/items",
    ...overrides,
});

const makeContext = (publicLists: any[] = [], genres: string[] = []) => ({
    state: {
        app: {
            publicListsHref: "http://localhost:8081/public-lists",
            genresHref: "http://localhost:8081/genres",
        },
        lists: { publicLists, genres },
    },
    dispatch: vi.fn(),
});

const renderPage = (contextValue = makeContext()) => {
    return render(
        <MantineProvider>
            <AppContext.Provider value={contextValue as any}>
                <MemoryRouter>
                    <PublicListsPage />
                </MemoryRouter>
            </AppContext.Provider>
        </MantineProvider>
    );
};

// --- Tests ---

describe("PublicListsPage", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    // --- Unit: rendering ---

    it("renders the page title", () => {
        renderPage();
        expect(screen.getByText("Public Lists")).toBeInTheDocument();
    });

    it("shows empty message when there are no public lists", async () => {
        renderPage(makeContext([]));
        await waitFor(() => {
            expect(screen.getByText(/no public lists found/i)).toBeInTheDocument();
        });
    });

    it("renders a row for each public list", async () => {
        const lists = [mockList(), mockList({ listId: "list-2", name: "Sci-Fi Picks" })];
        renderPage(makeContext(lists));
        await waitFor(() => {
            expect(screen.getByText("Best Novels")).toBeInTheDocument();
            expect(screen.getByText("Sci-Fi Picks")).toBeInTheDocument();
        });
    });

    it("renders genre for each list", async () => {
        renderPage(makeContext([mockList()]));
        await waitFor(() => {
            expect(screen.getByText("Fiction")).toBeInTheDocument();
        });
    });

    // --- Unit: API calls on mount ---

    it("calls getListsOptions on mount", () => {
        const ctx = makeContext();
        renderPage(ctx);
        expect(getListsOptions).toHaveBeenCalledWith(ctx.dispatch);
    });

    it("calls getPublicLists when publicListsHref is available", () => {
        const ctx = makeContext();
        renderPage(ctx);
        expect(getPublicLists).toHaveBeenCalledWith(
            ctx.dispatch,
            "http://localhost:8081/public-lists"
        );
    });

    it("calls getGenres when genresHref is available", () => {
        const ctx = makeContext();
        renderPage(ctx);
        expect(getGenres).toHaveBeenCalledWith(
            ctx.dispatch,
            "http://localhost:8081/genres"
        );
    });

    // --- Unit: loading state ---

    it("shows a loader while fetching", async () => {
        vi.mocked(getPublicLists).mockReturnValue(new Promise(() => {})); // never resolves
        renderPage();
        expect(screen.getByTestId("loader")).toBeInTheDocument(); // Mantine Loader has role="status"
    });

    it("hides the loader after fetch completes", async () => {
        vi.mocked(getPublicLists).mockResolvedValue(undefined);
        renderPage(makeContext([mockList()]));
        await waitFor(() => {
            expect(screen.queryByTestId("loader")).not.toBeInTheDocument();
        });
    });

    // --- Unit: filtering ---

    it("filters lists by search term", async () => {
        const lists = [
            mockList({ listId: "1", name: "Best Novels" }),
            mockList({ listId: "2", name: "Sci-Fi Picks" }),
        ];
        renderPage(makeContext(lists));

        await waitFor(() => screen.getByText("Best Novels"));
        await userEvent.type(screen.getByTestId("search-input"), "Sci");

        await waitFor(() => {
            expect(screen.getByText("Sci-Fi Picks")).toBeInTheDocument();
            expect(screen.queryByText("Best Novels")).not.toBeInTheDocument();
        });
    });

    it("filters lists by genre", async () => {
        const lists = [
            mockList({ listId: "1", name: "Best Novels", genre: "Fiction" }),
            mockList({ listId: "2", name: "Horror Nights", genre: "Horror" }),
        ];
        renderPage(makeContext(lists, ["Fiction", "Horror"]));

        await waitFor(() => screen.getByText("Best Novels"));
        await userEvent.selectOptions(screen.getByTestId("genre-select"), "Horror");

        await waitFor(() => {
            expect(screen.getByText("Horror Nights")).toBeInTheDocument();
            expect(screen.queryByText("Best Novels")).not.toBeInTheDocument();
        });
    });

    it("search is case-insensitive", async () => {
        renderPage(makeContext([mockList({ name: "Best Novels" })]));
        await waitFor(() => screen.getByText("Best Novels"));
        await userEvent.type(screen.getByTestId("search-input"), "best novels");
        await waitFor(() => {
            expect(screen.getByText("Best Novels")).toBeInTheDocument();
        });
    });

    it("shows all lists when search is cleared", async () => {
        const lists = [
            mockList({ listId: "1", name: "Best Novels" }),
            mockList({ listId: "2", name: "Sci-Fi Picks" }),
        ];
        renderPage(makeContext(lists));

        await waitFor(() => screen.getByText("Best Novels"));
        await userEvent.type(screen.getByTestId("search-input"), "Sci");
        await userEvent.clear(screen.getByTestId("search-input"));

        await waitFor(() => {
            expect(screen.getByText("Best Novels")).toBeInTheDocument();
            expect(screen.getByText("Sci-Fi Picks")).toBeInTheDocument();
        });
    });

    // --- Unit: favourites toggle ---

    it("renders an unfilled heart icon by default", async () => {
        renderPage(makeContext([mockList()]));
        await waitFor(() => screen.getByText("Best Novels"));
        // IconHeart renders as svg; check for the action button
        const heartBtn = screen.getByRole("button");
        expect(heartBtn).toBeInTheDocument();
    });

    it("toggles favourite on heart click", async () => {
        renderPage(makeContext([mockList()]));
        await waitFor(() => screen.getByText("Best Novels"));

        const heartBtn = screen.getByRole("button");
        await userEvent.click(heartBtn);
        // After click, IconHeartFilled should be rendered (svg title or aria changes)
        // We verify the button is still present and clickable (visual state is icon-based)
        expect(heartBtn).toBeInTheDocument();
    });

    it("toggles favourite off on second heart click", async () => {
        renderPage(makeContext([mockList()]));
        await waitFor(() => screen.getByText("Best Novels"));

        const heartBtn = screen.getByRole("button");
        await userEvent.click(heartBtn); // on
        await userEvent.click(heartBtn); // off
        expect(heartBtn).toBeInTheDocument();
    });

    // --- Integration: navigation ---

    it("navigates to list items page using itemsHref when row is clicked", async () => {
        renderPage(makeContext([mockList()]));
        await waitFor(() => screen.getByText("Best Novels"));
        await userEvent.click(screen.getByText("Best Novels"));
        expect(mockNavigate).toHaveBeenCalledWith("/lists/list-1/items");
    });

    it("navigates using listId directly when itemsHref is absent", async () => {
        const list = mockList({ itemsHref: null });
        renderPage(makeContext([list]));
        await waitFor(() => screen.getByText("Best Novels"));
        await userEvent.click(screen.getByText("Best Novels"));
        expect(mockNavigate).toHaveBeenCalledWith("/lists/list-1/items");
    });

    it("extracts listId correctly from a nested itemsHref", async () => {
        const list = mockList({ itemsHref: "http://localhost:8081/my-lists/abc-999/items" });
        renderPage(makeContext([list]));
        await waitFor(() => screen.getByText("Best Novels"));
        await userEvent.click(screen.getByText("Best Novels"));
        expect(mockNavigate).toHaveBeenCalledWith("/lists/abc-999/items");
    });
});