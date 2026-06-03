import { axe, render, screen, within } from '@/test-utils';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';
import userEvent from "@testing-library/user-event";

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

    it('renders the create a sale button', () => {
        render(<MyLibraryPage />);

        expect(
            screen.getByRole('button', { name: /create a sale/i })
        ).toBeInTheDocument();
    });

    it('opens the add item modal when clicking the add item button', async () => {
        const user = userEvent.setup();

        render(<MyLibraryPage />);

        await user.click(screen.getByRole('button', { name: /add item/i }));

        const dialog = await screen.findByRole('dialog', { name: /add item/i });
        expect(
            within(dialog).getByRole('heading', { name: /add item/i })
        ).toBeInTheDocument();
    });

    it('opens the create new sale modal when clicking the create a sale button', async () => {
        const user = userEvent.setup();

        render(<MyLibraryPage />);

        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        const dialog = await screen.findByRole('dialog', { name: /create new sale/i });
        expect(dialog).toBeInTheDocument();

        expect(
            within(dialog).getByRole('heading', { name: /create new sale/i })
        ).toBeInTheDocument();
    });

});

