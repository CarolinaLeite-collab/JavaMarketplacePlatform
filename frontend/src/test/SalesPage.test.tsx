import { beforeEach, describe, expect, it, vi } from 'vitest';
import { render, screen, waitFor } from '../test-utils';
import userEvent from '@testing-library/user-event';
import AppContext from '../context/AppContext';
import SalesPage from '../pages/Sales/SalesPage';
import { apiClient } from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getByHref: vi.fn(),
        getDirectSalesOptions: vi.fn(),
        getDirectSaleById: vi.fn(),
        getItemById: vi.fn(),
    },
}));

const mockGetByHref = vi.mocked(apiClient.getByHref);
const mockGetDirectSalesOptions = vi.mocked(apiClient.getDirectSalesOptions);
const mockGetDirectSaleById = vi.mocked(apiClient.getDirectSaleById);
const mockGetItemById = vi.mocked(apiClient.getItemById);

const SALES_HREF = 'http://localhost:8081/sales';
const FIRST_SALE_HREF = 'http://localhost:8081/sales/SALE-1';
const SECOND_SALE_HREF = 'http://localhost:8081/sales/SALE-2';
const FIRST_SALE_LINE_HREF = 'http://localhost:8081/sales/SALE-1/sale-lines/SL-1';
const SECOND_SALE_LINE_HREF = 'http://localhost:8081/sales/SALE-2/sale-lines/SL-2';
const FIRST_DIRECT_SALE_HREF = 'http://localhost:8081/direct-sales/DS-1';
const SECOND_DIRECT_SALE_HREF = 'http://localhost:8081/direct-sales/DS-2';
const FIRST_ITEM_HREF = 'http://localhost:8081/items/ITEM-1';
const SECOND_ITEM_HREF = 'http://localhost:8081/items/ITEM-2';

const firstSale = {
    saleId: 'SALE-1',
    createdAt: '2026-06-20T10:00:00',
    completedAt: '2026-06-20T11:00:00',
    totalAmount: 25,
    currency: 'EUR',
    saleLines: [
        { saleLineId: 'SL-1', directSaleId: 'DS-1', sellerId: 'pedro@aeiou.com', price: 25, currency: 'EUR' },
    ],
    _links: {
        'sale-line': { href: FIRST_SALE_LINE_HREF },
    },
};

const secondSale = {
    saleId: 'SALE-2',
    createdAt: '2026-06-21T12:00:00',
    completedAt: null,
    totalAmount: 40,
    currency: 'EUR',
    saleLines: [
        { saleLineId: 'SL-2', directSaleId: 'DS-2', sellerId: 'ana@aeiou.com', price: 40, currency: 'EUR' },
    ],
    _links: {
        'sale-line': { href: SECOND_SALE_LINE_HREF },
    },
};

function mockPurchaseResponses() {
    mockGetDirectSalesOptions.mockResolvedValue({
        _links: { 'direct-sale': { href: 'http://localhost:8081/direct-sales/{id}' } },
    });

    mockGetByHref.mockImplementation(async (href) => {
        switch (href) {
            case SALES_HREF:
                return { _links: { sale: [{ href: FIRST_SALE_HREF }, { href: SECOND_SALE_HREF }] } };
            case FIRST_SALE_HREF:
                return firstSale;
            case SECOND_SALE_HREF:
                return secondSale;
            case FIRST_SALE_LINE_HREF:
                return { directSaleId: 'DS-1' };
            case SECOND_SALE_LINE_HREF:
                return { directSaleId: 'DS-2' };
            case FIRST_DIRECT_SALE_HREF:
                return { _links: { item: { href: FIRST_ITEM_HREF } } };
            case SECOND_DIRECT_SALE_HREF:
                return { _links: { item: { href: SECOND_ITEM_HREF } } };
            case FIRST_ITEM_HREF:
                return { title: 'Delirious New York' };
            case SECOND_ITEM_HREF:
                return { title: 'Invisible Cities' };
            default:
                throw new Error(`Unexpected href: ${href}`);
        }
    });

    mockGetDirectSaleById.mockImplementation(async (id) => {
        if (id === 'DS-1') return { itemsId: ['ITEM-1'] };
        if (id === 'DS-2') return { itemsId: ['ITEM-2'] };
        throw new Error(`Unexpected id: ${id}`);
    });

    mockGetItemById.mockImplementation(async (id) => {
        if (id === 'ITEM-1') return { title: 'Delirious New York', authorName: 'Rem Koolhaas', condition: 'GOOD', picture: null };
        if (id === 'ITEM-2') return { title: 'Invisible Cities', authorName: 'Italo Calvino', condition: 'FAIR', picture: null };
        throw new Error(`Unexpected id: ${id}`);
    });
}

function mockSinglePurchaseResponse() {
    mockGetDirectSalesOptions.mockResolvedValue({
        _links: { 'direct-sale': { href: 'http://localhost:8081/direct-sales/{id}' } },
    });

    mockGetByHref.mockImplementation(async (href) => {
        switch (href) {
            case SALES_HREF:
                return { _links: { sale: { href: FIRST_SALE_HREF } } };
            case FIRST_SALE_HREF:
                return firstSale;
            case FIRST_SALE_LINE_HREF:
                return { directSaleId: 'DS-1' };
            case FIRST_DIRECT_SALE_HREF:
                return { _links: { item: { href: FIRST_ITEM_HREF } } };
            case FIRST_ITEM_HREF:
                return { title: 'Delirious New York' };
            default:
                throw new Error(`Unexpected href: ${href}`);
        }
    });

    mockGetDirectSaleById.mockResolvedValue({ itemsId: ['ITEM-1'] });
    mockGetItemById.mockResolvedValue({
        title: 'Delirious New York', authorName: 'Rem Koolhaas', condition: 'GOOD', picture: null,
    });
}

function renderWithContext(salesHref = SALES_HREF) {
    const state = { app: { salesHref } } as any;
    return render(
        <AppContext.Provider value={{ state, dispatch: vi.fn() }}>
            <SalesPage />
        </AppContext.Provider>,
    );
}

describe('SalesPage', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('fetches the Sales collection through salesHref', async () => {
        mockGetByHref.mockResolvedValueOnce({ _links: {} });
        renderWithContext();

        await waitFor(() => {
            expect(mockGetByHref).toHaveBeenCalledWith(SALES_HREF);
        });
    });

    it('renders a single sale with item name', async () => {
        mockSinglePurchaseResponse();
        renderWithContext();

        expect(await screen.findByText('Delirious New York')).toBeInTheDocument();
    });

    it('renders multiple sales', async () => {
        mockPurchaseResponses();
        renderWithContext();

        expect(await screen.findByText('Delirious New York')).toBeInTheDocument();
        const invisibleCities = screen.getAllByText('Invisible Cities');
        expect(invisibleCities.length).toBeGreaterThanOrEqual(1);
    });

    it('displays sale ID, completion date and total', async () => {
        mockSinglePurchaseResponse();
        renderWithContext();

        expect(await screen.findByText('Sale ID: SALE-1')).toBeInTheDocument();
        expect(screen.getByText('Completed: 2026-06-20')).toBeInTheDocument();
        expect(screen.getByText('Total: 25.00 EUR')).toBeInTheDocument();
    });

    it('displays Completed badge when sale is completed', async () => {
        mockSinglePurchaseResponse();
        renderWithContext();

        expect(await screen.findByText('Completed')).toBeInTheDocument();
    });

    it('displays Pending badge when sale is not completed', async () => {
        mockPurchaseResponses();
        renderWithContext();

        expect(await screen.findByText('Pending')).toBeInTheDocument();
    });

    it('expands accordion and shows sale line details', async () => {
        const user = userEvent.setup();
        mockSinglePurchaseResponse();
        renderWithContext();

        const control = await screen.findByText('Delirious New York');
        await user.click(control);

        expect(await screen.findByText('Item')).toBeInTheDocument();
        expect(screen.getByText('Condition')).toBeInTheDocument();
        expect(screen.getByText('Seller')).toBeInTheDocument();
        expect(screen.getByText('Price')).toBeInTheDocument();
    });

    it('shows item details after expanding accordion', async () => {
        const user = userEvent.setup();
        mockSinglePurchaseResponse();
        renderWithContext();

        const control = await screen.findByText('Delirious New York');
        await user.click(control);

        expect(await screen.findByText('Rem Koolhaas')).toBeInTheDocument();
        expect(screen.getByText('GOOD')).toBeInTheDocument();
        expect(screen.getByText('pedro')).toBeInTheDocument();
        expect(screen.getByText('25 EUR')).toBeInTheDocument();
    });

    it('displays loading state while purchases are loading', () => {
        mockGetByHref.mockImplementation(() => new Promise(() => {}));
        renderWithContext();

        expect(screen.getByText(/loading purchases/i)).toBeInTheDocument();
    });

    it('displays empty purchase history', async () => {
        mockGetByHref.mockResolvedValueOnce({ _links: {} });
        renderWithContext();

        expect(await screen.findByText(/you have no purchases yet/i)).toBeInTheDocument();
    });

    it('displays error when purchases cannot be loaded', async () => {
        mockGetByHref.mockRejectedValueOnce(new Error('Request failed'));
        renderWithContext();

        expect(await screen.findByText(/could not load your purchases/i)).toBeInTheDocument();
    });
});