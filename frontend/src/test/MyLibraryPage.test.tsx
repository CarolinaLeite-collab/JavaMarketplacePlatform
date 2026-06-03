import { axe, render, screen, within } from '@/test-utils';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';
import userEvent from "@testing-library/user-event";
import { LibraryContext } from "../context/AppContext";

describe('MyLibraryPage', () => {
    axe([<MyLibraryPage key="1" />]);

    it('renders correctly', () => {
        render(<MyLibraryPage />);
    });

    it('renders the page title', () => {
        render(<MyLibraryPage />);
        expect(screen.getByRole('heading', { name: /my library/i })).toBeInTheDocument();
    });

    it('renders the page subtitle', () => {
        render(<MyLibraryPage />);
        expect(screen.getByText(/check out your items/i)).toBeInTheDocument();
    });

    it('renders the add item button', () => {
        render(<MyLibraryPage />);

        expect(
            screen.getByRole('button', { name: /add item/i })
        ).toBeInTheDocument();
    });

    it('opens the add item modal when clicking the add item button', async () => {
        const user = userEvent.setup();

        render(<MyLibraryPage />);

        await user.click(screen.getByRole('button', { name: /add item/i }));

        const dialog = await within(document.body).findByRole('dialog');
        expect(dialog).toBeInTheDocument();
    });

    it('shows loading state', () => {
        render(
            <LibraryContext.Provider
                value={{
                    state: {
                        items: [],
                        details: {},
                        loading: true,
                        error: null
                    },
                    dispatch: vi.fn()
                }}
            >
                <MyLibraryPage />
            </LibraryContext.Provider>
        );

        expect(screen.getByText(/loading/i)).toBeInTheDocument();
    });

    it('shows error state', () => {
        render(
            <LibraryContext.Provider
                value={{
                    state: {
                        items: [],
                        details: {},
                        loading: false,
                        error: 'Failed'
                    },
                    dispatch: vi.fn()
                }}
            >
                <MyLibraryPage />
            </LibraryContext.Provider>
        );

        expect(screen.getByText(/failed/i)).toBeInTheDocument();
    });

});

