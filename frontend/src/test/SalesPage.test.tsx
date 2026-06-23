import { beforeEach, describe, expect, it, vi } from 'vitest';
import { Route, Routes } from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import { render, screen, waitFor } from '../test-utils';
import AppContext from '../context/AppContext';
import SalesPage from '../pages/Sales/SalesPage';
import { apiClient } from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getByHref: vi.fn(),
        getDirectSalesOptions: vi.fn(),
    },
}));

const mockGetByHref = vi.mocked(apiClient.getByHref);
const mockGetDirectSalesOptions = vi.mocked(apiClient.getDirectSalesOptions);

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
    _links: {
        'sale-line': {
            href: FIRST_SALE_LINE_HREF,
        },
    },
};

const secondSale = {
    saleId: 'SALE-2',
    createdAt: '2026-06-21T12:00:00',
    completedAt: '2026-06-21T13:00:00',
    totalAmount: 40,
    currency: 'EUR',
    _links: {
        'sale-line': {
            href: SECOND_SALE_LINE_HREF,
        },
    },
};

function mockPurchaseResponses() {
    mockGetDirectSalesOptions.mockResolvedValue({
        _links: {
            'direct-sale': {
                href: 'http://localhost:8081/direct-sales/{id}',
            },
        },
    });

    mockGetByHref.mockImplementation(async (href) => {
        switch (href) {
            case SALES_HREF:
                return {
                    _links: {
                        sale: [
                            { href: FIRST_SALE_HREF },
                            { href: SECOND_SALE_HREF },
                        ],
                    },
                };
            case FIRST_SALE_HREF:
                return firstSale;
            case SECOND_SALE_HREF:
                return secondSale;
            case FIRST_SALE_LINE_HREF:
                return { directSaleId: 'DS-1' };
            case SECOND_SALE_LINE_HREF:
                return { directSaleId: 'DS-2' };
            case FIRST_DIRECT_SALE_HREF:
                return {
                    _links: {
                        item: { href: FIRST_ITEM_HREF },
                    },
                };
            case SECOND_DIRECT_SALE_HREF:
                return {
                    _links: {
                        item: { href: SECOND_ITEM_HREF },
                    },
                };
            case FIRST_ITEM_HREF:
                return { title: 'Delirious New York' };
            case SECOND_ITEM_HREF:
                return { title: 'Invisible Cities' };
            default:
                throw new Error(`Unexpected href: ${href}`);
        }
    });
}

function mockSinglePurchaseResponse() {
    mockGetDirectSalesOptions.mockResolvedValue({
        _links: {
            'direct-sale': {
                href: 'http://localhost:8081/direct-sales/{id}',
            },
        },
    });

    mockGetByHref.mockImplementation(async (href) => {
        switch (href) {
            case SALES_HREF:
                return {
                    _links: {
                        sale: {
                            href: FIRST_SALE_HREF,
                        },
                    },
                };
            case FIRST_SALE_HREF:
                return firstSale;
            case FIRST_SALE_LINE_HREF:
                return { directSaleId: 'DS-1' };
            case FIRST_DIRECT_SALE_HREF:
                return {
                    _links: {
                        item: { href: FIRST_ITEM_HREF },
                    },
                };
            case FIRST_ITEM_HREF:
                return { title: 'Delirious New York' };
            default:
                throw new Error(`Unexpected href: ${href}`);
        }
    });
}

function renderWithContext(
    component: React.ReactNode,
    salesHref: string | null = SALES_HREF,
    initialEntries = ['/'],
) {
    const state = {
        app: {
            salesHref,
        },
    } as any;

    return render(
        <AppContext.Provider value={{ state, dispatch: vi.fn() }}>
            {component}
        </AppContext.Provider>,
        { initialEntries },
    );
}

function renderSalesPage() {
    return renderWithContext(<SalesPage />);
}

describe('SalesPage', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('fetches the Sales collection through salesHref', async () => {
        mockGetByHref.mockResolvedValueOnce({
            _links: {},
        });

        renderSalesPage();

        await waitFor(() => {
            expect(mockGetByHref).toHaveBeenCalledWith(SALES_HREF);
        });
    });

    it('normalizes a single sale link and renders one Sale', async () => {
        mockSinglePurchaseResponse();

        renderSalesPage();

        expect(
            await screen.findByRole('link', { name: /delirious new york/i }),
        ).toBeInTheDocument();

        expect(mockGetByHref).toHaveBeenCalledWith(SALES_HREF);
        expect(mockGetByHref).toHaveBeenCalledWith(FIRST_SALE_HREF);
    });

    it('normalizes multiple sale links and renders multiple Sales', async () => {
        mockPurchaseResponses();

        renderSalesPage();

        expect(
            await screen.findByRole('link', { name: /delirious new york/i }),
        ).toBeInTheDocument();

        expect(
            screen.getByRole('link', { name: /invisible cities/i }),
        ).toBeInTheDocument();

        expect(mockGetByHref).toHaveBeenCalledWith(FIRST_SALE_HREF);
        expect(mockGetByHref).toHaveBeenCalledWith(SECOND_SALE_HREF);
    });

    it('displays the item name, Sale ID, dates, total and currency', async () => {
        mockSinglePurchaseResponse();

        renderSalesPage();

        expect(
            await screen.findByText('Delirious New York'),
        ).toBeInTheDocument();

        expect(
            screen.getByText('Sale ID: SALE-1'),
        ).toBeInTheDocument();

        expect(
            screen.queryByText(/Created:/i),
        ).not.toBeInTheDocument();

        expect(
            screen.getByText('Completed: 2026-06-20'),
        ).toBeInTheDocument();

        expect(
            screen.getByText('Total: 25 EUR'),
        ).toBeInTheDocument();
    });

    it('navigates to /sales/:saleId when a Sale is selected', async () => {
        const user = userEvent.setup();

        mockSinglePurchaseResponse();

        renderWithContext(
            <Routes>
                <Route path="/sales" element={<SalesPage />} />
                <Route
                    path="/sales/:saleId"
                    element={<h1>Sale details</h1>}
                />
            </Routes>,
            SALES_HREF,
            ['/sales'],
        );

        const saleLink = await screen.findByRole('link', {
            name: /delirious new york/i,
        });

        await user.click(saleLink);

        expect(
            screen.getByRole('heading', { name: /sale details/i }),
        ).toBeInTheDocument();
    });

    it('displays the loading state while purchases are loading', () => {
        mockGetByHref.mockImplementation(
            () => new Promise(() => {}),
        );

        renderSalesPage();

        expect(
            screen.getByText(/loading purchases/i),
        ).toBeInTheDocument();
    });

    it('displays an empty purchase history', async () => {
        mockGetByHref.mockResolvedValueOnce({
            _links: {},
        });

        renderSalesPage();

        expect(
            await screen.findByText(/you have no purchases yet/i),
        ).toBeInTheDocument();
    });

    it('displays an error when purchases cannot be loaded', async () => {
        mockGetByHref.mockRejectedValueOnce(
            new Error('Request failed'),
        );

        renderSalesPage();

        expect(
            await screen.findByText(/could not load your purchases/i),
        ).toBeInTheDocument();
    });
});
