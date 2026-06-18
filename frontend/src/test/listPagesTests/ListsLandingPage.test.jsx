import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { MantineProvider } from "@mantine/core";
import { MemoryRouter } from "react-router-dom";
import ListsLandingPage from "../../pages/Lists/ListsLandingPage.jsx";
import React from "react";

// --- Mocks ---

const mockNavigate = vi.fn();

vi.mock("react-router-dom", async () => {
    const actual = await vi.importActual("react-router-dom");
    return { ...actual, useNavigate: () => mockNavigate };
});

vi.mock("../../components/layout/DefaultLayout", () => ({
    DefaultLayout: ({ children, title, subtitle }) => (
        <div>
            <h1>{title}</h1>
            <p>{subtitle}</p>
            {children}
        </div>
    ),
}));

// --- Helpers ---

const renderPage = () =>
    render(
        <MantineProvider>
            <MemoryRouter>
                <ListsLandingPage />
            </MemoryRouter>
        </MantineProvider>
    );

// --- Tests ---

describe("ListsLandingPage", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    // --- Unit: rendering ---

    it("renders the page title", () => {
        renderPage();
        expect(screen.getByText("Lists")).toBeInTheDocument();
    });

    it("renders the page subtitle", () => {
        renderPage();
        expect(screen.getByText("CHOOSE AN OPTION:")).toBeInTheDocument();
    });

    it("renders the My Lists button", () => {
        renderPage();
        expect(screen.getByRole("button", { name: /my lists/i })).toBeInTheDocument();
    });

    it("renders the Public Lists button", () => {
        renderPage();
        expect(screen.getByRole("button", { name: /public lists/i })).toBeInTheDocument();
    });

    it("renders exactly two buttons", () => {
        renderPage();
        expect(screen.getAllByRole("button")).toHaveLength(2);
    });

    // --- Integration: navigation ---

    it("navigates to /lists/my-lists when My Lists is clicked", async () => {
        renderPage();
        await userEvent.click(screen.getByRole("button", { name: /my lists/i }));
        expect(mockNavigate).toHaveBeenCalledWith("/lists/my-lists");
    });

    it("navigates to /lists/public when Public Lists is clicked", async () => {
        renderPage();
        await userEvent.click(screen.getByRole("button", { name: /public lists/i }));
        expect(mockNavigate).toHaveBeenCalledWith("/lists/public");
    });

    it("does not navigate on render without any click", () => {
        renderPage();
        expect(mockNavigate).not.toHaveBeenCalled();
    });

    it("navigates to the correct route regardless of click order", async () => {
        renderPage();
        await userEvent.click(screen.getByRole("button", { name: /public lists/i }));
        await userEvent.click(screen.getByRole("button", { name: /my lists/i }));
        expect(mockNavigate).toHaveBeenNthCalledWith(1, "/lists/public");
        expect(mockNavigate).toHaveBeenNthCalledWith(2, "/lists/my-lists");
    });
});