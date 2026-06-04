import { render, screen } from '@/test-utils';
import { AppRoutes } from '../routes/AppRoutes';
import AppContext from "../context/AppContext";

vi.mock('../pages/MyLists/MyListsPage', () => ({
    default: () => <h1>My Lists</h1>,
}));

vi.mock('../pages/MyLibrary/MyLibraryPage', () => ({
    default: () => <h1>My Library</h1>,
}));

const mockState = {
    app: {
        myListsHref: '/my-lists',
        libraryHref: '/my-library',
    },
};
import AppContext from '../context/AppContext';

function renderRoutes(initialEntries = ['/'], appStateOverrides = {}) {
    const state = {
        app: {
            myListsHref: 'http://localhost:8081/my-lists/',
            libraryHref: 'http://localhost:8081/my-library/',
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
    axe([<AppRoutes key="1" />]);

    it('renders correctly', () => {
        renderRoutes();
    });

    it('renders Marketplace on default route', () => {
        render(
            <AppContext.Provider value={{ state: mockState }}>
                <AppRoutes />
            </AppContext.Provider>
        );
        renderRoutes();
        expect(screen.getByRole('heading', { name: /marketplace/i })).toBeInTheDocument();
    });

        expect(
            screen.getByRole('heading', { name: /marketplace/i })
        ).toBeInTheDocument();
    it('renders MyListsPage on /my-lists route', () => {
        renderRoutes(['/my-lists']);
        expect(screen.getByRole('heading', { name: /my lists/i })).toBeInTheDocument();
    });

    it('renders MyLibraryPage on /my-library route', () => {
        render(
            <AppContext.Provider value={{ state: mockState }}>
                <AppRoutes />
            </AppContext.Provider>,
            { initialEntries: ['/my-library'] }
        );

        expect(screen.getByText(/my library/i)).toBeInTheDocument();
        renderRoutes(['/my-library']);
        expect(screen.getByRole('heading', { name: /my library/i })).toBeInTheDocument();
    });
});
