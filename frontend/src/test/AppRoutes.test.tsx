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

describe('AppRoutes', () => {
    it('renders Marketplace on default route', () => {
        render(
            <AppContext.Provider value={{ state: mockState }}>
                <AppRoutes />
            </AppContext.Provider>
        );

        expect(
            screen.getByRole('heading', { name: /marketplace/i })
        ).toBeInTheDocument();
    });

    it('renders MyLibraryPage on /my-library route', () => {
        render(
            <AppContext.Provider value={{ state: mockState }}>
                <AppRoutes />
            </AppContext.Provider>,
            { initialEntries: ['/my-library'] }
        );

        expect(screen.getByText(/my library/i)).toBeInTheDocument();
    });
});