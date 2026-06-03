import { render, screen, within, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi } from 'vitest';
import { MantineProvider } from '@mantine/core';
import { DeleteListModal } from '../components/deletelistmodal/DeleteListModal';
import AppContext from '../context/AppContext';

vi.mock('../context/lists/ListsActions', () => ({
    deleteList: vi.fn(),
}));

const mockDispatch = vi.fn();
const mockLinks = [
    { rel: 'delete', href: 'http://localhost:8081/my-lists/LOI-123' },
    { rel: 'self', href: 'http://localhost:8081/my-lists/LOI-123' },
];

const renderWithContext = (ui: React.ReactElement) =>
    render(
        <AppContext.Provider value={{
            state: { lists: { lists: [], genres: [], error: null, loading: false } },
            dispatch: mockDispatch
        }}>
            <MantineProvider>{ui}</MantineProvider>
        </AppContext.Provider>
    );

describe('DeleteListModal – trigger button', () => {
    it('renders the delete action icon button', () => {
        renderWithContext(<DeleteListModal listName="My List" links={mockLinks} />);
        expect(screen.getByRole('button')).toBeInTheDocument();
    });

    it('does not show the modal on initial render', () => {
        renderWithContext(<DeleteListModal listName="My List" links={mockLinks} />);
        expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    });
});

describe('DeleteListModal – modal open', () => {
    it('opens the modal when the delete button is clicked', async () => {
        const user = userEvent.setup();
        renderWithContext(<DeleteListModal listName="My List" links={mockLinks} />);
        await user.click(screen.getByRole('button'));
        expect(await screen.findByRole('dialog')).toBeInTheDocument();
    });

    it('shows the list name in the modal title', async () => {
        const user = userEvent.setup();
        renderWithContext(<DeleteListModal listName="Secret Reads" links={mockLinks} />);
        await user.click(screen.getByRole('button'));
        expect(await screen.findByText(/Delete "Secret Reads"/)).toBeInTheDocument();
    });

    it('shows the confirmation message with the list name', async () => {
        const user = userEvent.setup();
        renderWithContext(<DeleteListModal listName="My List" links={mockLinks} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        expect(within(dialog).getByText(/are you sure you want to delete/i)).toBeInTheDocument();
    });

    it('renders Yes and No buttons inside the modal', async () => {
        const user = userEvent.setup();
        renderWithContext(<DeleteListModal listName="My List" links={mockLinks} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        expect(within(dialog).getByRole('button', { name: /yes/i })).toBeInTheDocument();
        expect(within(dialog).getByRole('button', { name: /no/i })).toBeInTheDocument();
    });
});

describe('DeleteListModal – modal close', () => {
    it('closes when "No" is clicked', async () => {
        const user = userEvent.setup();
        renderWithContext(<DeleteListModal listName="My List" links={mockLinks} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        await user.click(within(dialog).getByRole('button', { name: /no/i }));
        await waitFor(
            () => expect(screen.queryByRole('dialog')).not.toBeInTheDocument(),
            { timeout: 500 }
        );
    });

    it('closes when the X button is clicked', async () => {
        const user = userEvent.setup();
        renderWithContext(<DeleteListModal listName="My List" links={mockLinks} />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');
        const closeBtn = document.querySelector<HTMLElement>('.mantine-Modal-close')!;
        await user.click(closeBtn);
        await waitFor(
            () => expect(screen.queryByRole('dialog')).not.toBeInTheDocument(),
            { timeout: 500 }
        );
    });
});

describe('DeleteListModal – delete action', () => {
    it('calls deleteList with dispatch and links when Yes is clicked', async () => {
        const { deleteList } = await import('../context/lists/ListsActions');
        const user = userEvent.setup();
        renderWithContext(<DeleteListModal listName="My List" links={mockLinks} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        await user.click(within(dialog).getByRole('button', { name: /yes/i }));
        expect(deleteList).toHaveBeenCalledWith(mockDispatch, mockLinks);
    });

    it('closes the modal after confirming delete', async () => {
        const user = userEvent.setup();
        renderWithContext(<DeleteListModal listName="My List" links={mockLinks} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        await user.click(within(dialog).getByRole('button', { name: /yes/i }));
        await waitFor(
            () => expect(screen.queryByRole('dialog')).not.toBeInTheDocument(),
            { timeout: 500 }
        );
    });
});