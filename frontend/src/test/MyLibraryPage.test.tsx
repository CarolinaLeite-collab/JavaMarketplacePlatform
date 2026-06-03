import { axe, render, screen, within } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';
import { LibraryContext } from '../context/AppContext';

const mockState = {
    items: [],
    details: {},
    loading: false,
    error: null,
};

function renderWithLibraryProvider(state = mockState) {
    return render(
        <LibraryContext.Provider
            value={{
                state,
                dispatch: vi.fn(),
            }}
        >
            <MyLibraryPage />
        </LibraryContext.Provider>
    );
}

describe('MyLibraryPage', () => {
    axe([
        <LibraryContext.Provider
            key="1"
            value={{
                state: mockState,
                dispatch: vi.fn(),
            }}
        >
            <MyLibraryPage />
        </LibraryContext.Provider>,
    ]);

    it('renders correctly', () => {
        renderWithLibraryProvider();
    });

    it('renders the page title', () => {
        renderWithLibraryProvider();

        expect(
            screen.getByRole('heading', { name: /my library/i })
        ).toBeInTheDocument();
    });

    it('renders the page subtitle', () => {
        renderWithLibraryProvider();

        expect(
            screen.getByText(/check out your items/i)
        ).toBeInTheDocument();
    });

    it('renders the add item button', () => {
        renderWithLibraryProvider();

        expect(
            screen.getByRole('button', { name: /add item/i })
        ).toBeInTheDocument();
    });

    it('opens the add item modal when clicking the add item button', async () => {
        const user = userEvent.setup();

        renderWithLibraryProvider();

        await user.click(
            screen.getByRole('button', { name: /add item/i })
        );

        const dialog = await within(document.body).findByRole('dialog');

        expect(dialog).toBeInTheDocument();
    });

    it('shows loading state', () => {
        renderWithLibraryProvider({
            items: [],
            details: {},
            loading: true,
            error: null,
        });

        expect(screen.getByText(/loading/i)).toBeInTheDocument();
    });

    it('shows error state', () => {
        renderWithLibraryProvider({
            items: [],
            details: {},
            loading: false,
            error: 'Failed',
        });

        expect(screen.getByText(/failed/i)).toBeInTheDocument();
    });
});