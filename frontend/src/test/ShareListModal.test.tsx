import { render, screen, within, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi } from 'vitest';
import { MantineProvider } from '@mantine/core';
import { ShareListModal } from '../components/sharelistmodal/ShareListModal';
import AppContext from '../context/AppContext';

vi.mock('../context/lists/ListsActions', () => ({
    makeListPublic: vi.fn(),
    makeListPrivate: vi.fn(),
}));

const mockDispatch = vi.fn();
const mockLinksPrivate = [
    { rel: 'make-public', href: 'http://localhost:8081/my-lists/LOI-123/visibility' },
    { rel: 'self', href: 'http://localhost:8081/my-lists/LOI-123' },
];
const mockLinksPublic = [
    { rel: 'make-private', href: 'http://localhost:8081/my-lists/LOI-123/visibility' },
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

// ---------------------------------------------------------------------------
// 1. Tooltip
// ---------------------------------------------------------------------------
describe('ShareListModal – tooltip', () => {
    it('shows "Make private" tooltip when visibility is public', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="public" links={mockLinksPublic} />);
        await user.hover(screen.getByRole('button'));
        expect(await screen.findByText('Make private')).toBeInTheDocument();
    });

    it('shows "Make public" tooltip when visibility is private', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
        await user.hover(screen.getByRole('button'));
        expect(await screen.findByText('Make public')).toBeInTheDocument();
    });
});

// ---------------------------------------------------------------------------
// 2. Icon
// ---------------------------------------------------------------------------
describe('ShareListModal – icon', () => {
    it('renders the eye icon when visibility is public', () => {
        renderWithContext(<ShareListModal listName="My List" visibility="public" links={mockLinksPublic} />);
        expect(document.querySelector('.tabler-icon-eye')).toBeInTheDocument();
        expect(document.querySelector('.tabler-icon-eye-off')).not.toBeInTheDocument();
    });

    it('renders the eye-off icon when visibility is private', () => {
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
        expect(document.querySelector('.tabler-icon-eye-off')).toBeInTheDocument();
        expect(document.querySelector('.tabler-icon-eye')).not.toBeInTheDocument();
    });
});

// ---------------------------------------------------------------------------
// 3. Public list — opens confirm make private modal
// ---------------------------------------------------------------------------
describe('ShareListModal – public list click', () => {
    it('opens the confirm modal when clicking a public list button', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="public" links={mockLinksPublic} />);
        await user.click(screen.getByRole('button'));
        expect(await screen.findByRole('dialog')).toBeInTheDocument();
    });

    it('shows "Unshare" in the confirm modal title', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="public" links={mockLinksPublic} />);
        await user.click(screen.getByRole('button'));
        expect(await screen.findByText(/Unshare "My List"/)).toBeInTheDocument();
    });

    it('shows confirmation message in the confirm modal', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="public" links={mockLinksPublic} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        expect(within(dialog).getByText(/are you sure you want to make this list private/i)).toBeInTheDocument();
    });

    it('renders Yes and No buttons in the confirm modal', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="public" links={mockLinksPublic} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        expect(within(dialog).getByRole('button', { name: /yes/i })).toBeInTheDocument();
        expect(within(dialog).getByRole('button', { name: /no/i })).toBeInTheDocument();
    });

    it('closes confirm modal when No is clicked', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="public" links={mockLinksPublic} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        await user.click(within(dialog).getByRole('button', { name: /no/i }));
        await waitFor(
            () => expect(screen.queryByRole('dialog')).not.toBeInTheDocument(),
            { timeout: 500 }
        );
    });

    it('calls makeListPrivate when Yes is clicked', async () => {
        const { makeListPrivate } = await import('../context/lists/ListsActions');
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="public" links={mockLinksPublic} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        await user.click(within(dialog).getByRole('button', { name: /yes/i }));
        expect(makeListPrivate).toHaveBeenCalledWith(mockDispatch, mockLinksPublic);
    });
});

// ---------------------------------------------------------------------------
// 4. Private list — opens share modal
// ---------------------------------------------------------------------------
describe('ShareListModal – private list click', () => {
    it('opens the share modal when clicking a private list button', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
        await user.click(screen.getByRole('button'));
        expect(await screen.findByRole('dialog')).toBeInTheDocument();
    });

    it('shows the list name in the share modal title', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="Secret Reads" visibility="private" links={mockLinksPrivate} />);
        await user.click(screen.getByRole('button'));
        expect(await screen.findByText(/Share "Secret Reads"/)).toBeInTheDocument();
    });

    it('shows the descriptive text inside the share modal', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        expect(within(dialog).getByText(/how many days do you want this list to be public/i)).toBeInTheDocument();
    });

    it('renders the NumberInput with default value of 7', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');
        const input = screen.getByRole('textbox', { name: /days/i }) as HTMLInputElement;
        expect(input.value).toBe('7');
    });

    it('renders the Make Public button inside the share modal', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        expect(within(dialog).getByRole('button', { name: /make public/i })).toBeInTheDocument();
    });

    it('updates the days value when user types', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');
        const input = screen.getByRole('textbox', { name: /days/i }) as HTMLInputElement;
        await user.clear(input);
        await user.type(input, '14');
        expect(input.value).toBe('14');
    });

    it('calls makeListPublic when Make Public is clicked', async () => {
        const { makeListPublic } = await import('../context/lists/ListsActions');
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');
        await user.click(screen.getByRole('button', { name: /make public/i }));
        expect(makeListPublic).toHaveBeenCalledWith(mockDispatch, mockLinksPrivate, 7);
    });

    it('closes the share modal when Make Public is clicked', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');
        await user.click(screen.getByRole('button', { name: /make public/i }));
        await waitFor(
            () => expect(screen.queryByRole('dialog')).not.toBeInTheDocument(),
            { timeout: 500 }
        );
    });

    it('closes the share modal when X is clicked', async () => {
        const user = userEvent.setup();
        renderWithContext(<ShareListModal listName="My List" visibility="private" links={mockLinksPrivate} />);
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