import {render, screen, waitFor} from '@/test-utils';
import userEvent from '@testing-library/user-event';
import AppContext from '../context/AppContext';
import {CreateSaleModal} from '../components/createSaleModal/CreateSaleModal';
import {notifications} from '@mantine/notifications';
import {clearSalesMessages, createDirectSale, getMyLibraryItems,} from '../context/sales/SalesActions.jsx';
import {beforeEach, vi} from 'vitest';

vi.mock('@mantine/notifications', () => ({
    notifications: {
        show: vi.fn(),
    },
}));

vi.mock('../context/sales/SalesActions.jsx', () => ({
    clearSalesMessages: vi.fn(() => ({ type: 'CLEAR_SALES_MESSAGES' })),
    createDirectSale: vi.fn(),
    getMyLibraryItems: vi.fn(),
}));

const mockDispatch = vi.fn();

const mockContextValue = {
    state: {
        sales: {
            libraryItems: [
                { value: 'item-1', label: 'Dune' },
                { value: 'item-2', label: '1984' },
            ],
            error: null,
            successMessage: null,
        },
    },
    dispatch: mockDispatch,
};

function renderComponent(props = {}) {
    return render(
        <AppContext.Provider value={mockContextValue}>
            <CreateSaleModal opened={true} onClose={vi.fn()} {...props} />
        </AppContext.Provider>
    );
}

describe('CreateSaleModal', () => {
    beforeEach( () => {
        vi.clearAllMocks();
    });

    it('renders all form fields when opened', () => {
        renderComponent();

        expect(screen.getByRole('heading', { name: /create new sale/i })).toBeInTheDocument();
        expect(screen.getByLabelText(/sale type/i, { selector: 'input' })).toBeInTheDocument();
        expect(screen.getByPlaceholderText(/select an item from your library/i)).toBeInTheDocument();
        expect(screen.getByRole('textbox', { name: /price value/i })).toBeInTheDocument();
        expect(screen.getByLabelText(/currency/i, { selector: 'input' })).toBeInTheDocument();
        expect(screen.getByRole('textbox', { name: /duration \(days\)/i })).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /create sale/i })).toBeInTheDocument();
    });

    it('loads sales messages and library items when opened', async () => {
        renderComponent();

        await waitFor(() => {
            expect(clearSalesMessages).toHaveBeenCalledTimes(1);
            expect(mockDispatch).toHaveBeenCalledWith({ type: 'CLEAR_SALES_MESSAGES' });
            expect(getMyLibraryItems).toHaveBeenCalledWith(mockDispatch);
        });
    });

    it('shows validation errors when submitting empty form', async () => {
        const user = userEvent.setup();
        renderComponent();

        await user.click(screen.getByRole('button', { name: /create sale/i }));

        expect(await screen.findByText(/item is required/i)).toBeInTheDocument();
        expect(await screen.findByText(/price value is required and must be greater than 0/i)).toBeInTheDocument();
        expect(createDirectSale).not.toHaveBeenCalled();
    });

    it('calls createDirectSale with correct payload when form is valid', async () => {
        const user = userEvent.setup();
        createDirectSale.mockResolvedValue(true);

        renderComponent();

        await user.click(screen.getByPlaceholderText(/select an item from your library/i));
        await user.click(screen.getByText('Dune'));

        const priceInput = screen.getByRole('textbox', { name: /price value/i });
        await user.clear(priceInput);
        await user.type(priceInput, '12.5');

        const durationInput = screen.getByRole('textbox', { name: /duration \(days\)/i });
        await user.clear(durationInput);
        await user.type(durationInput, '7');

        await user.click(screen.getByRole('button', { name: /create sale/i }));

        await waitFor(() => {
            expect(createDirectSale).toHaveBeenCalledWith(mockDispatch, {
                itemsId: ['item-1'],
                priceValue: 12.5,
                priceCurrency: 'EUR',
                timeLimitSeconds: 604800,
            });
        });
    });

    it('closes modal and shows success notification after successful creation', async () => {
        const user = userEvent.setup();
        const onClose = vi.fn();
        createDirectSale.mockResolvedValue(true);

        renderComponent({ onClose });

        await user.click(screen.getByPlaceholderText(/select an item from your library/i));
        await user.click(screen.getByText('Dune'));

        const priceInput = screen.getByRole('textbox', { name: /price value/i });
        await user.clear(priceInput);
        await user.type(priceInput, '10');

        await user.click(screen.getByRole('button', { name: /create sale/i }));

        await waitFor(() => {
            expect(onClose).toHaveBeenCalledTimes(1);
            expect(notifications.show).toHaveBeenCalledWith({
                title: 'Direct sale created',
                message: 'The item was successfully put on direct sale.',
                color: 'green',
                autoClose: 3000,
            });
        });
    });
});