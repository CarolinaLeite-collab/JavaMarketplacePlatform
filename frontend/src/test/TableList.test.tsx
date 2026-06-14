import {render, screen, within} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {describe, expect, it, vi} from 'vitest';
import {MantineProvider} from '@mantine/core';
import {TableList} from "../components/lists/tablelist/TableList.tsx";
import AppContext from '../context/AppContext';
import { MemoryRouter } from 'react-router-dom';

vi.mock('../components/sharelistmodal/ShareListModal', () => ({
    ShareListModal: ({ listName, visibility }: { listName: string; visibility: string }) => (
        <button data-testid={`share-${listName}`} data-visibility={visibility}>
            Share
        </button>
    ),
}));

vi.mock('../components/deletelistmodal/DeleteListModal', () => ({
    DeleteListModal: ({ listName }: { listName: string }) => (
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
    { listId: '1', name: 'Fantasy Favourites', genre: 'Fantasy', visibility: 'public' as const, sharedUntil: 12, links: [{ rel: 'delete', href: '/my-lists/1' }, { rel: 'make-private', href: '/my-lists/1/visibility' }], itemIds: [] },
    { listId: '2', name: 'Secret Reads', genre: 'Mystery', visibility: 'private' as const, sharedUntil: null, links: [{ rel: 'delete', href: '/my-lists/2' }, { rel: 'make-public', href: '/my-lists/2/visibility' }], itemIds: [] },
    { listId: '3', name: 'Sci-Fi Shelf', genre: 'Science Fiction', visibility: 'public' as const, sharedUntil: 3, links: [{ rel: 'delete', href: '/my-lists/3' }, { rel: 'make-private', href: '/my-lists/3/visibility' }], itemIds: [] },
    { listId: '4', name: 'Classic Literature', genre: 'Classic', visibility: 'private' as const, sharedUntil: null, links: [{ rel: 'delete', href: '/my-lists/4' }, { rel: 'make-public', href: '/my-lists/4/visibility' }], itemIds: [] },
    { listId: '5', name: 'Lista fixe', genre: 'Classic', visibility: 'private' as const, sharedUntil: null, links: [{ rel: 'delete', href: '/my-lists/5' }, { rel: 'make-public', href: '/my-lists/5/visibility' }], itemIds: [] },
];

const mockDispatch = vi.fn();

const renderWithContext = (ui: React.ReactElement) =>
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

const getCells = (row: HTMLElement) =>
    within(row).getAllByRole('cell').map((c) => c.textContent ?? '');

describe('TableList – rendering', () => {
    it('renders the search input', () => {
        renderWithContext(<TableList />);
        expect(screen.getByPlaceholderText(/search by name, genre or visibility/i)).toBeInTheDocument();
    });

    it('renders all six column headers', () => {
        renderWithContext(<TableList />);
        const headers = ['List Name', 'Genre', 'Shared Until', 'Visibility', 'Add Items', 'Delete'];
        headers.forEach((h) =>
            expect(screen.getByRole('columnheader', { name: new RegExp(h, 'i') })).toBeInTheDocument()
        );
    });

    it('renders four buttons per data row (Link + Share + Add + Delete)', () => {
        renderWithContext(<TableList />);
        getDataRows().forEach((row) => {
            expect(within(row).getAllByRole('button')).toHaveLength(4);
        });
    });

    it('passes correct visibility prop to ShareListModal for every row', () => {
        renderWithContext(<TableList />);
        const shareButtons = screen.queryAllByTestId(/^share-/);
        shareButtons.forEach((btn) => {
            expect(['public', 'private']).toContain(btn.getAttribute('data-visibility'));
        });
    });
});

describe('TableList – empty state', () => {
    it('shows "Nothing found" when search matches no rows', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        await user.type(
            screen.getByPlaceholderText(/search by name, genre or visibility/i),
            'zzzzzzz_no_match'
        );
        expect(screen.getByText(/nothing found/i)).toBeInTheDocument();
        expect(getDataRows()).toHaveLength(1);
    });
});

describe('TableList – search filtering', () => {
    it('filters rows by name (case-insensitive)', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        await user.type(
            screen.getByPlaceholderText(/search by name, genre or visibility/i),
            'fantasy'
        );
        const rows = getDataRows();
        expect(rows).toHaveLength(1);
        expect(getCells(rows[0])[0]).toMatch(/fantasy/i);
    });

    it('filters rows by genre', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        await user.type(
            screen.getByPlaceholderText(/search by name, genre or visibility/i),
            'mystery'
        );
        const rows = getDataRows();
        expect(rows).toHaveLength(1);
        expect(getCells(rows[0])[1]).toMatch(/mystery/i);
    });

    it('filters rows by visibility — public shows fewer rows than total', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        const totalRows = getDataRows().length;
        await user.type(
            screen.getByPlaceholderText(/search by name, genre or visibility/i),
            'public'
        );
        const filteredRows = getDataRows();
        expect(filteredRows.length).toBeGreaterThan(0);
        expect(filteredRows.length).toBeLessThan(totalRows);
    });

    it('filters rows by visibility — private shows fewer rows than total', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        const totalRows = getDataRows().length;
        await user.type(
            screen.getByPlaceholderText(/search by name, genre or visibility/i),
            'private'
        );
        const filteredRows = getDataRows();
        expect(filteredRows.length).toBeGreaterThan(0);
        expect(filteredRows.length).toBeLessThan(totalRows);
    });

    it('restores all rows when search is cleared', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        const input = screen.getByPlaceholderText(/search by name, genre or visibility/i);
        const initialCount = getDataRows().length;
        await user.type(input, 'fantasy');
        expect(getDataRows()).toHaveLength(1);
        await user.clear(input);
        expect(getDataRows()).toHaveLength(initialCount);
    });
});

describe('TableList – sorting', () => {
    const getColumnValues = (colIndex: number) =>
        getDataRows().map((r) => getCells(r)[colIndex]);

    it('sorts by List Name ascending on first click', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        await user.click(screen.getByRole('button', { name: /list name/i }));
        const names = getColumnValues(0);
        expect(names).toEqual([...names].sort((a, b) => a.localeCompare(b)));
    });

    it('sorts by List Name descending on second click', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        const btn = screen.getByRole('button', { name: /list name/i });
        await user.click(btn);
        await user.click(btn);
        const names = getColumnValues(0);
        expect(names).toEqual([...names].sort((a, b) => b.localeCompare(a)));
    });

    it('sorts by Genre ascending on first click', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        await user.click(screen.getByRole('button', { name: /genre/i }));
        const genres = getColumnValues(1);
        expect(genres).toEqual([...genres].sort((a, b) => a.localeCompare(b)));
    });

    it('resets sort direction when switching sort column', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        const nameBtn = screen.getByRole('button', { name: /list name/i });
        await user.click(nameBtn);
        await user.click(nameBtn);
        await user.click(screen.getByRole('button', { name: /genre/i }));
        const genres = getColumnValues(1);
        expect(genres).toEqual([...genres].sort((a, b) => a.localeCompare(b)));
    });
});

describe('TableList – Shared Until display', () => {
    it('shows "X days left" (plural) when sharedUntil > 1', () => {
        renderWithContext(<TableList />);
        expect(screen.getByText('12 days left')).toBeInTheDocument();
        expect(screen.getByText('3 days left')).toBeInTheDocument();
    });

    it('shows a dash for rows with no sharing expiry', () => {
        renderWithContext(<TableList />);
        expect(screen.getAllByText('—').length).toBeGreaterThan(0);
    });
});

describe('TableList – combined search + sort', () => {
    it('applies search on top of an active sort', async () => {
        const user = userEvent.setup();
        renderWithContext(<TableList />);
        await user.click(screen.getByRole('button', { name: /list name/i }));
        await user.type(
            screen.getByPlaceholderText(/search by name, genre or visibility/i),
            'classic'
        );
        const rows = getDataRows();
        rows.forEach((row) => expect(row.textContent?.toLowerCase()).toContain('classic'));
        const names = rows.map((r) => getCells(r)[0]);
        expect(names).toEqual([...names].sort((a, b) => a.localeCompare(b)));
    });
});