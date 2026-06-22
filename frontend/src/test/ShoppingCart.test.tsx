import { render, screen } from '@/test-utils';
import AppContext from '../context/AppContext';
import { ShoppingCart } from '../components/shoppingCart/ShoppingCart.tsx';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import userEvent from '@testing-library/user-event';
import { REMOVE_FROM_CART } from '../context/cart/CartActions';
import { apiClient } from '../services/apiClient';

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getByHref: vi.fn(),
        deleteByHref: vi.fn(),
        patchNoBodyByHref: vi.fn(),
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

    //Test denied DELETE
    vi.mocked(
        apiClient.getAllowedMethodsByHref
    ).mockResolvedValue(['GET', 'OPTIONS']);
    expect(apiClient.deleteByHref).not.toHaveBeenCalled();
    expect(mockDispatch).not.toHaveBeenCalledWith(
        expect.objectContaining({
            type: REMOVE_FROM_CART,
        }),
    );

    //Test denied PATCH
    expect(
        apiClient.patchNoBodyByHref
    ).not.toHaveBeenCalled();



});
