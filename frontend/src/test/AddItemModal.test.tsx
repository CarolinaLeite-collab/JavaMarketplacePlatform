import { render, screen } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';

describe('MyLibraryPage', () => {
    it('renders initial items', () => {
        render(<MyLibraryPage />);

        expect(screen.getByText(/the war of the worlds/i)).toBeInTheDocument();
        expect(screen.getByText(/duna/i)).toBeInTheDocument();
    });

    it('opens the add item modal', async () => {
        const user = userEvent.setup();
        render(<MyLibraryPage />);

        await user.click(screen.getByRole('button', { name: /add item/i }));

        expect(screen.getByRole('dialog')).toBeInTheDocument();
        expect(screen.getByText(/publication/i)).toBeInTheDocument();
    });

    it('adds a new item to the library', async () => {
        const user = userEvent.setup();
        render(<MyLibraryPage />);

        await user.click(screen.getByRole('button', { name: /add item/i }));

        // Publication
        await user.type(screen.getByLabelText(/title/i), 'Clean Code');
        await user.type(screen.getByLabelText(/author/i), 'Robert C. Martin');
        await user.type(screen.getByLabelText(/release year/i), '2008');
        await user.type(screen.getByLabelText(/genre/i), 'Programming');

        await user.click(screen.getByRole('button', { name: /next/i }));

        // Edition
        await user.type(screen.getByLabelText(/publication type/i), 'Book');
        await user.type(screen.getByLabelText(/isbn\/issn/i), '9780132350884');
        await user.type(screen.getByLabelText(/language/i), 'English');
        await user.type(screen.getByLabelText(/publishing company/i), 'Prentice Hall');
        await user.type(screen.getByLabelText(/publishing year/i), '2008');

        await user.type(screen.getByLabelText(/weight/i), '800g');
        await user.type(screen.getByLabelText(/dimension/i), '23 x 18 cm');
        await user.type(screen.getByLabelText(/binding/i), 'Paperback');
        await user.type(screen.getByLabelText(/number of pages/i), '464');
        await user.type(screen.getByLabelText(/edition number/i), '1');

        await user.click(screen.getByRole('button', { name: /next/i }));

        // Item
        await user.click(screen.getByLabelText(/condition/i));
        await user.click(screen.getByText('GOOD'));

        await user.type(
            screen.getByLabelText(/description/i),
            'Used copy in good condition'
        );

        await user.type(
            screen.getByLabelText(/picture url/i),
            'https://example.com/clean-code.jpg'
        );

        await user.click(screen.getByRole('button', { name: /create item/i }));
        await user.click(screen.getByRole('button', { name: /add to library/i }));

        expect(screen.getByText(/clean code/i)).toBeInTheDocument();
        expect(screen.getByText(/robert c. martin/i)).toBeInTheDocument();
        expect(screen.getByText(/9780132350884/i)).toBeInTheDocument();
    });
});