import {render, screen, within} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {describe, expect, it, vi} from 'vitest';
import {MantineProvider} from '@mantine/core';
import {TableList} from "../components/lists/tablelist/TableList.tsx";
import AppContext from '../context/AppContext';
import { MemoryRouter } from 'react-router-dom';

vi.mock('../components/sharelistmodal/ShareListModal', () => ({
    ShareListModal: ({ listName, visibility }) => (
        <button data-testid={`share-${listName}`} data-visibility={visibility}>
            Share
        </button>
    ),
}));

vi.mock('../components/deletelistmodal/DeleteListModal', () => ({
    DeleteListModal: ({ listName }) => (
        <button data-testid={`delete-${listName}`}>Delete</button>
    ),
}));

vi.mock('../context/lists/ListsActions', () => ({
    getMyLists: vi.fn(),
    deleteList: vi.fn(),
    getListsOptions: vi.fn(),
    addItemToList: vi.fn(),
}));

const mockLists = [
    { listId: '1', name: 'Fantasy Favourites', genre: 'Fantasy', isPrivate: false, sharedUntil: 12, links: [{ rel: 'delete', href: '/my-lists/1' }, { rel: 'make-private', href: '/my-lists/1/visibility' }], itemsId: [] },
    { listId: '2', name: 'Secret Reads', genre: 'Mystery', isPrivate: true, sharedUntil: null, links: [{ rel: 'delete', href: '/my-lists/2' }, { rel: 'make-public', href: '/my-lists/2/visibility' }], itemsId: [] },
    { listId: '3', name: 'Sci-Fi Shelf', genre: 'Science Fiction', isPrivate: false, sharedUntil: 3, links: [{ rel: 'delete', href: '/my-lists/3' }, { rel: 'make-private', href: '/my-lists/3/visibility' }], itemsId: [] },
    { listId: '4', name: 'Classic Literature', genre: 'Classic', isPrivate: true, sharedUntil: null, links: [{ rel: 'delete', href: '/my-lists/4' }, { rel: 'make-public', href: '/my-lists/4/visibility' }], itemsId: [] },
];

const mockDispatch = vi.fn();

const renderWithContext = (ui) =>
    render(
        <MemoryRouter>
            <AppContext.Provider
                value={{
                    state: {
                        app: {
                            myListsHref: 'http://localhost:8081/my-lists',
                            libraryHref: 'http://localhost:8081/items/my-library',
                        },
                        lists: {
                            lists: mockLists,
                            genres: [],
                            error: null,
                            loading: false
                        }
                    },
                    dispatch: mockDispatch
                }}
            >
                <MantineProvider>{ui}</MantineProvider>
            </AppContext.Provider>
        </MemoryRouter>
    );

const getDataRows = () =>
    screen.getAllByRole('row').filter((r) => within(r).queryAllByRole('columnheader').length === 0);

const getCells = (row) =>
    within(row).getAllByRole('cell').map((c) => c.textContent ?? '');

describe('TableList – rendering', () => {
    it('renders all five column headers', () => {
        renderWithContext(<TableList search="" genre={null} />);
        const headers = ['List Name', 'Genre', 'Shared Until', 'Visibility', 'Delete'];
        headers.forEach((h) =>
            expect(screen.getByRole('columnheader', { name: new RegExp(h, 'i') })).toBeInTheDocument()
        );
    });

    it('renders two buttons per data row (Link + Share + Delete)', () => {
        renderWithContext(<TableList search="" genre={null} />);
        getDataRows().forEach((row) => {
            expect(within(row).getAllByRole('button')).toHaveLength(2);
        });
    });

    it('passes correct visibility prop to ShareListModal for every row', () => {
        renderWithContext(<TableList search="" genre={null} />);
        const shareButtons = screen.queryAllByTestId(/^share-/);
        shareButtons.forEach((btn) => {
            expect(['public', 'private']).toContain(btn.getAttribute('data-visibility'));
        });
    });
});

describe('TableList – empty state', () => {
    it('shows "Nothing found" when search matches no rows', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList search="" genre={null} />);
        expect(getDataRows()).toHaveLength(mockLists.length);
        renderWithContext(<TableList search="zzzzzzz_no_match" genre={null} />);
        expect(screen.getByText(/nothing found/i)).toBeInTheDocument();
    });
});

describe('TableList – search filtering', () => {
    it('filters rows by name (case-insensitive)', () => {
        renderWithContext(<TableList search="fantasy" genre={null} />);
        const rows = getDataRows();
        expect(rows).toHaveLength(1);
        expect(getCells(rows[0])[0]).toMatch(/fantasy/i);
    });

    it('filters rows by genre', () => {
        renderWithContext(<TableList search="mystery" genre={null} />);
        const rows = getDataRows();
        expect(rows).toHaveLength(1);
        expect(getCells(rows[0])[1]).toMatch(/mystery/i);
    });

    it('filters rows by visibility — public', () => {
        renderWithContext(<TableList search="public" genre={null} />);
        const rows = getDataRows();
        expect(rows.length).toBeGreaterThan(0);
        expect(rows.length).toBeLessThan(mockLists.length);
    });

    it('filters rows by visibility — private', () => {
        renderWithContext(<TableList search="private" genre={null} />);
        const rows = getDataRows();
        expect(rows.length).toBeGreaterThan(0);
        expect(rows.length).toBeLessThan(mockLists.length);
    });
});

describe('TableList – sorting', () => {
    const getColumnValues = (colIndex) =>
        getDataRows().map((r) => getCells(r)[colIndex]);

    it('sorts by List Name ascending on first click', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList search="" genre={null} />);
        await user.click(screen.getByRole('button', { name: /list name/i }));
        const names = getColumnValues(0);
        expect(names).toEqual([...names].sort((a, b) => a.localeCompare(b)));
    });

    it('sorts by List Name descending on second click', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList search="" genre={null} />);
        const btn = screen.getByRole('button', { name: /list name/i });
        await user.click(btn);
        await user.click(btn);
        const names = getColumnValues(0);
        expect(names).toEqual([...names].sort((a, b) => b.localeCompare(a)));
    });
});

describe('TableList – Shared Until display', () => {
    it('shows "X days left" when sharedUntil > 1', () => {
        renderWithContext(<TableList search="" genre={null} />);
        expect(screen.getByText('12 days left')).toBeInTheDocument();
        expect(screen.getByText('3 days left')).toBeInTheDocument();
    });

    it('shows a dash for rows with no sharing expiry', () => {
        renderWithContext(<TableList search="" genre={null} />);
        expect(screen.getAllByText('—').length).toBeGreaterThan(0);
    });
});