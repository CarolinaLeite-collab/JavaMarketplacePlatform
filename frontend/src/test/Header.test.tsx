import { beforeEach, describe, expect, it, vi } from 'vitest';
import userEvent from '@testing-library/user-event';
import { axe, render, screen, within } from '@/test-utils';
import AppContext from '../context/AppContext';
import { Header } from '../components/header/Header';

const mockUser = vi.hoisted(() => ({
    currentUser: 'pedro@aeiou.com',
}));

vi.mock('../context/UserContext', () => ({
    useUser: () => ({
        currentUser: mockUser.currentUser,
        toggleUser: vi.fn(),
    }),
}));

vi.mock('../components/shoppingCart/ShoppingCart.tsx', () => ({
    ShoppingCart: () => null,
}));

function renderHeader(salesHref: string | null = '/sales') {
    const state = {
        app: {
            myListsHref: '/my-lists',
            libraryHref: '/my-library',
            salesHref,
        },
        cart: {
            items: [],
        },
    } as any;

    return render(
        <AppContext.Provider value={{ state, dispatch: vi.fn() }}>
            <Header />
        </AppContext.Provider>,
    );
}

describe('Header', () => {
    beforeEach(() => {
        mockUser.currentUser = 'pedro@aeiou.com';
    });

    axe([<Header key="1" />]);

    it('shows Purchases for an authenticated user when salesHref is available', () => {
        renderHeader();

        expect(
            screen.getByRole('link', { name: /purchases/i }),
        ).toHaveAttribute('href', '/sales');
    });

    it('hides Purchases from unauthenticated users', () => {
        mockUser.currentUser = 'guest@aeiou.com';

        renderHeader();

        expect(
            screen.queryByRole('link', { name: /purchases/i }),
        ).not.toBeInTheDocument();
    });

    it('hides Purchases when salesHref is unavailable', () => {
        renderHeader(null);

        expect(
            screen.queryByRole('link', { name: /purchases/i }),
        ).not.toBeInTheDocument();
    });

    it('links to Purchases in desktop and mobile navigation', async () => {
        const user = userEvent.setup();
        renderHeader();

        expect(
            screen.getByRole('link', { name: /purchases/i }),
        ).toHaveAttribute('href', '/sales');

        await user.click(
            screen.getByRole('button', { name: /toggle navigation/i }),
        );

        const drawer = await screen.findByRole('dialog');
        expect(
            within(drawer).getByRole('link', { name: /purchases/i }),
        ).toHaveAttribute('href', '/sales');
    });
});
