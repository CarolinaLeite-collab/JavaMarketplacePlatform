import {render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {beforeEach, describe, expect, it, vi} from 'vitest';
import {MantineProvider} from '@mantine/core';
import {AddItemToListDropDown} from '../components/addItemToListModal/AddItemToListDropDown';
import {apiClient} from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getByHref: vi.fn(),
    },
}));

const mockLibraryItems = [
    { itemId: 'ITEM-001', title: 'The War of the Worlds' },
    { itemId: 'ITEM-002', title: 'Dune' },
    { itemId: 'ITEM-003', title: '1984' },
];

const renderComponent = (onConfirm = vi.fn(), existingItemIds: string[] = []) =>
    render(
        <MantineProvider>
            <AddItemToListDropDown
                listName="My Fiction"
                libraryHref="http://test/my-library/"
                existingItemIds={existingItemIds}
                onConfirm={onConfirm}
            />
        </MantineProvider>
    );

beforeEach(() => {
    vi.mocked(apiClient.getByHref).mockResolvedValue({
        _embedded: { items: mockLibraryItems },
    });
});

describe('AddItemToListDropDown – trigger button', () => {
    it('renders the add button', () => {
        renderComponent();
        expect(screen.getByRole('button')).toBeInTheDocument();
    });

    it('does not show the popover on initial render', () => {
        renderComponent();
        expect(screen.queryByText(/Add item to/i)).not.toBeInTheDocument();
    });
});

describe('AddItemToListDropDown – popover open', () => {
    it('opens the popover when the button is clicked', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        expect(await screen.findByText(/Add item to "My Fiction"/i)).toBeInTheDocument();
    });

    it('fetches and shows library items', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        expect(await screen.findByLabelText(/1984/i)).toBeInTheDocument();
    });

    it('shows Cancel and Confirm buttons', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        await screen.findByText(/Add item to/i);
        expect(screen.getByRole('button', { name: /cancel/i, hidden: true })).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /confirm/i, hidden: true })).toBeInTheDocument();
    });
});

describe('AddItemToListDropDown – Confirm button state', () => {
    it('Confirm is disabled when nothing is selected', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        await screen.findByText(/Add item to/i);
        expect(screen.getByRole('button', { name: /confirm/i, hidden: true })).toBeDisabled();
    });

    it('Confirm is enabled after selecting an item', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        const checkbox = await screen.findByLabelText(/1984/i);
        await user.click(checkbox);
        expect(screen.getByRole('button', { name: /confirm/i, hidden: true })).not.toBeDisabled();
    });
});

describe('AddItemToListDropDown – selection', () => {
    it('checks and unchecks an item', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        const checkbox = await screen.findByLabelText(/1984/i);
        await user.click(checkbox);
        expect(checkbox).toBeChecked();
        await user.click(checkbox);
        expect(checkbox).not.toBeChecked();
    });

    it('allows selecting multiple items', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        await screen.findByText(/Add item to/i);
        await user.click(screen.getByLabelText(/1984/i));
        await user.click(screen.getByLabelText(/Dune/i));
        expect(screen.getByLabelText(/1984/i)).toBeChecked();
        expect(screen.getByLabelText(/Dune/i)).toBeChecked();
    });
});

describe('AddItemToListDropDown – Cancel', () => {
    it('closes the popover when Cancel is clicked', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        await screen.findByText(/Add item to/i);
        await user.click(screen.getByRole('button', { name: /cancel/i, hidden: true }));
        await waitFor(() =>
            expect(screen.queryByText(/Add item to/i)).not.toBeInTheDocument()
        );
    });

    it('clears selection when Cancel is clicked', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        await user.click(await screen.findByLabelText(/1984/i));
        await user.click(screen.getByRole('button', { name: /cancel/i, hidden: true }));
        await user.click(screen.getByRole('button'));
        const checkbox = await screen.findByLabelText(/1984/i);
        expect(checkbox).not.toBeChecked();
    });
});

describe('AddItemToListDropDown – Confirm', () => {
    it('calls onConfirm with the selected item ids', async () => {
        const onConfirm = vi.fn();
        const user = userEvent.setup();
        renderComponent(onConfirm);
        await user.click(screen.getByRole('button'));
        await user.click(await screen.findByLabelText(/1984/i));
        await user.click(screen.getByRole('button', { name: /confirm/i, hidden: true }));
        expect(onConfirm).toHaveBeenCalledWith(['ITEM-003']);
    });

    it('closes the popover after confirming', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        await user.click(await screen.findByLabelText(/1984/i));
        await user.click(screen.getByRole('button', { name: /confirm/i, hidden: true }));
        await waitFor(() =>
            expect(screen.queryByText(/Add item to/i)).not.toBeInTheDocument()
        );
    });

    it('clears selection after confirming', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(screen.getByRole('button'));
        await user.click(await screen.findByLabelText(/1984/i));
        await user.click(screen.getByRole('button', { name: /confirm/i, hidden: true }));
        await user.click(screen.getByRole('button'));
        const checkbox = await screen.findByLabelText(/1984/i);
        expect(checkbox).not.toBeChecked();
    });
});

describe('AddItemToListDropDown – existingItemIds', () => {
    it('disables checkboxes for items already in the list', async () => {
        const user = userEvent.setup();
        renderComponent(vi.fn(), ['ITEM-003']);
        await user.click(screen.getByRole('button'));
        const checkbox = await screen.findByLabelText(/1984/i);
        expect(checkbox).toBeDisabled();
    });

    it('does not disable items not in the list', async () => {
        const user = userEvent.setup();
        renderComponent(vi.fn(), ['ITEM-003']);
        await user.click(screen.getByRole('button'));
        const checkbox = await screen.findByLabelText(/Dune/i);
        expect(checkbox).not.toBeDisabled();
    });

    it('shows tooltip when hovering over a disabled item', async () => {
        const user = userEvent.setup();
        renderComponent(vi.fn(), ['ITEM-003']);
        await user.click(screen.getByRole('button'));
        const checkbox = await screen.findByLabelText(/1984/i);
        await user.hover(checkbox.parentElement!);
        expect(await screen.findByText('Already in this list')).toBeInTheDocument();
    });

    it('does not include disabled items in onConfirm', async () => {
        const onConfirm = vi.fn();
        const user = userEvent.setup();
        renderComponent(onConfirm, ['ITEM-003']);
        await user.click(screen.getByRole('button'));
        await user.click(await screen.findByLabelText(/Dune/i));
        await user.click(screen.getByRole('button', { name: /confirm/i, hidden: true }));
        expect(onConfirm).toHaveBeenCalledWith(['ITEM-002']);
    });
});
