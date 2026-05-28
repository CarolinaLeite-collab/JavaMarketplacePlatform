import { axe, render, screen } from '@/test-utils';
import MyLibraryPage from '../pages/MyLibrary/MyLibraryPage';

describe('MyLibraryPage', () => {
    axe([<MyLibraryPage key="1" />]);

    it('renders correctly', () => {
        render(<MyLibraryPage />);
    });

    it('renders the page heading', () => {
        render(<MyLibraryPage />);
        expect(screen.getByRole('heading', { name: /my library/i })).toBeInTheDocument();
    });
});