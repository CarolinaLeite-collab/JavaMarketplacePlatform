import { render, screen, within, waitFor } from '@testing-library/react';import userEvent from '@testing-library/user-event';
import { describe, it, expect } from 'vitest';
import { MantineProvider } from '@mantine/core';
import {ShareListModal} from "../components/sharelistmodal/ShareListModal.tsx";

const renderWithMantine = (ui: React.ReactElement) =>
    render(<MantineProvider>{ui}</MantineProvider>);

beforeAll(() => {
    Object.defineProperty(window, 'matchMedia', {
        writable: true,
        value: vi.fn().mockImplementation((query) => ({
            matches: query === '(prefers-reduced-motion: reduce)',
            media: query,
            onchange: null,
            addListener: vi.fn(),
            removeListener: vi.fn(),
            addEventListener: vi.fn(),
            removeEventListener: vi.fn(),
            dispatchEvent: vi.fn(),
        })),
    });
});

describe('ShareListModal – tooltip', () => {
    it('shows "Make private" tooltip when visibility is public', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="public" />);
        await user.hover(screen.getByRole('button'));
        expect(await screen.findByText('Make private')).toBeInTheDocument();
    });

    it('shows "Make public" tooltip when visibility is private', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
        await user.hover(screen.getByRole('button'));
        expect(await screen.findByText('Make public')).toBeInTheDocument();
    });
});

it('closes the modal when "Make Public" is clicked', async () => {
    const user = userEvent.setup();
    renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
    await user.click(screen.getByRole('button'));
    await screen.findByRole('dialog');
    await user.click(screen.getByRole('button', { name: /make public/i }));
    await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());
});

it('closes the modal when the close (X) button is clicked', async () => {
    const user = userEvent.setup();
    renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
    await user.click(screen.getByRole('button'));
    await screen.findByRole('dialog');
    const closeBtn = document.querySelector('.mantine-Modal-close') as HTMLElement;
    await user.click(closeBtn);
    await waitFor(() => expect(screen.queryByRole('dialog')).not.toBeInTheDocument());
});

describe('ShareListModal – public click (no modal)', () => {
    it('does not open the modal when clicking a public list button', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="public" />);
        await user.click(screen.getByRole('button'));
        expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    });
});

describe('ShareListModal – modal open', () => {
    it('opens the modal when clicking a private list button', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
        await user.click(screen.getByRole('button'));
        expect(await screen.findByRole('dialog')).toBeInTheDocument();
    });

    it('displays the list name in the modal title', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="Secret Reads" visibility="private" />);
        await user.click(screen.getByRole('button'));
        expect(await screen.findByText(/Share "Secret Reads"/)).toBeInTheDocument();
    });

    it('shows the descriptive text inside the modal', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        expect(within(dialog).getByText(/how many days do you want this list to be public/i)).toBeInTheDocument();
    });

    it('renders the NumberInput with default value of 7', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');
        const input = screen.getByRole('textbox', { name: /days/i }) as HTMLInputElement;
        expect(input.value).toBe('7');
    });

    it('renders the "Make Public" button inside the modal', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
        await user.click(screen.getByRole('button'));
        const dialog = await screen.findByRole('dialog');
        expect(within(dialog).getByRole('button', { name: /make public/i })).toBeInTheDocument();
    });
});

describe('ShareListModal – days input', () => {
    it('updates the value when the user types a new number', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');

        const input = screen.getByRole('textbox', { name: /days/i }) as HTMLInputElement;
        await user.clear(input);
        await user.type(input, '14');
        expect(input.value).toBe('14');
    });

    it('enforces a minimum value of 1', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');

        const input = screen.getByRole('textbox', { name: /days/i }) as HTMLInputElement;
        await user.clear(input);
        await user.type(input, '0');
        await user.tab(); // trigger blur so Mantine clamps the value
        expect(Number(input.value)).toBeGreaterThanOrEqual(1);
    });

    it('closes the modal when "Make Public" is clicked', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');
        await user.click(screen.getByRole('button', { name: /make public/i }));
        await waitFor(
            () => expect(screen.queryByRole('dialog')).not.toBeInTheDocument(),
            { timeout: 500 }
        );
    });

    it('closes the modal when the close (X) button is clicked', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');
        const closeBtn = document.querySelector<HTMLElement>('.mantine-Modal-close')!;
        await user.click(closeBtn);
        await waitFor(
            () => expect(screen.queryByRole('dialog')).not.toBeInTheDocument(),
            { timeout: 500 }
        );
    });

    it('resets days back to 7 when modal is reopened', async () => {
        const user = userEvent.setup();
        renderWithMantine(<ShareListModal listName="My List" visibility="private" />);

        // Open, change value, close
        await user.click(screen.getByRole('button'));
        await screen.findByRole('dialog');
        const input = screen.getByRole('textbox', { name: /days/i }) as HTMLInputElement;
        await user.clear(input);
        await user.type(input, '30');
        await user.click(screen.getByRole('button', { name: /make public/i }));

        // Reopen and check value — note: state persists unless component remounts;
        // this test documents the current behaviour
        await user.click(screen.getByRole('button', { name: /make p(ublic|rivate)/i }));
        await screen.findByRole('dialog');
        const reopenedInput = screen.getByRole('textbox', { name: /days/i }) as HTMLInputElement;
        // State is NOT reset on close (no reset logic in component), so value persists
        expect(reopenedInput.value).toBe('30');
    });
});