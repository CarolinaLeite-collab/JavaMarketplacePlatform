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
        getSalesAllowedMethods: vi.fn(),
    },
}));

const mockGetSalesAllowedMethods = vi.mocked(apiClient.getSalesAllowedMethods);

const mockGetByHref = vi.mocked(apiClient.getByHref);

const SALES_HREF = 'http://localhost:8081/sales';
const FIRST_SALE_HREF = 'http://localhost:8081/sales/SALE-1';
const SECOND_SALE_HREF = 'http://localhost:8081/sales/SALE-2';

const firstSale = {
    saleId: 'SALE-1',
    createdAt: '2026-06-20T10:00:00',
    completedAt: '2026-06-20T11:00:00',
    totalAmount: 25,
    currency: 'EUR',
};

const secondSale = {
    saleId: 'SALE-2',
    createdAt: '2026-06-21T12:00:00',
    completedAt: '2026-06-21T13:00:00',
    totalAmount: 40,
    currency: 'EUR',
};

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
        mockGetSalesAllowedMethods.mockResolvedValue(['GET', 'OPTIONS']);
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
        mockGetByHref
            .mockResolvedValueOnce({
                _links: {
                    sale: {
                        href: FIRST_SALE_HREF,
                    },
                },
            })
            .mockResolvedValueOnce(firstSale);

        renderSalesPage();

        expect(
            await screen.findByRole('link', { name: /sale id: sale-1/i }),
        ).toBeInTheDocument();

        expect(mockGetByHref).toHaveBeenNthCalledWith(1, SALES_HREF);
        expect(mockGetByHref).toHaveBeenNthCalledWith(2, FIRST_SALE_HREF);
    });

    it('normalizes multiple sale links and renders multiple Sales', async () => {
        mockGetByHref
            .mockResolvedValueOnce({
                _links: {
                    sale: [
                        { href: FIRST_SALE_HREF },
                        { href: SECOND_SALE_HREF },
                    ],
                },
            })
            .mockResolvedValueOnce(firstSale)
            .mockResolvedValueOnce(secondSale);

        renderSalesPage();

        expect(
            await screen.findByRole('link', { name: /sale id: sale-1/i }),
        ).toBeInTheDocument();

        expect(
            screen.getByRole('link', { name: /sale id: sale-2/i }),
        ).toBeInTheDocument();

        expect(mockGetByHref).toHaveBeenCalledWith(FIRST_SALE_HREF);
        expect(mockGetByHref).toHaveBeenCalledWith(SECOND_SALE_HREF);
    });

    it('displays the Sale ID, dates, total and currency', async () => {
        mockGetByHref
            .mockResolvedValueOnce({
                _links: {
                    sale: {
                        href: FIRST_SALE_HREF,
                    },
                },
            })
            .mockResolvedValueOnce(firstSale);

        renderSalesPage();

        expect(
            await screen.findByText('Sale ID: SALE-1'),
        ).toBeInTheDocument();

        expect(
            screen.getByText('Created: 2026-06-20T10:00:00'),
        ).toBeInTheDocument();

        expect(
            screen.getByText('Completed: 2026-06-20T11:00:00'),
        ).toBeInTheDocument();

        expect(
            screen.getByText('Total: 25 EUR'),
        ).toBeInTheDocument();
    });

    it('navigates to /sales/:saleId when a Sale is selected', async () => {
        const user = userEvent.setup();

        mockGetByHref
            .mockResolvedValueOnce({
                _links: {
                    sale: {
                        href: FIRST_SALE_HREF,
                    },
                },
            })
            .mockResolvedValueOnce(firstSale);

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
            name: /sale id: sale-1/i,
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

    //Add denied-permission tests
    mockGetSalesAllowedMethods.mockResolvedValue([
        'OPTIONS',
    ]);
    expect(mockGetByHref).not.toHaveBeenCalled();


});
