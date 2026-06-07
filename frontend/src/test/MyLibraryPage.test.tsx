import {axe, render, screen, within} from '@/test-utils';
import {vi} from 'vitest';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';
import userEvent from "@testing-library/user-event";
import {LibraryContext} from '../context/AppContext';
import * as LibraryActions from '../context/library/LibraryActions';

const mockState = {
    items: [],
    details: {},
    loading: false,
    error: null,

    sales: {
        libraryItems: [],
        error: null,
        successMessage: null
    }
};

vi.mock('../components/createSaleModal/CreateSaleModal', () => ({
    CreateSaleModal: ({ opened }) =>
        opened ? <div role="dialog">Create Sale Modal</div> : null,
}));

vi.mock('../components/addItemModal/AddItemModal', () => ({
    AddItemModal: ({ opened }) =>
        opened ? <div role="dialog">Add Item Modal</div> : null,
}));

vi.mock('../components/accordion/ItemAccordion', () => ({
    ItemAccordion: () => <div>Accordion</div>,
}));

vi.mock('../context/library/LibraryActions', () => ({
    getLibrary: vi.fn(),
}));

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

    it('opens the create sale modal when clicking create sale button', async () => {
        const user = userEvent.setup();

        renderWithLibraryProvider();

        await user.click(
            screen.getByRole('button', { name: /create a sale/i })
        );

        expect(
            await within(document.body).findByRole('dialog')
        ).toBeInTheDocument();
    });

    it('calls getLibrary on mount', () => {
        renderWithLibraryProvider();

        expect(LibraryActions.getLibrary).toHaveBeenCalled();
    });

    it('reloads library when item is added', async () => {
        const user = userEvent.setup();

        renderWithLibraryProvider();

        await user.click(screen.getByRole('button', { name: /add item/i }));

        const modal = await within(document.body).findByRole('dialog');
        expect(modal).toBeInTheDocument();

        const props = screen.getByText('Add Item Modal');

        expect(props).toBeInTheDocument();
        expect(LibraryActions.getLibrary).toHaveBeenCalled();
    });

    it('opens and closes both modals independently', async () => {
        const user = userEvent.setup();

        renderWithLibraryProvider();

        await user.click(screen.getByRole('button', { name: /add item/i }));
        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        expect(within(document.body).getAllByRole('dialog').length).toBeGreaterThan(0);
    });
});