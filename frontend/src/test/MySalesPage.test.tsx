import { axe, render, screen } from '@/test-utils';
import MySalesPage from '../pages/MySales/MySalesPage';

describe('MySalesPage', () => {
    axe([<MySalesPage key="1" />]);

    it('renders correctly', () => {
        render(<MySalesPage />);
    });

    it('renders the page heading', () => {
        render(<MySalesPage />);
        expect(screen.getByRole('heading', { name: /my sales/i })).toBeInTheDocument();
    });
});