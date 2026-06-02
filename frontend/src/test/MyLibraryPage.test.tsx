import { axe, render, screen } from '@/test-utils';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';

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

    it('renders the initial library items', () => {
        render(<MyLibraryPage />);

        expect(screen.getByText(/the war of the worlds/i)).toBeInTheDocument();
        expect(screen.getByText(/duna/i)).toBeInTheDocument();
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

        expect(screen.getByRole('dialog')).toBeInTheDocument();
    });
});

