import {render, screen, waitFor, within} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {describe, expect, it, vi} from 'vitest';
import {MantineProvider} from '@mantine/core';
import {NewListModal} from "../components/lists/newlistmodal/NewListModal.tsx";
import MyListsPage from "../pages/lists/MyListsPage.jsx";
import AppContext from '../context/AppContext';
import { MemoryRouter } from 'react-router-dom';

vi.mock('../context/lists/ListsActions', () => ({
    createList: vi.fn().mockResolvedValue(true),
    getGenres: vi.fn().mockImplementation((dispatch) => { }),
}));

vi.mock('@/components/lists/tablelist/TableList.tsx', () => ({
    TableList: () => <div data-testid="table-list" />,
}));

vi.mock('@/components/layout/DefaultLayout.tsx', () => ({
    DefaultLayout: ({ children, title, subtitle }: { children: React.ReactNode; title: string; subtitle: string }) => (
        <div>
            <h1>{title}</h1>
            <p>{subtitle}</p>
            <footer style={{ height: '60px' }} />
            {children}
        </div>
    ),
}));

const mockContextValue = {
    state: {
        app: {
            createListHref: 'http://localhost:8081/my-lists/',
            genresHref: 'http://localhost:8081/genres',
            myListsHref: 'http://localhost:8081/my-lists/',
        },
        lists: {
            genres: [
                { value: 'fantasy', label: 'Fantasy' },
                { value: 'mystery', label: 'Mystery' },
                { value: 'science-fiction', label: 'Science Fiction' },
                { value: 'classic', label: 'Classic' },
                { value: 'romance', label: 'Romance' },
                { value: 'thriller', label: 'Thriller' },
            ],
            error: null,
        },
    },
    dispatch: vi.fn(),
};

const renderWithMantine = (ui: React.ReactElement) =>
    render(
        <MantineProvider>
            <AppContext.Provider value={mockContextValue as any}>
                {ui}
            </AppContext.Provider>
        </MantineProvider>
    );

const renderPage = () =>
    render(
        <MantineProvider>
            <AppContext.Provider value={mockContextValue as any}>
                <MemoryRouter>
                    <MyListsPage />
                </MemoryRouter>
            </AppContext.Provider>
        </MantineProvider>
    );

describe('NewListModal – trigger button', () => {
    it('renders a "NEW LIST" button', () => {
        renderWithMantine(<NewListModal />);
        expect(screen.getByRole('button', { name: /new list/i })).toBeInTheDocument();
    });

    it('does not show the modal on initial render', () => {
        renderWithMantine(<NewListModal />);
        expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    });
});

describe('NewListModal – modal open', () => {
    it('opens the modal when the "NEW LIST" button is clicked', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        expect(await screen.findByRole('dialog')).toBeInTheDocument();
    });

    it('shows "Create New List" as the modal title', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        expect(await screen.findByText('Create New List')).toBeInTheDocument();
    });

    it('renders the List Name text input', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        await screen.findByRole('dialog');
        expect(screen.getByRole('textbox', { name: /list name/i })).toBeInTheDocument();
    });

    it('renders the Genre select', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        await screen.findByRole('dialog');
        expect(screen.getByRole('combobox', { name: /genre/i })).toBeInTheDocument();
    });

    it('renders the "Create List" button inside the modal', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        const dialog = await screen.findByRole('dialog');
        expect(within(dialog).getByRole('button', { name: /create list/i })).toBeInTheDocument();
    });
});

describe('NewListModal – form inputs', () => {
    it('List Name input starts empty', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        await screen.findByRole('dialog');
        const input = screen.getByRole('textbox', { name: /list name/i }) as HTMLInputElement;
        expect(input.value).toBe('');
    });

    it('accepts text in the List Name input', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        await screen.findByRole('dialog');
        const input = screen.getByRole('textbox', { name: /list name/i }) as HTMLInputElement;
        await user.type(input, 'My Reading List');
        expect(input.value).toBe('My Reading List');
    });

    it('Genre select contains all expected options', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        await screen.findByRole('dialog');
        await user.click(screen.getByRole('combobox', { name: /genre/i }));

        await waitFor(() => {
            const options = document.querySelectorAll('[data-combobox-option]');
            expect(options.length).toBe(6);
            const labels = Array.from(options).map(o => o.textContent?.trim());
            expect(labels).toContain('Fantasy');
            expect(labels).toContain('Science Fiction');
        });
    });

    it('can select a genre from the dropdown', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        await screen.findByRole('dialog');
        await user.click(screen.getByRole('combobox', { name: /genre/i }));

        await waitFor(() => {
            expect(document.querySelectorAll('[data-combobox-option]').length).toBeGreaterThan(0);
        });

        const options = document.querySelectorAll('[data-combobox-option]');
        const fantasy = Array.from(options).find(o => o.textContent?.trim() === 'Fantasy') as HTMLElement;
        await user.click(fantasy);

        await waitFor(() => {
            expect((screen.getByRole('combobox', { name: /genre/i }) as HTMLInputElement).value).toBe('Fantasy');
        });
    });
});

describe('NewListModal – modal close', () => {
    it('closes when "Create List" is clicked', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        const dialog = await screen.findByRole('dialog');
        await user.click(within(dialog).getByRole('button', { name: /create list/i }));
        await waitFor(
            () => expect(screen.queryByRole('dialog')).not.toBeInTheDocument(),
            { timeout: 500 }
        );
    });

    it('closes when the close (X) button is clicked', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        await screen.findByRole('dialog');
        const closeBtn = document.querySelector<HTMLElement>('.mantine-Modal-close')!;
        await user.click(closeBtn);
        await waitFor(
            () => expect(screen.queryByRole('dialog')).not.toBeInTheDocument(),
            { timeout: 500 }
        );
    });
});

describe('MyListsPage', () => {
    it('renders the page title and subtitle', () => {
        renderPage();
        expect(screen.getByRole('heading', { name: /my lists/i })).toBeInTheDocument();
        expect(screen.getByText(/check out your lists/i)).toBeInTheDocument();
    });

    it('renders the TableList component', () => {
        renderPage();
        expect(screen.getByTestId('table-list')).toBeInTheDocument();
    });

    it('renders the NEW LIST button via the Affix', () => {
        renderPage();
        expect(screen.getByRole('button', { name: /new list/i })).toBeInTheDocument();
    });

    it('opens the modal from the page-level NEW LIST button', async () => {
        const user = userEvent.setup();
        renderPage();
        await user.click(screen.getByRole('button', { name: /new list/i }));
        expect(await screen.findByRole('dialog')).toBeInTheDocument();
    });
});