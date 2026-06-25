import { MantineProvider } from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import AppContext from '../context/AppContext';
import { ADD_TO_CART } from '../context/cart/CartActions';
import { useUser } from '../context/UserContext';
import DirectSaleDetailPage from '../pages/DirectSale/DirectSaleDetailPage';
import { apiClient } from '../services/apiClient';

vi.mock('../components/layout/DefaultLayout.tsx', () => ({
    DefaultLayout: ({ children }: { children: React.ReactNode }) => <>{children}</>,
}));

vi.mock('../context/UserContext', async () => {
    const actual = await vi.importActual('../context/UserContext');
    return {
        ...actual,
        useUser: vi.fn(),
    };
});

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getDirectSalesOptions: vi.fn(),
        getDirectSaleWithoutPrice: vi.fn(),
        getByHref: vi.fn(),
        getEditionById: vi.fn(),
        getPublishingCompanyById: vi.fn(),
        postByHref: vi.fn(),
    },
}));

vi.mock('@mantine/notifications', async () => {
    const actual = await vi.importActual('@mantine/notifications');
    return {
        ...actual,
        notifications: {
            show: vi.fn(),
        },
    };
});

const directSaleHref = 'http://localhost:8081/direct-sales/DS-123';
const itemHref = 'http://localhost:8081/items/ITEM-001';
const cartHref = 'http://localhost:8081/shopping-carts/SC-001/lines';

const directSale = {
    directSaleId: 'DS-123',
    priceValue: 19.99,
    priceCurrency: 'EUR',
    endDate: '2099-06-30T12:00:00Z',
    status: 'ACTIVE',
    sellerId: 'ana@aeiou.com',
    _links: {
        item: { href: itemHref },
        'shopping-cart': { href: cartHref },
    },
};

const item = {
    itemId: 'ITEM-001',
    editionId: 'ED-001',
    title: 'Dune',
    publicationTypeName: 'BOOK',
    picture: 'https://example.com/dune.jpg',
    authorName: 'Frank Herbert',
    identifier: '9780441172719',
    condition: 'GOOD',
    description: 'A well-kept copy.',
    genreName: 'Science Fiction',
    publishingYear: 1965,
    language: 'ENGLISH',
    synopsis: 'A story set on Arrakis.',
};

const edition = {
    editionId: 'ED-001',
    publishingCompanyId: 'PC-001',
    numberOfPages: 412,
    editionNumber: 1,
    binding: 'HARDCOVER',
    weight: { value: 600, unit: 'GRAMS' },
    dimension: {
        width: 15,
        height: 23,
        depth: 4,
        unit: 'CENTIMETERS',
    },
};

const publisher = {
    publishingCompanyId: 'PC-001',
    publishingCompanyName: 'Chilton Books',
};

function renderDirectSaleDetail({
    currentUser = 'pedro@aeiou.com',
    cartItems = [],
    dispatch = vi.fn(),
    routeState,
}: {
    currentUser?: string;
    cartItems?: Array<{ id: string }>;
    dispatch?: ReturnType<typeof vi.fn>;
    routeState?: { selfHref?: string | null };
} = {}) {
    vi.mocked(useUser).mockReturnValue({
        currentUser,
        toggleUser: vi.fn(),
    });

    return {
        dispatch,
        ...render(
            <MantineProvider>
                <AppContext.Provider
                    value={{
                        state: {
                            app: {},
                            lists: {
                                lists: [],
                                publicLists: [],
                                genres: [],
                                error: null,
                            },
                            cart: { items: cartItems },
                        } as any,
                        dispatch,
                    }}
                >
                    <MemoryRouter
                        initialEntries={[
                            {
                                pathname: '/directSales/DS-123',
                                state: routeState,
                            },
                        ]}
                    >
                        <Routes>
                            <Route
                                path="/directSales/:directSaleId"
                                element={<DirectSaleDetailPage />}
                            />
                        </Routes>
                    </MemoryRouter>
                </AppContext.Provider>
            </MantineProvider>,
        ),
    };
}

describe('DirectSaleDetailPage', () => {
    beforeEach(() => {
        vi.clearAllMocks();

        vi.mocked(apiClient.getDirectSalesOptions).mockResolvedValue({
            _links: {
                'direct-sale': {
                    href: 'http://localhost:8081/direct-sales/{id}',
                },
            },
        });

        vi.mocked(apiClient.getByHref).mockImplementation(async (href: string) => {
            if (href === directSaleHref) return directSale;
            if (href === itemHref) return item;
            throw new Error('404');
        });

        vi.mocked(apiClient.getDirectSaleWithoutPrice).mockResolvedValue({
            ...directSale,
            priceValue: undefined,
            priceCurrency: undefined,
            _links: { item: { href: itemHref } },
        });

        vi.mocked(apiClient.getEditionById).mockResolvedValue(edition);
        vi.mocked(apiClient.getPublishingCompanyById).mockResolvedValue(publisher);
    });

    it('loads and renders direct sale details for an authenticated user', async () => {
        renderDirectSaleDetail();

        expect(
            await screen.findByRole('heading', { name: /book:\s*dune/i }),
        ).toBeInTheDocument();
        expect(screen.getByText('19.99 EUR')).toBeInTheDocument();
        expect(screen.getByText(/sold by ana/i)).toBeInTheDocument();
        expect(screen.getByText('Frank Herbert')).toBeInTheDocument();
        expect(screen.getByText('Chilton Books')).toBeInTheDocument();
        expect(screen.getByText('412')).toBeInTheDocument();
        expect(screen.getByText('600 GRAMS')).toBeInTheDocument();
        expect(screen.getByText('15 x 23 x 4 CENTIMETERS')).toBeInTheDocument();

        expect(apiClient.getDirectSalesOptions).toHaveBeenCalledTimes(1);
        expect(apiClient.getByHref).toHaveBeenCalledWith(directSaleHref);
    });

    it('uses the self link supplied through route state', async () => {
        const stateHref = 'http://localhost:8081/direct-sales/from-state';

        vi.mocked(apiClient.getByHref).mockImplementation(async (href: string) => {
            if (href === stateHref) return directSale;
            if (href === itemHref) return item;
            throw new Error('404');
        });

        renderDirectSaleDetail({
            routeState: { selfHref: stateHref },
        });

        expect(await screen.findByText('Dune')).toBeInTheDocument();
        expect(apiClient.getByHref).toHaveBeenCalledWith(stateHref);
    });

    it('uses the public no-price endpoint for a guest and hides the price', async () => {
        renderDirectSaleDetail({
            currentUser: 'guest@aeiou.com',
        });

        expect(await screen.findByText('Dune')).toBeInTheDocument();
        expect(apiClient.getDirectSaleWithoutPrice).toHaveBeenCalledWith('DS-123');
        expect(apiClient.getDirectSalesOptions).not.toHaveBeenCalled();
        expect(screen.queryByText('19.99 EUR')).not.toBeInTheDocument();
        expect(screen.getByRole('button', { name: /add to cart/i })).toBeDisabled();
    });

    it('shows the not-found state when loading the direct sale fails', async () => {
        vi.mocked(apiClient.getDirectSalesOptions).mockRejectedValue(
            new Error('500'),
        );

        renderDirectSaleDetail();

        expect(
            await screen.findByText(/direct sale not found/i),
        ).toBeInTheDocument();
    });

    it('adds the direct sale to the cart', async () => {
        const user = userEvent.setup();
        const dispatch = vi.fn();

        vi.mocked(apiClient.postByHref).mockResolvedValue({
            _links: {
                self: {
                    href: 'http://localhost:8081/shopping-carts/SC-001/lines/CL-001',
                },
            },
        });

        renderDirectSaleDetail({ dispatch });

        await user.click(
            await screen.findByRole('button', { name: /add to cart/i }),
        );

        expect(apiClient.postByHref).toHaveBeenCalledWith(cartHref, {
            directSaleId: 'DS-123',
        });
        expect(dispatch).toHaveBeenCalledWith({
            type: ADD_TO_CART,
            payload: {
                id: 'DS-123',
                name: 'Dune',
                price: '19.99 EUR',
                priceValue: 19.99,
                currency: 'EUR',
                deleteHref:
                    'http://localhost:8081/shopping-carts/SC-001/lines/CL-001',
                image: 'https://example.com/dune.jpg',
            },
        });
        expect(notifications.show).toHaveBeenCalledWith(
            expect.objectContaining({
                title: 'Item added to cart',
                color: 'green',
            }),
        );
    });

    it('disables adding the seller own direct sale to the cart', async () => {
        renderDirectSaleDetail({
            currentUser: 'ana@aeiou.com',
        });

        expect(
            await screen.findByRole('button', { name: /add to cart/i }),
        ).toBeDisabled();
    });

    it('shows Already in Cart and disables the button when the sale is in the cart', async () => {
        renderDirectSaleDetail({
            cartItems: [{ id: 'DS-123' }],
        });

        expect(
            await screen.findByRole('button', { name: /already in cart/i }),
        ).toBeDisabled();
    });

    it('disables adding to cart when the shopping-cart link is absent', async () => {
        vi.mocked(apiClient.getByHref).mockImplementation(async (href: string) => {
            if (href === directSaleHref) {
                return {
                    ...directSale,
                    _links: { item: { href: itemHref } },
                };
            }
            if (href === itemHref) return item;
            throw new Error('404');
        });

        renderDirectSaleDetail();

        expect(
            await screen.findByRole('button', { name: /add to cart/i }),
        ).toBeDisabled();
    });

    it('shows an error notification when adding to cart fails', async () => {
        const user = userEvent.setup();

        vi.mocked(apiClient.postByHref).mockRejectedValue(new Error('500'));

        renderDirectSaleDetail();

        await user.click(
            await screen.findByRole('button', { name: /add to cart/i }),
        );

        await waitFor(() => {
            expect(notifications.show).toHaveBeenCalledWith(
                expect.objectContaining({
                    title: 'Item could not be added to cart',
                    color: 'red',
                }),
            );
        });
    });
});
