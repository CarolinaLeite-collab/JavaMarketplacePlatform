import { axe, render, screen, within } from '@/test-utils';
import { vi } from 'vitest';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';
import userEvent from "@testing-library/user-event";
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
            value={{ state: mockState, dispatch: vi.fn() }}
        >
            <MyLibraryPage />
        </LibraryContext.Provider>
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

    it('renders the create a sale button', () => {
        renderWithLibraryProvider();

        expect(
            screen.getByRole('button', { name: /create a sale/i })
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

    it('opens the create new sale modal when clicking the create a sale button', async () => {
        const user = userEvent.setup();

        renderWithLibraryProvider();

        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        const dialog = await within(document.body).findByRole('dialog');

        expect(dialog).toBeInTheDocument();
    });

    it('renders create sale button', () => {
        render(<MyLibraryPage />);

        expect(
            screen.getByRole('button', { name: /create a sale/i })
        ).toBeInTheDocument();
    });

    it('opens the create sale modal when clicking create sale button', async () => {
        const user = userEvent.setup();

        render(<MyLibraryPage />);

        await user.click(
            screen.getByRole('button', { name: /create a sale/i })
        );

        expect(
            await within(document.body).findByRole('dialog')
        ).toBeInTheDocument();
    });
});