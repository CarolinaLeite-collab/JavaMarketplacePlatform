import {render, screen, waitFor, within} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {describe, expect, it, vi} from 'vitest';
import {MantineProvider} from '@mantine/core';
import {NewListModal} from "../components/lists/newlistmodal/NewListModal.tsx";
import MyListsPage from "../pages/Lists/MyListsPage.tsx";

vi.mock('@/components/lists/TableList.tsx', () => ({
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

const renderWithMantine = (ui: React.ReactElement) =>
    render(<MantineProvider>{ui}</MantineProvider>);

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
        const expectedGenres = ['Fantasy', 'Mystery', 'Science Fiction', 'Classic', 'Romance', 'Thriller'];
        for (const genre of expectedGenres) {
            expect(await screen.findByRole('option', { name: genre })).toBeInTheDocument();
        }
    });

    it('can select a genre from the dropdown', async () => {
        const user = userEvent.setup();
        renderWithMantine(<NewListModal />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        await screen.findByRole('dialog');
        await user.click(screen.getByRole('combobox', { name: /genre/i }));
        await user.click(await screen.findByRole('option', { name: 'Fantasy' }));
        const combobox = screen.getByRole('combobox', { name: /genre/i }) as HTMLInputElement;
        expect(combobox.value).toBe('Fantasy');
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
        renderWithMantine(<MyListsPage />);
        expect(screen.getByRole('heading', { name: /my lists/i })).toBeInTheDocument();
        expect(screen.getByText(/check out your lists/i)).toBeInTheDocument();
    });

    it('renders the TableList component', () => {
        renderWithMantine(<MyListsPage />);
        expect(screen.getByTestId('table-list')).toBeInTheDocument();
    });

    it('renders the NEW LIST button via the Affix', () => {
        renderWithMantine(<MyListsPage />);
        expect(screen.getByRole('button', { name: /new list/i })).toBeInTheDocument();
    });

    it('opens the modal from the page-level NEW LIST button', async () => {
        const user = userEvent.setup();
        renderWithMantine(<MyListsPage />);
        await user.click(screen.getByRole('button', { name: /new list/i }));
        expect(await screen.findByRole('dialog')).toBeInTheDocument();
    });
});