import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi } from "vitest";
import { ItemDetailModal, ItemDTO } from "../../components/lists/ItemDetailModal";
import { MantineProvider } from "@mantine/core";

const mockItem: ItemDTO = {
    itemId: "1",
    title: "The Great Gatsby",
    authorName: "F. Scott Fitzgerald",
    publishingYear: 1925,
    condition: "GOOD",
    description: "A novel about the American dream.",
    identifier: "978-0743273565",
    publicationTypeName: "Novel",
    picture: null,
};

const renderModal = (item: ItemDTO | null, opened: boolean, onClose = vi.fn()) => {
    return render(
        <MantineProvider>
            <ItemDetailModal item={item} opened={opened} onClose={onClose} />
        </MantineProvider>
    );
};

describe("ItemDetailModal", () => {

    // --- Unit: rendering ---

    it("renders nothing when item is null", () => {
        const { container } = renderModal(null, true);
        expect(container).toBeEmptyDOMElement();
    });

    it("does not render when opened is false", () => {
        renderModal(mockItem, false);
        expect(screen.queryByText("The Great Gatsby")).not.toBeInTheDocument();
    });

    it("renders the modal title when opened", () => {
        renderModal(mockItem, true);
        expect(screen.getByText("The Great Gatsby")).toBeInTheDocument();
    });

    it("renders author, year, identifier and type", () => {
        renderModal(mockItem, true);
        expect(screen.getByText(/F. Scott Fitzgerald/)).toBeInTheDocument();
        expect(screen.getByText(/1925/)).toBeInTheDocument();
        expect(screen.getByText(/978-0743273565/)).toBeInTheDocument();
        expect(screen.getByText(/Novel/)).toBeInTheDocument();
    });

    it("renders the description", () => {
        renderModal(mockItem, true);
        expect(screen.getByText(/A novel about the American dream/)).toBeInTheDocument();
    });

    it("renders a placeholder div when picture is null", () => {
        const { container } = renderModal(mockItem, true);
        const placeholder = container.querySelector("div[style*='background']");
        expect(placeholder).toBeInTheDocument();
    });

    it("renders the cover image when picture is provided", () => {
        const itemWithPicture = { ...mockItem, picture: "https://example.com/cover.jpg" };
        renderModal(itemWithPicture, true);
        const img = screen.getByAltText("The Great Gatsby");
        expect(img).toBeInTheDocument();
        expect(img).toHaveAttribute("src", "https://example.com/cover.jpg");
    });

    // --- Unit: condition badge colors ---

    it("renders a teal badge for GOOD condition", () => {
        renderModal(mockItem, true);
        expect(screen.getByText("GOOD")).toBeInTheDocument();
    });

    it("renders a yellow badge for FAIR condition", () => {
        const fairItem = { ...mockItem, condition: "FAIR" };
        renderModal(fairItem, true);
        expect(screen.getByText("FAIR")).toBeInTheDocument();
    });

    it("renders a gray badge for unknown condition", () => {
        const unknownItem = { ...mockItem, condition: "POOR" };
        renderModal(unknownItem, true);
        expect(screen.getByText("POOR")).toBeInTheDocument();
    });

    // --- Unit: fallback values ---

    it("renders -- for missing identifier", () => {
        const noIdentifier = { ...mockItem, identifier: "" };
        renderModal(noIdentifier, true);
        expect(screen.getAllByText(/--/).length).toBeGreaterThan(0);
    });

    it("renders -- for missing publicationTypeName", () => {
        const noType = { ...mockItem, publicationTypeName: "" };
        renderModal(noType, true);
        expect(screen.getAllByText(/--/).length).toBeGreaterThan(0);
    });

    // --- Integration: interaction ---

    it("calls onClose when the modal close button is clicked", async () => {
        const onClose = vi.fn();
        renderModal(mockItem, true, onClose);
        const closeButton = screen.getByRole("button", { name: /close/i });
        await userEvent.click(closeButton);
        expect(onClose).toHaveBeenCalledTimes(1);
    });
});