import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { MantineProvider } from "@mantine/core";
import { DeleteListModal } from "../../components/lists/DeleteListModal";
import AppContext from "../../context/AppContext";
import { deleteList } from "../../context/lists/ListsActions";

// --- Mocks ---

vi.mock("../../context/lists/ListsActions", () => ({
    deleteList: vi.fn().mockResolvedValue(undefined),
}));

// --- Helpers ---

const mockLinks = [{ rel: "delete", href: "http://localhost:8081/my-lists/1" }];
const mockDispatch = vi.fn();

const renderModal = (props = {}) =>
    render(
        <MantineProvider>
            <AppContext.Provider value={{ dispatch: mockDispatch, state: {} } as any}>
                <DeleteListModal
                    listName="My Reading List"
                    links={mockLinks}
                    myListsHref="http://localhost:8081/my-lists"
                    {...props}
                />
            </AppContext.Provider>
        </MantineProvider>
    );

// --- Tests ---

describe("DeleteListModal", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    // --- Unit: rendering ---

    it("renders the Delete trigger button", () => {
        renderModal();
        expect(screen.getByRole("button", { name: /delete/i })).toBeInTheDocument();
    });

    it("does not show the modal on initial render", () => {
        renderModal();
        expect(screen.queryByText(/are you sure/i)).not.toBeInTheDocument();
    });

    // --- Unit: open modal ---

    it("opens the modal when Delete is clicked", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /delete/i }));
        await waitFor(() => {
            expect(screen.getByText(/are you sure you want to delete this list/i)).toBeInTheDocument();
        });
    });

    it("shows the list name in the modal title", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /delete/i }));
        await waitFor(() => {
            expect(screen.getByText(/delete "my reading list"/i)).toBeInTheDocument();
        });
    });

    it("renders Confirm button inside the modal", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /delete/i }));
        await waitFor(() => {
            expect(screen.getByRole("button", { name: /confirm/i })).toBeInTheDocument();
        });
    });

    // --- Unit: close modal ---

    it("closes the modal when the X button is clicked", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /delete/i }));
        await waitFor(() => screen.getByRole("button", { name: /close/i }));
        await userEvent.click(screen.getByRole("button", { name: /close/i }));
        await waitFor(() => {
            expect(screen.queryByText(/are you sure/i)).not.toBeInTheDocument();
        });
    });

    // --- Integration: delete ---

    it("calls deleteList with correct args when Confirm is clicked", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /delete/i }));
        await waitFor(() => screen.getByRole("button", { name: /confirm/i }));
        await userEvent.click(screen.getByRole("button", { name: /confirm/i }));
        await waitFor(() => {
            expect(deleteList).toHaveBeenCalledWith(
                mockDispatch,
                mockLinks,
                "http://localhost:8081/my-lists"
            );
        });
    });

    it("closes the modal after successful delete", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /delete/i }));
        await waitFor(() => screen.getByRole("button", { name: /confirm/i }));
        await userEvent.click(screen.getByRole("button", { name: /confirm/i }));
        await waitFor(() => {
            expect(screen.queryByText(/are you sure/i)).not.toBeInTheDocument();
        });
    });

    it("does not call deleteList when modal is closed without confirming", async () => {
        renderModal();
        await userEvent.click(screen.getByRole("button", { name: /delete/i }));
        await waitFor(() => screen.getByRole("button", { name: /close/i }));
        await userEvent.click(screen.getByRole("button", { name: /close/i }));
        expect(deleteList).not.toHaveBeenCalled();
    });

    it("passes null myListsHref correctly to deleteList", async () => {
        renderModal({ myListsHref: null });
        await userEvent.click(screen.getByRole("button", { name: /delete/i }));
        await waitFor(() => screen.getByRole("button", { name: /confirm/i }));
        await userEvent.click(screen.getByRole("button", { name: /confirm/i }));
        await waitFor(() => {
            expect(deleteList).toHaveBeenCalledWith(mockDispatch, mockLinks, null);
        });
    });
});