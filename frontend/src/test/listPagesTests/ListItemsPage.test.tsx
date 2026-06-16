import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { MantineProvider } from "@mantine/core";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import ListItemsPage from "../../pages/lists/ListItemsPage.tsx";
import AppContext from "../../context/AppContext.jsx";
import { apiClient } from "../../services/apiClient.js";

// --- Mocks ---

vi.mock("../../services/apiClient", () => ({
    apiClient: {
        getByHref: vi.fn(),
        getItemById: vi.fn(),
    },
}));

vi.mock("../../components/addItemToListModal/AddItemToListDropDown", () => ({
    AddItemToListDropDown: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
}));

vi.mock("../../components/lists/DeleteItemFromListModal", () => ({
    DeleteItemFromListModal: ({ itemName }: { itemName: string }) => (
        <button>Delete {itemName}</button>
    ),
}));

vi.mock("../../context/lists/ListsActions", () => ({
    addItemToList: vi.fn(),
}));

// --- Helpers ---

const mockItem = {
    itemId: "item-1",
    title: "The Great Gatsby",
    authorName: "F. Scott Fitzgerald",
    publishingYear: 1925,
    condition: "GOOD",
    description: "A novel about the American dream.",
    identifier: "978-0743273565",
    publicationTypeName: "Novel",
    picture: null,
};

const mockContextValue = {
    state: { app: { libraryHref: "http://localhost:8081/library" } },
    dispatch: vi.fn(),
};

// Owner list response (has remove-item HAL link)
const mockOwnerListResponse = {
    name: "My Reading List",
    itemsId: ["item-1"],
    _links: {
        "remove-item": { href: "http://localhost:8081/my-lists/1/items" },
    },
};

// Viewer list response (no remove-item HAL link)
const mockViewerListResponse = {
    name: "Shared List",
    itemsId: ["item-1"],
    _links: {},
};

const renderPage = (listId = "1") => {
    return render(
        <MantineProvider>
            <AppContext.Provider value={mockContextValue as any}>
                <MemoryRouter initialEntries={[`/lists/${listId}`]}>
                    <Routes>
                        <Route path="/lists/:listId" element={<ListItemsPage />} />
                    </Routes>
                </MemoryRouter>
            </AppContext.Provider>
        </MantineProvider>
    );
};

// --- Tests ---

describe("ListItemsPage", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    // --- Unit: empty state ---

    it("shows empty message when list has no items", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue({
            name: "Empty List",
            itemsId: [],
            _links: {},
        });

        renderPage();

        await waitFor(() => {
            expect(screen.getByText(/no items in this list/i)).toBeInTheDocument();
        });
    });

    // --- Unit: list name in title ---

    it("renders the list name as the page title", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue(mockViewerListResponse);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);

        renderPage();

        await waitFor(() => {
            expect(screen.getByText("Shared List")).toBeInTheDocument();
        });
    });

    // --- Unit: table columns ---

    it("renders item title, author and year in the table", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue(mockViewerListResponse);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);

        renderPage();

        await waitFor(() => {
            expect(screen.getByText("The Great Gatsby")).toBeInTheDocument();
            expect(screen.getByText("F. Scott Fitzgerald")).toBeInTheDocument();
            expect(screen.getByText("1925")).toBeInTheDocument();
        });
    });

    // --- Unit: owner vs viewer ---

    it("shows Remove column when user is owner", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue(mockOwnerListResponse);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);

        renderPage();

        await waitFor(() => {
            expect(screen.getByText(/remove/i)).toBeInTheDocument();
            expect(screen.getByText(/delete the great gatsby/i)).toBeInTheDocument();
        });
    });

    it("hides Remove column when user is not owner", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue(mockViewerListResponse);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);

        renderPage();

        await waitFor(() => {
            expect(screen.queryByText(/remove/i)).not.toBeInTheDocument();
            expect(screen.queryByText(/delete the great gatsby/i)).not.toBeInTheDocument();
        });
    });

    it("shows the Add Item button only when user is owner", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue(mockOwnerListResponse);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);

        renderPage();

        await waitFor(() => {
            expect(screen.queryByRole("button", { name: /add item/i })).not.toBeInTheDocument();
        });
    });

    it("hides the Add Item button when user is not owner", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue(mockViewerListResponse);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);

        renderPage();

        await waitFor(() => {
            expect(screen.queryByRole("button", { name: /add/i })).not.toBeInTheDocument();
        });
    });

    // --- Integration: row click opens modal ---

    it("opens the item detail modal when a row is clicked", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue(mockViewerListResponse);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);

        renderPage();

        await waitFor(() => {
            expect(screen.getByText("The Great Gatsby")).toBeInTheDocument();
        });

        await userEvent.click(screen.getByText("The Great Gatsby"));

        await waitFor(() => {
            expect(screen.getByText(/A novel about the American dream/)).toBeInTheDocument();
        });
    });

    // --- Integration: modal closes ---

    it("closes the modal when close button is clicked", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue(mockViewerListResponse);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);

        renderPage();

        await waitFor(() => screen.getByText("The Great Gatsby"));
        await userEvent.click(screen.getByText("The Great Gatsby"));
        await waitFor(() => screen.getByRole("button", { name: /close/i }));
        await userEvent.click(screen.getByRole("button", { name: /close/i }));

        await waitFor(() => {
            expect(screen.queryByText(/A novel about the American dream/)).not.toBeInTheDocument();
        });
    });

    // --- Integration: API failure ---

    it("handles API failure gracefully without crashing", async () => {
        vi.mocked(apiClient.getByHref).mockRejectedValue(new Error("Network error"));

        renderPage();

        await waitFor(() => {
            expect(screen.getByText(/list items/i)).toBeInTheDocument();
        });
    });

    it("renders items that resolved even if some API calls fail", async () => {
        vi.mocked(apiClient.getByHref).mockResolvedValue({
            name: "Partial List",
            itemsId: ["item-1", "item-2"],
            _links: {},
        });

        vi.mocked(apiClient.getItemById)
            .mockResolvedValueOnce(mockItem)
            .mockRejectedValueOnce(new Error("Not found"));

        renderPage();

        await waitFor(() => {
            expect(screen.getByText("The Great Gatsby")).toBeInTheDocument();
        });
    });
});