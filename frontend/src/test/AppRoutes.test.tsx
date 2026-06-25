import { axe, render, screen } from '../test-utils';
import { AppRoutes } from '../routes/AppRoutes';
import AppContext from "../context/AppContext";
vi.mock('../context/UserContext', () => ({
    useUser: vi.fn(),
}));

import { useUser } from '../context/UserContext';

vi.mock('../pages/MyLists/MyListsPage', () => ({
    default: () => <h1>My Lists</h1>,
}));

vi.mock('../pages/MyLibrary/MyLibraryPage', () => ({
    default: () => <h1>My Library</h1>,
}));

vi.mock('../pages/AuctionDetail/AuctionDetailPage', () => ({
    default: () => <h1>Auction Detail</h1>,
}));

vi.mock('../pages/Sales/SalesPage', () => ({
    default: () => <h1>Sales</h1>,
}));

function renderRoutes(initialEntries = ['/'], appStateOverrides = {}) {
    const state = {
        app: {
            myListsHref: 'http://localhost:8081/my-lists/',
            libraryHref: 'http://localhost:8081/my-library/',
            salesHref: 'http://localhost:8081/sales/',
            ...appStateOverrides,
        },
        lists: {
            lists: [],
            genres: [],
            error: null,
        },
        sales: {
            directSales: [],
            error: null,
            successMessage: null,
            libraryItems: [],
        },
    };

    return render(
        <AppContext.Provider value={{ state, dispatch: () => {} }}>
            <AppRoutes />
        </AppContext.Provider>,
        { initialEntries }
    );
}

describe('AppRoutes', () => {
    beforeEach(() => {
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'user@test.com',
        });
    });

    axe([<AppRoutes key="1" />]);

    it('renders correctly', () => {
        renderRoutes();
    });

    it('renders Marketplace on default route', () => {
        renderRoutes();
        expect(screen.getByRole('heading', { name: /marketplace/i })).toBeInTheDocument();
    });

    it('renders MyListsPage on /my-lists route', () => {
        renderRoutes(['/lists/my-lists']);
        expect(screen.getByRole('heading', { name: /my lists/i })).toBeInTheDocument();
    });

    it('renders MyLibraryPage on /my-library route', () => {
        renderRoutes(['/my-library']);
        expect(screen.getByRole('heading', { name: /my library/i })).toBeInTheDocument();
    });

    it('renders AuctionDetailPage on /auctions/:auctionId route', () => {
        renderRoutes(['/auctions/test-123']);
        expect(screen.getByRole('heading', { name: /auction detail/i })).toBeInTheDocument();
    });

    it('renders SalesPage on /sales when user is authenticated and salesHref exists', () => {
        renderRoutes(['/sales']);

        expect(screen.getByRole('heading', { name: /sales/i })).toBeInTheDocument();
    });

    it('redirects guest user from /sales to Marketplace', () => {
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'guest@aeiou.com',
        });

        renderRoutes(['/sales']);

        expect(screen.getByRole('heading', { name: /marketplace/i })).toBeInTheDocument();
    });

    it('redirects from /sales to Marketplace when salesHref is missing', () => {
        renderRoutes(['/sales'], {
            salesHref: null,
        });

        expect(screen.getByRole('heading', { name: /marketplace/i })).toBeInTheDocument();
    });

});
