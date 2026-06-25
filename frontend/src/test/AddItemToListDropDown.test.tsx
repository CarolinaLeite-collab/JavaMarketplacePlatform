import {render, screen, waitFor, within} from '@testing-library/react';
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
            >
                <button>Add Item To List</button>
            </AddItemToListDropDown>
        </MantineProvider>
    );

const body = () => within(document.body);

beforeEach(() => {
    vi.mocked(apiClient.getByHref).mockResolvedValue({
        _embedded: { items: mockLibraryItems },
    });
});

describe('AddItemToListDropDown – trigger button', () => {
    it('renders the add button', () => {
        renderComponent();
        expect(body().getByRole('button', { name: /add item to list/i })).toBeInTheDocument();
    });

    it('does not show the popover on initial render', () => {
        renderComponent();
        expect(body().queryByText(/Add item to "My Fiction"/i, { hidden: true })).not.toBeInTheDocument();
    });
});

describe('AddItemToListDropDown – popover open', () => {
    it('opens the popover when the button is clicked', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        expect(await body().findByText(/Add item to "My Fiction"/i, { hidden: true })).toBeInTheDocument();
    });

    it('fetches and shows library items', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        expect(await body().findByLabelText(/1984/i, { hidden: true })).toBeInTheDocument();
    });

    it('shows Cancel and Confirm buttons', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        await body().findByText(/Add item to "My Fiction"/i, { hidden: true });
        expect(await body().findByRole('button', { name: /cancel/i, hidden: true })).toBeInTheDocument();
        expect(await body().findByRole('button', { name: /confirm/i, hidden: true })).toBeInTheDocument();
    });
});

describe('AddItemToListDropDown – Confirm button state', () => {
    it('Confirm is disabled when nothing is selected', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        await body().findByText(/Add item to "My Fiction"/i, { hidden: true });
        expect(await body().findByRole('button', { name: /confirm/i, hidden: true })).toBeDisabled();
    });

    it('Confirm is enabled after selecting an item', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        const checkbox = await body().findByLabelText(/1984/i, { hidden: true });
        await user.click(checkbox);
        expect(await body().findByRole('button', { name: /confirm/i, hidden: true })).not.toBeDisabled();
    });
});

describe('AddItemToListDropDown – selection', () => {
    it('checks and unchecks an item', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        const checkbox = await body().findByLabelText(/1984/i, { hidden: true });
        await user.click(checkbox);
        expect(checkbox).toBeChecked();
        await user.click(checkbox);
        expect(checkbox).not.toBeChecked();
    });

    it('allows selecting multiple items', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        await user.click(await body().findByLabelText(/1984/i, { hidden: true }));
        await user.click(await body().findByLabelText(/Dune/i, { hidden: true }));
        expect(body().getByLabelText(/1984/i, { hidden: true })).toBeChecked();
        expect(body().getByLabelText(/Dune/i, { hidden: true })).toBeChecked();
    });
});

describe('AddItemToListDropDown – Cancel', () => {
    it('closes the popover when Cancel is clicked', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        await user.click(await body().findByRole('button', { name: /cancel/i, hidden: true }));
        await waitFor(() =>
            expect(body().queryByText(/Add item to "My Fiction"/i, { hidden: true })).not.toBeInTheDocument()
        );
    });

    it('clears selection when Cancel is clicked', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        await user.click(await body().findByLabelText(/1984/i, { hidden: true }));
        await user.click(await body().findByRole('button', { name: /cancel/i, hidden: true }));
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        const checkbox = await body().findByLabelText(/1984/i, { hidden: true });
        expect(checkbox).not.toBeChecked();
    });
});

describe('AddItemToListDropDown – Confirm', () => {
    it('calls onConfirm with the selected item ids', async () => {
        const onConfirm = vi.fn();
        const user = userEvent.setup();
        renderComponent(onConfirm);
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        await user.click(await body().findByLabelText(/1984/i, { hidden: true }));
        await user.click(await body().findByRole('button', { name: /confirm/i, hidden: true }));
        expect(onConfirm).toHaveBeenCalledWith(['ITEM-003']);
    });

    it('closes the popover after confirming', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        await user.click(await body().findByLabelText(/1984/i, { hidden: true }));
        await user.click(await body().findByRole('button', { name: /confirm/i, hidden: true }));
        await waitFor(() =>
            expect(body().queryByText(/Add item to "My Fiction"/i, { hidden: true })).not.toBeInTheDocument()
        );
    });

    it('clears selection after confirming', async () => {
        const user = userEvent.setup();
        renderComponent();
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        await user.click(await body().findByLabelText(/1984/i, { hidden: true }));
        await user.click(await body().findByRole('button', { name: /confirm/i, hidden: true }));
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        const checkbox = await body().findByLabelText(/1984/i, { hidden: true });
        expect(checkbox).not.toBeChecked();
    });
});

describe('AddItemToListDropDown – existingItemIds', () => {
    it('disables checkboxes for items already in the list', async () => {
        const user = userEvent.setup();
        renderComponent(vi.fn(), ['ITEM-003']);
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        const checkbox = await body().findByLabelText(/1984/i, { hidden: true });
        expect(checkbox).toBeDisabled();
    });

    it('does not disable items not in the list', async () => {
        const user = userEvent.setup();
        renderComponent(vi.fn(), ['ITEM-003']);
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        const checkbox = await body().findByLabelText(/Dune/i, { hidden: true });
        expect(checkbox).not.toBeDisabled();
    });

    it('shows tooltip when hovering over a disabled item', async () => {
        const user = userEvent.setup();
        renderComponent(vi.fn(), ['ITEM-003']);
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        const checkbox = await body().findByLabelText(/1984/i, { hidden: true });
        await user.hover(checkbox.parentElement!);
        expect(await body().findByText('Already in this list', { hidden: true })).toBeInTheDocument();
    });

    it('does not include disabled items in onConfirm', async () => {
        const onConfirm = vi.fn();
        const user = userEvent.setup();
        renderComponent(onConfirm, ['ITEM-003']);
        await user.click(body().getByRole('button', { name: /add item to list/i }));
        await user.click(await body().findByLabelText(/Dune/i, { hidden: true }));
        await user.click(await body().findByRole('button', { name: /confirm/i, hidden: true }));
        expect(onConfirm).toHaveBeenCalledWith(['ITEM-002']);
    });
});
