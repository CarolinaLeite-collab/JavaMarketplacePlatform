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

const mockItemsForSort = [
    { itemId: 'ITEM-002', title: 'Zebra', authorName: 'Anna Author', publicationType: 'Book', identifier: '111', picture: null, links: [] },
    { itemId: 'ITEM-001', title: 'Apple', authorName: 'Zack Writer', publicationType: 'Magazine', identifier: '999', picture: null, links: [] },
];

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
    ItemAccordion: ({ items }: { items: { itemId: string; title: string }[] }) => (
        <div data-testid="accordion">
            {items.map((item) => (
                <span key={item.itemId}>{item.title}</span>
            ))}
        </div>
    ),
}));

vi.mock('../context/library/LibraryActions', () => ({
    getLibrary: vi.fn(),
}));

beforeEach(() => {
    vi.clearAllMocks();
    Element.prototype.scrollIntoView = vi.fn();
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

    describe('sorting', () => {
        it('renders the sort select', () => {
            renderWithProviders();

            expect(screen.getByPlaceholderText(/sort by/i)).toBeInTheDocument();
        });

        it('renders items in original order when no sort is selected', () => {
            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            const accordion = screen.getByTestId('accordion');
            const titles = within(accordion).getAllByText(/Zebra|Apple/i).map((el) => el.textContent);

            expect(titles).toEqual(['Zebra', 'Apple']);
        });

        it('does not show the direction toggle when no sort is selected', () => {
            renderWithProviders();

            expect(screen.queryByRole('button', { name: /sort ascending|sort descending/i })).not.toBeInTheDocument();
        });

        it('sorts items by title when "Title" is selected', async () => {
            const user = userEvent.setup();

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{Enter}');

            expect(select).toHaveValue('Title');

            const accordion = screen.getByTestId('accordion');
            const titles = within(accordion).getAllByText(/Zebra|Apple/i).map((el) => el.textContent);

            expect(titles).toEqual(['Apple', 'Zebra']);
        });

        it('sorts items by author when "Author" is selected', async () => {
            const user = userEvent.setup();

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{ArrowDown}{Enter}');

            expect(select).toHaveValue('Author');

            const accordion = screen.getByTestId('accordion');
            const titles = within(accordion).getAllByText(/Zebra|Apple/i).map((el) => el.textContent);

            // Anna Author (Zebra) comes before Zack Writer (Apple)
            expect(titles).toEqual(['Zebra', 'Apple']);
        });

        it('sorts items by type when "Type" is selected', async () => {
            const user = userEvent.setup();

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{ArrowDown}{ArrowDown}{Enter}');

            expect(select).toHaveValue('Type');

            const accordion = screen.getByTestId('accordion');
            const titles = within(accordion).getAllByText(/Zebra|Apple/i).map((el) => el.textContent);

            // Book (Zebra) comes before Magazine (Apple)
            expect(titles).toEqual(['Zebra', 'Apple']);
        });

        it('sorts items by identifier when "ISBN/ISSN" is selected', async () => {
            const user = userEvent.setup();

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{ArrowDown}{ArrowDown}{ArrowDown}{Enter}');

            expect(select).toHaveValue('ISBN/ISSN');

            const accordion = screen.getByTestId('accordion');
            const titles = within(accordion).getAllByText(/Zebra|Apple/i).map((el) => el.textContent);

            // identifier "111" (Zebra) sorts before "999" (Apple)
            expect(titles).toEqual(['Zebra', 'Apple']);
        });

        it('reverses order when the direction toggle is clicked', async () => {
            const user = userEvent.setup();

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{Enter}'); // Title, ascending: Apple, Zebra

            let accordion = screen.getByTestId('accordion');
            let titles = within(accordion).getAllByText(/Zebra|Apple/i).map((el) => el.textContent);
            expect(titles).toEqual(['Apple', 'Zebra']);

            const toggle = screen.getByRole('button', { name: /sort ascending/i });
            await user.click(toggle);

            accordion = screen.getByTestId('accordion');
            titles = within(accordion).getAllByText(/Zebra|Apple/i).map((el) => el.textContent);
            expect(titles).toEqual(['Zebra', 'Apple']);
        });

        it('shows the direction toggle once a sort criterion is selected', async () => {
            const user = userEvent.setup();

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{Enter}');

            expect(screen.getByRole('button', { name: /sort ascending|sort descending/i })).toBeInTheDocument();
        });

        it('reverts to original order when sort selection is cleared', async () => {
            const user = userEvent.setup();

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{Enter}');

            expect(select).toHaveValue('Title');

            const clearButton = select.parentElement?.querySelector('button');
            expect(clearButton).toBeTruthy();
            await user.click(clearButton as HTMLButtonElement);

            const accordion = screen.getByTestId('accordion');
            const titles = within(accordion).getAllByText(/Zebra|Apple/i).map((el) => el.textContent);

            expect(titles).toEqual(['Zebra', 'Apple']);
        });

        it('does not crash when items are missing sort fields', async () => {
            const user = userEvent.setup();
            const incompleteItems = [
                { itemId: 'ITEM-003', title: 'No Data', authorName: null, publicationType: null, identifier: null, picture: null, links: [] },
            ];

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: incompleteItems },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{ArrowDown}{Enter}');

            expect(screen.getByText('No Data')).toBeInTheDocument();
        });

        it('shows a warning when no items have data for the selected sort field', async () => {
            const user = userEvent.setup();
            const itemsWithoutBackendFields = [
                { itemId: 'ITEM-001', title: 'Zebra', authorName: null, publicationType: null, identifier: null, picture: null, links: [] },
                { itemId: 'ITEM-002', title: 'Apple', authorName: null, publicationType: null, identifier: null, picture: null, links: [] },
            ];

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: itemsWithoutBackendFields },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{ArrowDown}{Enter}'); // Author

            expect(screen.getByText(/no data available to sort by this field yet/i)).toBeInTheDocument();
        });

        it('does not show a warning when sorting by title (always available)', async () => {
            const user = userEvent.setup();

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{Enter}'); // Title

            expect(screen.queryByText(/no data available to sort by this field yet/i)).not.toBeInTheDocument();
        });

        it('does not show a warning when at least one item has the sort field populated', async () => {
            const user = userEvent.setup();
            const mixedItems = [
                { itemId: 'ITEM-001', title: 'Zebra', authorName: 'Anna Author', publicationType: null, identifier: null, picture: null, links: [] },
                { itemId: 'ITEM-002', title: 'Apple', authorName: null, publicationType: null, identifier: null, picture: null, links: [] },
            ];

            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mixedItems },
            });

            const select = screen.getByPlaceholderText(/sort by/i);
            await user.click(select);
            await user.keyboard('{ArrowDown}{ArrowDown}{Enter}'); // Author

            expect(screen.queryByText(/no data available to sort by this field yet/i)).not.toBeInTheDocument();
        });

        it('does not show a warning when no sort is selected', () => {
            renderWithProviders({
                libraryState: { ...mockLibraryState, items: mockItemsForSort },
            });

            expect(screen.queryByText(/no data available to sort by this field yet/i)).not.toBeInTheDocument();
        });
    });
});