import { render, screen, waitFor } from '@/test-utils';
import AppContext from '../context/AppContext';
import { ShoppingCart } from '../components/shoppingCart/ShoppingCart.tsx';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import userEvent from '@testing-library/user-event';
import { CLEAR_CART, REMOVE_FROM_CART } from '../context/cart/CartActions';
import { apiClient } from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getByHref: vi.fn(),
        deleteByHref: vi.fn(),
        patchNoBodyByHref: vi.fn(),
        postByHref: vi.fn(),
        getShoppingCartAllowedMethods: vi.fn(),
        getAllowedMethodsByHref: vi.fn(),
    },
}));

const mockDispatch = vi.fn();

const mockContextValue = {
    state: {
        cart: {
            items: [
                {
                    id: 'cart-line-1',
                    name: 'Dune',
                    price: '12.50 EUR',
                    priceValue: 12.50,
                    currency: 'EUR',
                    deleteHref:
                        'http://localhost:8081/shopping-carts/SC-1/shopping-cart-lines/SCL-1',
                },
                {
                    id: 'cart-line-2',
                    name: '1984',
                    price: '10.00 EUR',
                    priceValue: 10,
                    currency: 'EUR',
                    deleteHref:
                        'http://localhost:8081/shopping-carts/SC-1/shopping-cart-lines/SCL-2',
                },
            ],
        },
    },
    dispatch: mockDispatch,
};

function renderComponent(props = {}, contextValue = mockContextValue) {
    return render(
        <AppContext.Provider value={contextValue}>
            <ShoppingCart opened={true} onClose={vi.fn()} {...props} />
        </AppContext.Provider>
    );
}

describe('ShoppingCart', () => {
    beforeEach(() => {
        mockDispatch.mockClear();
        vi.mocked(apiClient.getAllowedMethodsByHref).mockResolvedValue([
            'GET',
            'PATCH',
            'DELETE',
            'POST',
            'OPTIONS',
        ]);
        vi.mocked(apiClient.getShoppingCartAllowedMethods).mockResolvedValue([
            'OPTIONS',
        ]);
    });

    it('renders modal title when opened', () => {
        renderComponent();

        expect(
            screen.getByRole('heading', { name: /shopping cart/i })
        ).toBeInTheDocument();
    });

    it('shows empty cart message when cart has no items', () => {
        const emptyCartContext = {
            state: {
                cart: {
                    items: [],
                },
            },
            dispatch: mockDispatch,
        };

        renderComponent({}, emptyCartContext);

        expect(screen.getByText(/your cart is empty/i)).toBeInTheDocument();
    });

    it('shows empty cart message when cart is undefined', () => {
        const contextWithoutCart = {
            state: {},
            dispatch: mockDispatch,
        };

        renderComponent({}, contextWithoutCart);

        expect(screen.getByText(/your cart is empty/i)).toBeInTheDocument();
    });

    it('renders all cart items when cart has items', () => {
        renderComponent();

        expect(screen.getByText('Dune')).toBeInTheDocument();
        expect(screen.getByText('12.50 EUR')).toBeInTheDocument();

        expect(screen.getByText('1984')).toBeInTheDocument();
        expect(screen.getByText('10.00 EUR')).toBeInTheDocument();
    });

    it('renders checkout button', () => {
        renderComponent();

        expect(
            screen.getByRole('button', { name: /checkout/i })
        ).toBeInTheDocument();
    });

    it('creates a persisted Sale from the cart during checkout', async () => {
        const user = userEvent.setup();
        const checkoutContext = {
            ...mockContextValue,
            state: {
                ...mockContextValue.state,
                app: {
                    shoppingCartHref: 'http://localhost:8081/shopping-carts',
                },
            },
        };

        vi.mocked(apiClient.getByHref)
            .mockResolvedValueOnce({
                _links: {
                    self: { href: 'http://localhost:8081/shopping-carts/SC-A49F78E2' },
                },
            })
            .mockResolvedValueOnce({
                _links: {
                    sale: { href: 'http://localhost:8081/sales' },
                },
            });
        vi.mocked(apiClient.postByHref).mockResolvedValueOnce({
            _links: {
                self: { href: 'http://localhost:8081/sales/SA-1234ABCD' },
            },
        });

        renderComponent({}, checkoutContext);
        await user.click(screen.getByRole('button', { name: /checkout/i }));
        await user.click(await screen.findByRole('button', { name: /confirm purchase/i }));

        await waitFor(() => {
            expect(apiClient.postByHref).toHaveBeenCalledWith(
                'http://localhost:8081/sales',
                { shoppingCartId: 'SC-A49F78E2' },
            );
        });
        expect(mockDispatch).toHaveBeenCalledWith({ type: CLEAR_CART });
        expect(await screen.findByText(/sale sa-1234abcd was completed successfully/i))
            .toBeInTheDocument();
    });

    it('removes the selected item from the cart', async () => {
        const user = userEvent.setup();
        renderComponent();

        await user.click(
            screen.getByRole('button', { name: /remove dune from cart/i })
        );

        expect(mockDispatch).toHaveBeenCalledWith({
            type: REMOVE_FROM_CART,
            payload: { id: 'cart-line-1' },
        });
    });

    it('does not render modal content when closed', () => {
        renderComponent({ opened: false });

        expect(screen.queryByText(/shopping cart/i)).not.toBeInTheDocument();
        expect(screen.queryByText('Dune')).not.toBeInTheDocument();
        expect(screen.queryByRole('button', { name: /checkout/i })).not.toBeInTheDocument();
    });

});
