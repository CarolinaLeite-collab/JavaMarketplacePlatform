import { axe, render, screen, within } from '@/test-utils';
import { vi, beforeEach } from 'vitest';
import { waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';
import { LibraryContext } from '../context/AppContext';
import * as LibraryActions from '../context/library/LibraryActions';

const mockState = {
    items: [],
    details: {},
    error: null,
    libraryHref: null,
    addItemHref: null,
    sales: {
        libraryItems: [],
        error: null,
        successMessage: null
    }
};

vi.mock('../components/createSaleModal/CreateSaleModal', () => ({
    CreateSaleModal: ({ opened }) =>
        opened ? <div role="dialog">Create Sale Modal</div> : null,
}));

vi.mock('../components/addItemModal/AddItemModal', () => ({
    AddItemModal: ({ opened }) =>
        opened ? <div role="dialog">Add Item Modal</div> : null,
}));

vi.mock('../components/accordion/ItemAccordion', () => ({
    ItemAccordion: () => <div>Accordion</div>,
}));

vi.mock('../context/library/LibraryActions', () => ({
    getLibrary: vi.fn(),
    getLibraryOptions: vi.fn(),
}));

beforeEach(() => {
    vi.clearAllMocks();
});

function renderWithLibraryProvider(state = mockState) {
    return render(
        <LibraryContext.Provider
            value={{
                state,
                dispatch: vi.fn(),
            }}
        >
            <MyLibraryPage />
        </LibraryContext.Provider>
    );
}

describe('MyLibraryPage', () => {

    axe([
        <LibraryContext.Provider
            key="1"
            value={{ state: mockState, dispatch: vi.fn() }}
        >
            <MyLibraryPage />
        </LibraryContext.Provider>
    ]);

    it('renders correctly', () => {
        renderWithLibraryProvider();
    });

    it('renders the page title', () => {
        renderWithLibraryProvider();

        expect(
            screen.getByRole('heading', { name: /my library/i })
        ).toBeInTheDocument();
    });

    it('renders the page subtitle', () => {
        renderWithLibraryProvider();

        expect(
            screen.getByText(/check out your items/i)
        ).toBeInTheDocument();
    });

    it('renders buttons', () => {
        renderWithLibraryProvider();

        expect(screen.getByRole('button', { name: /add item/i })).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /create a sale/i })).toBeInTheDocument();
    });

    it('opens add item modal', async () => {
        const user = userEvent.setup();

        renderWithLibraryProvider();

        await user.click(screen.getByRole('button', { name: /add item/i }));

        expect(
            await within(document.body).findByRole('dialog')
        ).toBeInTheDocument();
    });

    it('opens create sale modal', async () => {
        const user = userEvent.setup();

        renderWithLibraryProvider();

        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        expect(
            await within(document.body).findByRole('dialog')
        ).toBeInTheDocument();
    });

    it('calls getLibraryOptions on mount', async () => {
        renderWithLibraryProvider();

        await waitFor(() => {
            expect(LibraryActions.getLibraryOptions).toHaveBeenCalled();
        });
    });

    it('calls getLibrary when libraryHref exists', async () => {
        const state = {
            ...mockState,
            libraryHref: 'http://localhost:8081/my-library'
        };

        renderWithLibraryProvider(state);

        await waitFor(() => {
            expect(LibraryActions.getLibrary).toHaveBeenCalledWith(
                expect.any(Function),
                'http://localhost:8081/my-library'
            );
        });
    });

    it('does NOT call getLibrary when libraryHref is null', async () => {
        renderWithLibraryProvider();

        await waitFor(() => {
            expect(LibraryActions.getLibrary).not.toHaveBeenCalled();
        });
    });

    it('opens and shows modals independently', async () => {
        const user = userEvent.setup();

        renderWithLibraryProvider();

        await user.click(screen.getByRole('button', { name: /add item/i }));
        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        expect(
            within(document.body).getAllByRole('dialog').length
        ).toBeGreaterThan(0);
    });
});