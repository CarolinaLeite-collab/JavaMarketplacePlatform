import {axe, render, screen} from '@/test-utils';
import MyListsPage from '../pages/MyLists/MyListsPage';

describe('MyListsPage', () => {
    axe([<MyListsPage key="1" />]);

    it('renders correctly', () => {
        render(<MyListsPage />);
    });

    it('renders the page title', () => {
        render(<MyListsPage />);
        expect(screen.getByRole('heading', { name: /my lists/i })).toBeInTheDocument();
    });
});