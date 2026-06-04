import { axe, render, screen, within } from '../test-utils';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';
import AppContext, { LibraryContext } from '../context/AppContext';
import * as LibraryActions from '../context/library/LibraryActions';

const mockLibraryState = {
    items: [],
    details: {},
    error: null,
    libraryHref: null,
    addItemHref: null,
};

const mockAppState = {
    app: {
        libraryHref: null,
    },
};

vi.mock('@/components/createSaleModal/CreateSaleModal.tsx', () => ({
    CreateSaleModal: ({ opened }: { opened: boolean }) =>
        opened ? (
            <div role="dialog" aria-label="Create Sale Modal">
                Create Sale Modal
            </div>
        ) : null,
}));

vi.mock('@/components/addItemModal/AddItemModal.tsx', () => ({
    AddItemModal: ({
                       opened,
                       onItemAdded,
                   }: {
        opened: boolean;
        onItemAdded: () => void;
    }) =>
        opened ? (
            <div role="dialog" aria-label="Add Item Modal">
                Add Item Modal
                <button onClick={onItemAdded}>Finish Add Item</button>
            </div>
        ) : null,
}));

vi.mock('@/components/accordion/ItemAccordion.js', () => ({
    ItemAccordion: () => <div>Accordion</div>,
}));

vi.mock('../context/library/LibraryActions', () => ({
    getLibrary: vi.fn(),
}));

beforeEach(() => {
    vi.clearAllMocks();
});

function renderWithProviders({
                                 libraryState = mockLibraryState,
                                 appState = mockAppState,
                                 dispatch = vi.fn(),
                             } = {}) {
    render(
        <AppContext.Provider value={{ state: appState, dispatch: vi.fn() }}>
            <LibraryContext.Provider
                value={{
                    state: libraryState,
                    dispatch,
                }}
            >
                <MyLibraryPage />
            </LibraryContext.Provider>
        </AppContext.Provider>
    );

    return { dispatch };
}

describe('MyLibraryPage', () => {
    axe([
        <AppContext.Provider
            key="app"
            value={{ state: mockAppState, dispatch: vi.fn() }}
        >
            <LibraryContext.Provider
                key="library"
                value={{ state: mockLibraryState, dispatch: vi.fn() }}
            >
                <MyLibraryPage />
            </LibraryContext.Provider>
        </AppContext.Provider>,
    ]);

    it('renders correctly', () => {
        renderWithProviders();
    });

    it('renders the page title', () => {
        renderWithProviders();

        expect(
            screen.getByRole('heading', { name: /my library/i })
        ).toBeInTheDocument();
    });

    it('renders the page subtitle', () => {
        renderWithProviders();

        expect(
            screen.getByText(/check out your items/i)
        ).toBeInTheDocument();
    });

    it('renders buttons', () => {
        renderWithProviders();

        expect(screen.getByRole('button', { name: /add item/i })).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /create a sale/i })).toBeInTheDocument();
    });

    it('opens add item modal', async () => {
        const user = userEvent.setup();

        renderWithProviders();

        await user.click(screen.getByRole('button', { name: /add item/i }));

        expect(
            await within(document.body).findByRole('dialog', { name: /add item modal/i })
        ).toBeInTheDocument();
    });

    it('opens create sale modal', async () => {
        const user = userEvent.setup();

        renderWithProviders();

        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        expect(
            await within(document.body).findByRole('dialog', { name: /create sale modal/i })
        ).toBeInTheDocument();
    });

    it('calls getLibrary when libraryHref exists', async () => {
        const dispatch = vi.fn();

        renderWithProviders({
            appState: {
                app: {
                    libraryHref: 'http://localhost:8081/my-library',
                },
            },
            dispatch,
        });

        await waitFor(() => {
            expect(LibraryActions.getLibrary).toHaveBeenCalledWith(
                dispatch,
                'http://localhost:8081/my-library'
            );
        });
    });

    it('does not call getLibrary when libraryHref is null', async () => {
        renderWithProviders();

        await waitFor(() => {
            expect(LibraryActions.getLibrary).not.toHaveBeenCalled();
        });
    });

    it('opens add item and create sale modals independently', async () => {
        const user = userEvent.setup();

        renderWithProviders();

        await user.click(screen.getByRole('button', { name: /add item/i }));
        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        expect(
            within(document.body).getByRole('dialog', { name: /add item modal/i })
        ).toBeInTheDocument();

        expect(
            within(document.body).getByRole('dialog', { name: /create sale modal/i })
        ).toBeInTheDocument();
    });

    it('refreshes library after item is added', async () => {
        const user = userEvent.setup();
        const dispatch = vi.fn();

        renderWithProviders({
            appState: {
                app: {
                    libraryHref: 'http://localhost:8081/my-library',
                },
            },
            dispatch,
        });

        await user.click(screen.getByRole('button', { name: /add item/i }));

        vi.mocked(LibraryActions.getLibrary).mockClear();

        await user.click(screen.getByRole('button', { name: /finish add item/i }));

        expect(LibraryActions.getLibrary).toHaveBeenCalledWith(
            dispatch,
            'http://localhost:8081/my-library'
        );
    });
});