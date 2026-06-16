import { render, screen, within, waitFor, waitForElementToBeRemoved } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { MantineProvider } from "@mantine/core";
import { DeleteItemFromListModal } from "../../components/lists/DeleteItemFromListModal";
import AppContext from "../../context/AppContext";
import { removeItemFromList } from "../../context/lists/ListsActions.jsx";

// --- Mocks ---

vi.mock("../../context/lists/ListsActions.jsx", () => ({
    removeItemFromList: vi.fn().mockResolvedValue(undefined),
}));

// --- Helpers ---

const mockLinks = [{ rel: "remove-item", href: "http://localhost:8081/my-lists/1/items" }];

const mockDispatch = vi.fn();

const renderModal = (props = {}) =>
    render(
        <MantineProvider>
            <AppContext.Provider value={{ dispatch: mockDispatch, state: {} } as any}>
                <DeleteItemFromListModal
                    itemName="The Great Gatsby"
                    itemId="item-1"
                    links={mockLinks}
                    {...props}
                />
            </AppContext.Provider>
        </MantineProvider>
    );

// --- Tests ---

describe("DeleteItemFromListModal", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    // --- Unit: rendering ---

    it("renders the Remove trigger button", () => {
        renderModal();
        expect(screen.getByRole("button", { name: /remove/i })).toBeInTheDocument();
    });

    it("does not show the modal on initial render", () => {
        renderModal();
        expect(screen.queryByText(/are you sure/i)).not.toBeInTheDocument();
    });

    // --- Unit: open modal ---

    it("opens the modal when Remove button is clicked", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /remove/i }));
        await waitFor(() => {
            expect(screen.getByText(/are you sure you want to remove this item/i)).toBeInTheDocument();
        });
    });

    it("shows the item name in the modal title", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /remove/i }));
        await waitFor(() => {
            expect(screen.getByText(/remove "the great gatsby"/i)).toBeInTheDocument();
        });
    });

    it("renders Cancel and Remove buttons inside the modal", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /remove/i }));
        await waitFor(() => {
            expect(screen.getByRole("button", { name: /cancel/i })).toBeInTheDocument();
        });
        // Two "Remove" buttons: trigger + modal confirm
        expect(screen.getAllByRole("button", { name: /remove/i })).toHaveLength(2);
    });

    // --- Unit: close modal ---

    it("closes the modal when Cancel is clicked", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /remove/i }));
        await waitFor(() => screen.getByRole("button", { name: /cancel/i }));
        await userEvent.click(screen.getByRole("button", { name: /cancel/i }));
        await waitFor(() => {
            expect(screen.queryByText(/are you sure/i)).not.toBeInTheDocument();
        });
    });

    it("closes the modal when the X button is clicked", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /remove/i }));
        await waitFor(() => screen.getByRole("button", { name: /close/i }));
        await userEvent.click(screen.getByRole("button", { name: /close/i }));
        await waitFor(() => {
            expect(screen.queryByText(/are you sure/i)).not.toBeInTheDocument();
        });
    });

    // --- Integration: delete ---

    it("calls removeItemFromList with correct args when Remove is confirmed", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /remove/i }));
        await waitFor(() => screen.getByRole("dialog"));

        const dialog = screen.getByRole("dialog");
        await userEvent.click(within(dialog).getByRole("button", { name: /remove/i }));

        await waitFor(() => {
            expect(screen.queryByText(/are you sure/i)).not.toBeInTheDocument();
        });
    });

    it("closes the modal after successful delete", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /remove/i }));
        await waitFor(() => screen.getByRole("dialog"));

        const dialog = screen.getByRole("dialog");
        await userEvent.click(within(dialog).getByRole("button", { name: /remove/i }));

        await waitFor(() => {
            expect(screen.queryByText(/are you sure/i)).not.toBeInTheDocument();
        });
    });

    it("does not call removeItemFromList when Cancel is clicked", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /remove/i }));
        await waitFor(() => screen.getByRole("button", { name: /cancel/i }));
        await userEvent.click(screen.getByRole("button", { name: /cancel/i }));
        expect(removeItemFromList).not.toHaveBeenCalled();
    });
});